/**
 * Licensed to the Austrian Association for Software Tool Integration (AASTI)
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. The AASTI licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openengsb.core.services.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openengsb.core.api.LinkingSupport;
import org.openengsb.core.api.model.ModelDescription;
import org.openengsb.core.api.model.ModelWrapper;
import org.openengsb.core.api.model.OpenEngSBModel;
import org.openengsb.core.api.model.QueryRequest;
import org.openengsb.core.api.xlink.exceptions.DomainNotLinkableException;
import org.openengsb.core.api.xlink.model.ModelViewMapping;
import org.openengsb.core.api.xlink.model.XLinkConnectorRegistration;
import org.openengsb.core.api.xlink.model.XLinkConnectorView;
import org.openengsb.core.api.xlink.model.XLinkObject;
import org.openengsb.core.api.xlink.service.XLinkConnectorManager;
import org.openengsb.core.ekb.api.EDBQueryFilter;
import org.openengsb.core.ekb.api.EKBService;
import org.openengsb.core.ekb.api.SingleModelQuery;
import org.openengsb.core.ekb.api.TransformationEngine;

public class XLinkConnectorManagerImpl extends ConnectorManagerImpl implements XLinkConnectorManager {
    private Map<String, XLinkConnectorRegistration> xLinkRegistrations = new HashMap<>();
    private String xLinkBaseUrl;
    private int xLinkExpiresIn = 3;

    private TransformationEngine transformationEngine;
    private EKBService queryService;

    @Override
    public void registerWithXLink(String connectorId, String remoteHostId, String toolName,
        ModelViewMapping... modelViewMappings) {
        // TODO: check that connector exists
        // TODO: overwrite delete connector s.t. it also removes xlink registration
        // if (!connectorExists(connectorId)) {
        // throw new IllegalArgumentException("unknown connector id");
        // }
        Map<ModelDescription, XLinkConnectorView[]> modelViewsMap =
            convertToMapWithModelDescriptionAsKey(modelViewMappings);
        synchronized (xLinkRegistrations) {
            xLinkRegistrations.put(connectorId, new XLinkConnectorRegistration(remoteHostId, connectorId, toolName,
                    modelViewsMap));
        }
    }

    @Override
    public void unregisterFromXLink(String connectorId) {
        synchronized (xLinkRegistrations) {
            xLinkRegistrations.remove(connectorId);
        }
    }

    @Override
    public List<XLinkConnectorRegistration> getXLinkRegistrations(String hostId) {
        List<XLinkConnectorRegistration> registrationsOnHost = new ArrayList<>();
        synchronized (xLinkRegistrations) {
            for (XLinkConnectorRegistration registration : xLinkRegistrations.values()) {
                if (registration.getHostId().equals(hostId)) {
                    registrationsOnHost.add(registration);
                }
            }
        }
        return registrationsOnHost;
    }

    @Override
    public String publishXLink(String connectorId, Object modelObject, boolean hostOnly) {
        XLinkConnectorRegistration registration = xLinkRegistrations.get(connectorId);
        Collection<XLinkConnectorRegistration> registrations =
            hostOnly ? getXLinkRegistrations(registration.getHostId()) : xLinkRegistrations.values();

        ModelDescription modelDescription = ModelWrapper.wrap(modelObject).getModelDescription();
        for (XLinkConnectorRegistration r : registrations) {
            List<XLinkObject> xLinkObjects = collectXLinkObjects(modelObject, modelDescription, r);
            if (!xLinkObjects.isEmpty()) {
                Object connector = getUtilsService().getService("(service.pid=" + r.getConnectorId() + ")", 100L);
                if (connector == null) {
                    throw new IllegalStateException("registered connector not there");
                }
                try {
                    LinkingSupport service = (LinkingSupport) connector;
                    service.openXLinks(xLinkObjects);
                } catch (ClassCastException e) {
                    throw new DomainNotLinkableException();
                }
            }
        }

        return generateXLink(connectorId, modelObject);
    }

    private List<XLinkObject> collectXLinkObjects(Object modelObject, ModelDescription modelDescription,
        XLinkConnectorRegistration registration) {
        List<XLinkObject> xLinkObjects = new ArrayList<>();
        for (Entry<ModelDescription, XLinkConnectorView[]> entry : registration.getModelsToViews().entrySet()) {
            if (modelDescription.equals(entry.getKey())) {
                xLinkObjects.add(new XLinkObject(modelObject, modelDescription, Arrays.asList(entry.getValue())));
            } else if (transformationEngine.isTransformationPossible(modelDescription, entry.getKey())) {
                Object transformedObject = transformAndMerge(modelDescription, entry.getKey(), modelObject);
                xLinkObjects.add(new XLinkObject(transformedObject, entry.getKey(), Arrays.asList(entry.getValue())));
            }
        }

        return xLinkObjects;
    }

    private Object transformAndMerge(ModelDescription sourceModel, ModelDescription targetModel, Object modelObject) {
        Object transformedObject = transformationEngine.performTransformation(sourceModel, targetModel, modelObject);
        if (transformedObject instanceof OpenEngSBModel
            && ((OpenEngSBModel) transformedObject).retrieveInternalModelId() != null) {
            List<?> result =
                queryService.query(new SingleModelQuery(transformedObject.getClass(), new EDBQueryFilter(QueryRequest
                        .query(((OpenEngSBModel) transformedObject).retrieveInternalModelIdName(),
                                ((OpenEngSBModel) transformedObject).retrieveInternalModelId())), null));
            // queryService.query(transformedObject.getClass(), QueryRequest.query(
            // ((OpenEngSBModel) transformedObject).retrieveInternalModelIdName(),
            // ((OpenEngSBModel) transformedObject).retrieveInternalModelId()));
            if (!result.isEmpty()) {
                transformedObject =
                    transformationEngine.performTransformation(sourceModel, targetModel, modelObject, result.get(0));
            }
        }
        return transformedObject;
    }

    @Override
    public String generateXLink(String connectorId, Object modelObject) {
        StringBuilder sb = new StringBuilder(xLinkBaseUrl);
        appendXLinkProperty(sb, "expiresIn", String.valueOf(xLinkExpiresIn));
        // TODO: append the other properties
        return sb.toString();
    }

    private StringBuilder appendXLinkProperty(StringBuilder sb, String propertyName, String propertyValue) {
        sb.append('&');
        sb.append(propertyName);
        sb.append('=');
        sb.append(propertyValue);
        return sb;
    }

    private Map<ModelDescription, XLinkConnectorView[]> convertToMapWithModelDescriptionAsKey(
        ModelViewMapping[] modelViewMappings) {
        Map<ModelDescription, XLinkConnectorView[]> convertedMap = new HashMap<>();
        for (ModelViewMapping mapping : modelViewMappings) {
            convertedMap.put(mapping.getDescription(), mapping.getViews());
        }
        return convertedMap;
    }

    public String getxLinkBaseUrl() {
        return xLinkBaseUrl;
    }

    public void setxLinkBaseUrl(String xLinkBaseUrl) {
        this.xLinkBaseUrl = xLinkBaseUrl;
    }

    public int getxLinkExpiresIn() {
        return xLinkExpiresIn;
    }

    public void setxLinkExpiresIn(int xLinkExpiresIn) {
        this.xLinkExpiresIn = xLinkExpiresIn;
    }

    public void setTransformationEngine(TransformationEngine transformationEngine) {
        this.transformationEngine = transformationEngine;
    }
}
