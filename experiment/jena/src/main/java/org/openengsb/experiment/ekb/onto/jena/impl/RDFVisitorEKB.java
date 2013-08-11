package org.openengsb.experiment.ekb.onto.jena.impl;

import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFVisitor;
import com.hp.hpl.jena.rdf.model.Resource;

public class RDFVisitorEKB implements RDFVisitor {

	@Override
	public Object visitBlank(Resource r, AnonId id) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public Object visitURI(Resource r, String uri) {
		// TODO Auto-generated method stub
		return r.getLocalName();
	}

	@Override
	public Object visitLiteral(Literal l) {
		// TODO Auto-generated method stub
		return l.getValue();
	}

}
