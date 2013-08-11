package org.openengsb.experiment.ekb.onto.jena.test.models;

import java.util.List;

import org.openengsb.core.api.model.annotation.Model;
import org.openengsb.core.api.model.annotation.OpenEngSBForeignKey;
import org.openengsb.core.api.model.annotation.OpenEngSBModelId;

@Model
public class Hypothesis {
	@OpenEngSBModelId
	protected String hypothesisID;
	protected List<String> hypothesisText;
	@OpenEngSBForeignKey(modelType = Experiment.class, modelVersion = "")
	protected List<Experiment> hasHypothesisExperiment;
	
	public String get_id() {
		return hypothesisID;
	}
	public void set_id(String _id) {
		this.hypothesisID = _id;
	}
	public List<String> get_text() {
		return hypothesisText;
	}
	public void set_text(List<String> _text) {
		this.hypothesisText = _text;
	}
	public List<Experiment> getHasExperiment() {
		return hasHypothesisExperiment;
	}
	public void setHasExperiment(List<Experiment> hasExperiment) {
		this.hasHypothesisExperiment = hasExperiment;
	}
	
	
}
