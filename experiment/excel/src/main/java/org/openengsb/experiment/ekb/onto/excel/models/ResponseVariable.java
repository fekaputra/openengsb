package org.openengsb.experiment.ekb.onto.excel.models;

import java.util.List;

import org.openengsb.core.api.model.annotation.Model;
import org.openengsb.core.api.model.annotation.OpenEngSBForeignKey;
import org.openengsb.core.api.model.annotation.OpenEngSBModelId;

@Model
public class ResponseVariable {
	@OpenEngSBModelId
	protected String responseVariableID;
	protected List<String> responseVariableText;
	@OpenEngSBForeignKey(modelType = Experiment.class, modelVersion="")
	protected List<Experiment> hasResponseVariableExperiment;
	@OpenEngSBForeignKey(modelType = Hypothesis.class, modelVersion="")
	protected List<Experiment> hasResponseVariableHypothesis;
	
	public String get_id() {
		return responseVariableID;
	}
	public void set_id(String _id) {
		this.responseVariableID = _id;
	}
	public List<String> get_text() {
		return responseVariableText;
	}
	public void set_text(List<String> _text) {
		this.responseVariableText = _text;
	}
	public List<Experiment> getHasExperiment() {
		return hasResponseVariableExperiment;
	}
	public void setHasExperiment(List<Experiment> hasExperiment) {
		this.hasResponseVariableExperiment = hasExperiment;
	}
	public List<Experiment> getHasHypothesis() {
		return hasResponseVariableHypothesis;
	}
	public void setHasHypothesis(List<Experiment> hasHypothesis) {
		this.hasResponseVariableHypothesis = hasHypothesis;
	}
}
