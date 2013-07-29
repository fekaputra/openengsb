package org.openengsb.experimental.ekb.onto.persistence;

import java.util.Vector;

import org.openengsb.core.api.model.annotation.Model;
import org.openengsb.core.api.model.annotation.OpenEngSBForeignKey;

@Model
public class ClientModel extends PersonModel {
    @OpenEngSBForeignKey(modelType = ProjectModel.class, modelVersion = "")
	protected Vector<String> isProjectOwnerOf;

	public Vector<String> getIsProjectOwnerOf() {
		return isProjectOwnerOf;
	}

	public void setIsProjectOwnerOf(Vector<String> isProjectOwnerOf) {
		this.isProjectOwnerOf = isProjectOwnerOf;
	}
}
