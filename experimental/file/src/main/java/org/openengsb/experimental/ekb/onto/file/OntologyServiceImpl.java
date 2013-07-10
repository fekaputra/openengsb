package org.openengsb.experimental.ekb.onto.file;

import org.openengsb.core.ekb.api.EKBCommit;
import org.openengsb.experimental.ekb.onto.api.OntologyService;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class OntologyServiceImpl implements OntologyService {
	
	Model jenaModel;
	
	public OntologyServiceImpl() {
		jenaModel = ModelFactory.createDefaultModel();
	}

	@Override
	public Long OntoCreate(EKBCommit ekbCommit) {
		// TODO Auto-generated method stub
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
