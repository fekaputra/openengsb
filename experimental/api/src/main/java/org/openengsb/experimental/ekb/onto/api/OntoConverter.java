package org.openengsb.experimental.ekb.onto.api;

import java.util.List;

import org.openengsb.core.ekb.api.EKBCommit;

public class OntoConverter {
	
	public void convertEKBcommit(EKBCommit commit) {
		
	}
	
	public List<Object> convertModelToOntoObject(Object model) {
		
		return null;
	}

    /**
     * Gets the information about domain, connector and instance of an EKBCommit object and returns the corresponding
     * ConnectorInformation object.
     */
    public static ConnectorInformation getConnectorInformationOfEKBCommit(EKBCommit commit) {
        String domainId = commit.getDomainId();
        String connectorId = commit.getConnectorId();
        String instanceId = commit.getInstanceId();
        return new ConnectorInformation(domainId, connectorId, instanceId);
    }
}
