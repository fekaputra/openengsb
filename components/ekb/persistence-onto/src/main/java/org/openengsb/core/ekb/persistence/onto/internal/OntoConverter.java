package org.openengsb.core.ekb.persistence.onto.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.openengsb.core.api.context.ContextHolder;
import org.openengsb.core.api.model.FileWrapper;
import org.openengsb.core.api.model.ModelWrapper;
import org.openengsb.core.api.model.OpenEngSBModel;
import org.openengsb.core.api.model.OpenEngSBModelEntry;
import org.openengsb.core.ekb.api.ConnectorInformation;
import org.openengsb.core.ekb.api.EKBCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.ontology.AnnotationProperty;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.XSD;

public class OntoConverter {
    private static final Logger LOGGER = LoggerFactory.getLogger(OntoConverter.class);

    public static final String FILEWRAPPER_FILENAME_SUFFIX = ".filename";

    public OntoConverter() {

        LOGGER.info("OntoConverter Start! ");
    }

    /**
     * Converts the models of an EKBCommit to EDBObjects and return an object
     * which contains the three corresponding lists
     */
    public OntoCommit convertEKBCommit(EKBCommit commit) {
        ConnectorInformation information = commit.getConnectorInformation();
        OntoCommit result = new OntoCommit();

        result.setTimestamp(System.currentTimeMillis());
        result.setConnectorId(information.getConnectorId());
        result.setDomainId(information.getDomainId());
        result.setInstanceId(information.getInstanceId());
        result.setContextId(ContextHolder.get().getCurrentContextId());

        result.setInsertModel(convertModelsToJenaResources(commit.getInserts()));
        result.setUpdateModel(convertModelsToJenaResources(commit.getUpdates()));
        result.setDeleteModel(convertModelsToJenaResources(commit.getDeletes()));

        LOGGER.info("OntoConverter: " + result);

        return result;
    }

    /**
     * Convert a list of models to a list of EDBObjects (the version retrieving
     * is not considered here. This is done in the EDB directly).
     */
    public OntModel convertModelsToJenaResources(List<OpenEngSBModel> instances) {

        OntModel model = null;

        if (instances != null && !instances.isEmpty()) {
            model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.setNsPrefix("", OntoConstants.CDL_NAMESPACE);

            List<Resource> result = new ArrayList<>();
            if (instances != null) {
                for (Object instance : instances) {
                    result.addAll(convertModelToJenaResource(instance, model));
                }
            }
            OwlHelper.save(model, "src/test/resources/test-insert.owl");
        }
        return model;
    }

    /**
     * Converts an OpenEngSBModel object to an EDBObject (the version retrieving
     * is not considered here. This is done in the EDB directly).
     */
    public List<Resource> convertModelToJenaResource(Object instance, OntModel model) {
        if (!OpenEngSBModel.class.isAssignableFrom(instance.getClass())) {
            throw new IllegalArgumentException("This function need to get a model passed");
        }
        List<Resource> objects = new ArrayList<>();
        if (instance != null) {
            convertSubModel((OpenEngSBModel) instance, objects, model);
        }
        return objects;
    }

    /**
     * Recursive function to generate a list of EDBObjects out of a model
     * object.
     */
    private Resource convertSubModel(OpenEngSBModel instance, List<Resource> objects, OntModel model) {
        String oid = ModelWrapper.wrap(instance).getCompleteModelOID();

        OntClass modelCls = model.createClass(OntoConstants.CDL_NAMESPACE + instance.getClass().getSimpleName());
        Individual object = model.createIndividual(OntoConstants.CDL_NAMESPACE + oid, modelCls);

        for (OpenEngSBModelEntry entry : instance.toOpenEngSBModelEntries()) {
            if (entry.getValue() == null) {
                continue;
            } else if (entry.getType().equals(FileWrapper.class)) {
                try {
                    FileWrapper wrapper = (FileWrapper) entry.getValue();
                    String content = Base64.encodeBase64String(wrapper.getContent());

                    DatatypeProperty datatypeProp = model.createDatatypeProperty(OntoConstants.CDL_NAMESPACE
                            + entry.getKey());
                    datatypeProp.addDomain(modelCls);
                    datatypeProp.addRange(XSD.xstring);

                    object.addProperty(datatypeProp, content);
                    object.addProperty(
                            model.createDatatypeProperty(OntoConstants.CDL_NAMESPACE + entry.getKey()
                                    + FILEWRAPPER_FILENAME_SUFFIX), wrapper.getFilename());
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                }
            } else if (OpenEngSBModel.class.isAssignableFrom(entry.getType())) {
                OpenEngSBModel temp = (OpenEngSBModel) entry.getValue();
                Resource objProperty = convertSubModel(temp, objects, model);

                ObjectProperty objProp = model.createObjectProperty(OntoConstants.CDL_NAMESPACE + entry.getKey());
                objProp.addDomain(modelCls);
                objProp.addRange(model.createClass(OntoConstants.CDL_NAMESPACE + temp.getClass().getSimpleName()));

                object.addProperty(objProp, objProperty);
            } else if (List.class.isAssignableFrom(entry.getType())) {
                List<?> list = (List<?>) entry.getValue();
                if (list == null || list.size() == 0) {
                    continue;
                }
                Boolean modelItems = null;
                for (int i = 0; i < list.size(); i++) {
                    Object item = list.get(i);
                    ObjectProperty property = null;
                    if (modelItems == null) {
                        modelItems = OpenEngSBModel.class.isAssignableFrom(item.getClass());
                    }
                    if (modelItems) {
                        OpenEngSBModel oesbModel = (OpenEngSBModel) item;
                        item = convertSubModel(oesbModel, objects, model);
                        property = model.createObjectProperty(OntoConstants.CDL_NAMESPACE + entry.getKey());
                        property.addRange(model.createClass(OntoConstants.CDL_NAMESPACE
                                + oesbModel.getClass().getSimpleName()));
                    }
                    property.addDomain(modelCls);
                    object.addProperty(property, (Resource) item);
                }
            } else if (entry.getType().isArray()) {
                Object[] array = (Object[]) entry.getValue();
                if (array == null || array.length == 0) {
                    continue;
                }
                Boolean modelItems = null;
                for (int i = 0; i < array.length; i++) {
                    Object item = array[i];
                    if (modelItems == null) {
                        modelItems = OpenEngSBModel.class.isAssignableFrom(item.getClass());
                    }
                    if (modelItems) {
                        item = convertSubModel((OpenEngSBModel) item, objects, model);
                    }

                    DatatypeProperty datatypeProp = model.createDatatypeProperty(OntoConstants.CDL_NAMESPACE
                            + entry.getKey());
                    datatypeProp.addDomain(modelCls);

                    object.addProperty(datatypeProp, (Resource) item);
                }
            } else if (Map.class.isAssignableFrom(entry.getType())) {
                Map<?, ?> map = (Map<?, ?>) entry.getValue();
                if (map == null || map.size() == 0) {
                    continue;
                }
                Boolean keyIsModel = null;
                Boolean valueIsModel = null;

                for (Map.Entry<?, ?> ent : map.entrySet()) {
                    if (keyIsModel == null) {
                        keyIsModel = OpenEngSBModel.class.isAssignableFrom(ent.getKey().getClass());
                    }
                    if (valueIsModel == null) {
                        valueIsModel = OpenEngSBModel.class.isAssignableFrom(ent.getValue().getClass());
                    }
                    Object key = ent.getKey();
                    Object value = ent.getValue();
                    if (keyIsModel) {
                        key = convertSubModel((OpenEngSBModel) key, objects, model);
                    }
                    if (valueIsModel) {
                        value = convertSubModel((OpenEngSBModel) value, objects, model);
                    }
                    object.addProperty(model.createProperty(OntoConstants.CDL_NAMESPACE + entry.getKey()),
                            (Resource) key);
                    object.addProperty(model.createProperty(OntoConstants.CDL_NAMESPACE + entry.getKey()),
                            (Resource) value);
                }
            } else {
                // TODO: What type should be handled here?
                DatatypeProperty datatypeProp = model.createDatatypeProperty(OntoConstants.CDL_NAMESPACE
                        + entry.getKey());
                datatypeProp.addDomain(modelCls);

                if (entry.getType().equals(Integer.class)) {
                    datatypeProp.addRange(XSD.xint);
                    object.addLiteral(model.createProperty(OntoConstants.CDL_NAMESPACE + entry.getKey()),
                            entry.getValue());

                } else { // String
                    datatypeProp.addRange(XSD.xstring);
                    object.addProperty(datatypeProp, entry.getValue().toString());
                }
            }
        }

        OntClass modelInfoCls = model.createClass(OntoConstants.CDL_INFO_MODEL);
        Individual modelInfo = model.createIndividual(OntoConstants.CDL_INFO_MODEL + "_"
                + instance.getClass().getSimpleName(), modelInfoCls);

        AnnotationProperty annoProp = model.createAnnotationProperty(OntoConstants.CDL_HAS_INFO_MODEL);
        annoProp.setRange(modelInfoCls);
        modelCls.addProperty(annoProp, modelInfo);

        DatatypeProperty modelTypeProp = model.createDatatypeProperty(OntoConstants.CDL_NAMESPACE
                + OntoConstants.MODEL_TYPE);
        modelTypeProp.addDomain(modelInfoCls);
        modelTypeProp.addRange(XSD.xstring);
        modelInfo.addProperty(modelTypeProp, instance.retrieveModelName());

        DatatypeProperty modelVersionProp = model.createDatatypeProperty(OntoConstants.CDL_NAMESPACE
                + OntoConstants.MODEL_TYPE_VERSION);
        modelVersionProp.addDomain(modelInfoCls);
        modelVersionProp.addRange(XSD.xstring);
        modelInfo.addProperty(modelVersionProp, instance.retrieveModelVersion());

        objects.add(object);

        return object;
    }
}
