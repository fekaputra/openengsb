package org.openengsb.experimental.ekb.onto.api;

import org.openengsb.core.ekb.api.EKBCommit;

public class ConnectorInformation {
    private String domainId;
    private String connectorId;
    private String instanceId;

    public ConnectorInformation(EKBCommit commit) {
    	this.domainId = commit.getDomainId();
    	this.connectorId = commit.getConnectorId();
    	this.instanceId = commit.getInstanceId();
    }
    
    public ConnectorInformation(String domainId, String connectorId, String instanceId) {
        this.domainId = domainId;
        this.connectorId = connectorId;
        this.instanceId = instanceId;
    }

    public String getDomainId() {
        return domainId;
    }

    public String getConnectorId() {
        return connectorId;
    }

    public String getInstanceId() {
        return instanceId;
    }
}
