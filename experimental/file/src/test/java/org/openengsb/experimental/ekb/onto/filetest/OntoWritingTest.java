package org.openengsb.experimental.ekb.onto.filetest;

import java.io.InputStream;

import org.junit.Test;
import org.openengsb.core.api.model.OpenEngSBModel;
import org.openengsb.core.ekb.api.EKBCommit;
import org.openengsb.experimental.ekb.onto.file.JenaWrapperSpaghetti;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

public class OntoWritingTest {
	
	@Test
	public void test() {
		EKBCommit commit = new EKBCommit(); 
		
		String dataFile = ("pm-onto.owl");
		Model ontoModel = ModelFactory.createDefaultModel();

		try {
			InputStream in = FileManager.get().open(dataFile);
			
			ontoModel.read(in, null);
			
		} catch(Exception e) {
			e.printStackTrace();
		}	
		assert(true);
	}
	
	@Test
	public void jenaWrapperTest() {
		JenaWrapperSpaghetti jw = new JenaWrapperSpaghetti();
		
		jw.importOWLFile("pm-onto.owl", "onto.owl");
		
		assert(true);
	}
}
