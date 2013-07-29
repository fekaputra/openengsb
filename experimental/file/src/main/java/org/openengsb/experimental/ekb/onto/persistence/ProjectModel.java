package org.openengsb.experimental.ekb.onto.persistence;

import java.util.Date;
import java.util.Vector;

import org.openengsb.core.api.model.annotation.Model;
import org.openengsb.core.api.model.annotation.OpenEngSBForeignKey;

@Model
public class ProjectModel {
	protected Vector<Date> hasStartDate; 
	protected Vector<Date> hasEndDate;

    @OpenEngSBForeignKey(modelType = PersonModel.class, modelVersion = "")
	protected Vector<String> hasParticipant;
    
    @OpenEngSBForeignKey(modelType = PersonModel.class, modelVersion = "")
	protected Vector<String> hasProjectLeader;
    
    @OpenEngSBForeignKey(modelType = PersonModel.class, modelVersion = "")
	protected Vector<String> hasProjectOwner;
	protected String hasPhase;
}
