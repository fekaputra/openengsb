package org.openengsb.experimental.ekb.onto.persistence;

import java.util.Vector;

import org.openengsb.core.api.model.annotation.Model;
import org.openengsb.core.api.model.annotation.OpenEngSBForeignKey;

@Model
public class EngineerModel extends PersonModel {
    @OpenEngSBForeignKey(modelType = ProjectModel.class, modelVersion = "")
	protected Vector<String> isProjectLeaderOf;

	public Vector<String> getIsProjectLeaderOf() {
		return isProjectLeaderOf;
	}

	public void setIsProjectLeaderOf(Vector<String> isProjectLeaderOf) {
		this.isProjectLeaderOf = isProjectLeaderOf;
	}
}
