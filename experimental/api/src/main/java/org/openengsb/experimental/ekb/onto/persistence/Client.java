package org.openengsb.experimental.ekb.onto.persistence;

import java.util.List;

import org.openengsb.core.api.model.annotation.Model;
import org.openengsb.core.api.model.annotation.OpenEngSBForeignKey;

@Model
public class Client extends PersonModel {
    @OpenEngSBForeignKey(modelType = ProjectModel.class, modelVersion = "")
	protected List<ProjectModel> isProjectOwnerOf;

	public List<ProjectModel> getIsProjectOwnerOf() {
		return isProjectOwnerOf;
	}

	public void setIsProjectOwnerOf(List<ProjectModel> isProjectOwnerOf) {
		this.isProjectOwnerOf = isProjectOwnerOf;
	}
}
