package org.openengsb.experiment.ekb.onto.jena.test.models;

import java.util.List;

import org.openengsb.core.api.model.annotation.Model;
import org.openengsb.core.api.model.annotation.OpenEngSBModelId;

@Model
public class Experiment {
    @OpenEngSBModelId
	protected String experimentID;
    protected List<String> experimentObjective;
    
	public String get_id() {
		return experimentID;
	}
	public void set_id(String _id) {
		this.experimentID = _id;
	}
	public List<String> getObjective() {
		return experimentObjective;
	}
	public void setObjective(List<String> objective) {
		this.experimentObjective = objective;
	}
}
