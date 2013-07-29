package org.openengsb.experimental.ekb;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;
import org.openengsb.core.api.model.OpenEngSBModel;
import org.openengsb.core.ekb.api.EKBCommit;
import org.openengsb.experimental.ekb.onto.api.OntologyService;
import org.openengsb.experimental.ekb.onto.file.OntologyServiceImpl;
import org.openengsb.experimental.ekb.onto.persistence.Client;
import org.openengsb.experimental.ekb.onto.persistence.ProjectModel;

public class ModelTest {

	@Test
	public void testModel1() {
		ProjectModel model = new ProjectModel();
        assertThat("TestModel isn't enhanced. Maybe you forgot to set the java agent?",
            model instanceof OpenEngSBModel, is(true));
	}

	@Test
	public void testOntoSpaghetti() {
		OntologyService impl = new OntologyServiceImpl("pm-onto.owl");
		
        EKBCommit commit = OntologyServiceImpl.generateTestCase();
//		commit = new EKBCommit();
//        commit.setDomainId("testdomain").setConnectorId("testconnector").setInstanceId("testinstance");
//        commit.addInsert(new ClientModel());
        
        impl.OntoCommit(commit);
	}

}
