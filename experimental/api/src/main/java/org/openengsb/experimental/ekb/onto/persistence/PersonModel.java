package org.openengsb.experimental.ekb.onto.persistence;

import java.util.List;

import org.openengsb.core.api.model.annotation.Model;
import org.openengsb.core.api.model.annotation.OpenEngSBForeignKey;

@Model
public class PersonModel {
	protected String name;
	protected List<String> hasEmail;

    @OpenEngSBForeignKey(modelType = ProjectModel.class, modelVersion = "")
	protected List<ProjectModel> isParticipantOf;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getHasEmail() {
		return hasEmail;
	}

	public void setHasEmail(List<String> hasEmail) {
		this.hasEmail = hasEmail;
	}

	public List<ProjectModel> getIsParticipantOf() {
		return isParticipantOf;
	}

	public void setIsParticipantOf(List<ProjectModel> isParticipantOf) {
		this.isParticipantOf = isParticipantOf;
	}
}
