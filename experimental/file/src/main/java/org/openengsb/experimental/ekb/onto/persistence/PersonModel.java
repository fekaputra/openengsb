package org.openengsb.experimental.ekb.onto.persistence;

import java.util.Vector;

import org.openengsb.core.api.model.annotation.Model;
import org.openengsb.core.api.model.annotation.OpenEngSBForeignKey;

@Model
public class PersonModel {
	protected String name;
	protected Vector<String> hasEmail;

    @OpenEngSBForeignKey(modelType = ProjectModel.class, modelVersion = "")
	protected Vector<String> isParticipantOf;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vector<String> getHasEmail() {
		return hasEmail;
	}

	public void setHasEmail(Vector<String> hasEmail) {
		this.hasEmail = hasEmail;
	}

	public Vector<String> getIsParticipantOf() {
		return isParticipantOf;
	}

	public void setIsParticipantOf(Vector<String> isParticipantOf) {
		this.isParticipantOf = isParticipantOf;
	}
}
