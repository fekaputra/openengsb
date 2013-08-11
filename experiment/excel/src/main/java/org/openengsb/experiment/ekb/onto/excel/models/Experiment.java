package org.openengsb.experiment.ekb.onto.excel.models;

import java.util.List;

import org.openengsb.core.api.model.annotation.Model;
import org.openengsb.core.api.model.annotation.OpenEngSBModelId;

@Model
public class Experiment {
    @OpenEngSBModelId
	protected String experimentID;
    protected List<String> experimentObjective;
    protected List<String> experimentDesignType;
    
	public String get_id() {
		return experimentID;
	}
	public void set_id(String _id) {
		this.experimentID = _id;
	}
	public List<String> getExperimentObjective() {
		return experimentObjective;
	}
	public void setExperimentObjective(List<String> objective) {
		this.experimentObjective = objective;
	}
	public List<String> getExperimentDesignType() {
		return experimentDesignType;
	}
	public void setExperimentDesignType(List<String> experimentDesignType) {
		this.experimentDesignType = experimentDesignType;
	}
}
