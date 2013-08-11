package org.openengsb.experiment.ekb.onto.jena.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;
import org.openengsb.core.api.model.OpenEngSBModel;
import org.openengsb.experiment.ekb.onto.jena.test.models.Experiment;
import org.openengsb.experiment.ekb.onto.jena.test.models.Hypothesis;
import org.openengsb.experiment.ekb.onto.jena.test.models.ResponseVariable;

public class ModelTest {

	@Test
	public void testModels() {
		
		Experiment model1 = new Experiment();
		Hypothesis model2 = new Hypothesis();
		ResponseVariable model3 = new ResponseVariable();
        assertThat("Experiment isn't enhanced. Maybe you forgot to set the java agent?",
        		model1 instanceof OpenEngSBModel, is(true));
        assertThat("Hypothesis isn't enhanced. Maybe you forgot to set the java agent?",
                model2 instanceof OpenEngSBModel, is(true));
        assertThat("ResponseVariable isn't enhanced. Maybe you forgot to set the java agent?",
                model3 instanceof OpenEngSBModel, is(true));
	}

	@Test
	public void testLoadModels() {
		
		
//		OntologyService impl = new OntologyServiceImpl("pm-onto.owl");
//		
//        EKBCommit commit = OntologyServiceImpl.generateTestCase();
//		commit = new EKBCommit();
//        commit.setDomainId("testdomain").setConnectorId("testconnector").setInstanceId("testinstance");
//        commit.addInsert(new ClientModel());
        
//        impl.OntoCommit(commit);
	}

}
