package org.openengsb.experimental.ekb.onto.persistence;

import java.util.Vector;

import org.openengsb.core.api.model.annotation.Model;
import org.openengsb.core.api.model.annotation.OpenEngSBForeignKey;

@Model
public class EngineerModel extends PersonModel {
    @OpenEngSBForeignKey(modelType = ProjectModel.class, modelVersion = "")
	protected Vector<ProjectModel> isProjectLeaderOf;

	public Vector<ProjectModel> getIsProjectLeaderOf() {
		return isProjectLeaderOf;
	}

	public void setIsProjectLeaderOf(Vector<ProjectModel> isProjectLeaderOf) {
		this.isProjectLeaderOf = isProjectLeaderOf;
	}
}
