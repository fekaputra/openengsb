package org.openengsb.experimental.ekb.onto.file;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.openengsb.core.api.model.OpenEngSBModel;
import org.openengsb.core.ekb.api.EKBCommit;
import org.openengsb.experimental.ekb.onto.api.ConnectorInformation;
import org.openengsb.experimental.ekb.onto.api.OntoObject;
import org.openengsb.experimental.ekb.onto.api.OntologyService;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

public class OntologyServiceImpl implements OntologyService {
	
	public OntModel model;
	
	public OntologyServiceImpl() {
		model = ModelFactory.createOntologyModel();
	}

	
	public OntologyServiceImpl(String URL) {
		model = ModelFactory.createOntologyModel();
		
		try {
			InputStream in = FileManager.get().open(URL);
			
			if(in==null) throw new IllegalArgumentException("File: '"+URL+"' not found");
			model.read(in, null);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// --
	public List<OntoObject> convertModelToOntoObject(Object model, ConnectorInformation info) {
		List<OntoObject> listObj = new ArrayList<OntoObject>();
		
//		if
		
		return listObj;
	}
	
	// --

	@Override
	public Long OntoCreate(EKBCommit ekbCommit) {
		// TODO Auto-generated method stub
//		ConnectorInformation ci = new ConnectorInformation(ekbCommit);
//		
//		List<OpenEngSBModel> insert = ekbCommit.getInserts();
//		List<OpenEngSBModel> update = ekbCommit.getUpdates();
//		List<OpenEngSBModel> delete = ekbCommit.getDeletes();
		
		return null;
	}

	@Override
	public Long OntoCommit(EKBCommit ekbCommit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object OntoSPARQLQuery(String query) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
