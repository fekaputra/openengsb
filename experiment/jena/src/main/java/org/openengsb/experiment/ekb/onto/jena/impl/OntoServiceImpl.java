package org.openengsb.experiment.ekb.onto.jena.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.openengsb.core.api.model.OpenEngSBModel;
import org.openengsb.core.ekb.api.EKBCommit;
import org.openengsb.experiment.ekb.onto.api.service.OntoObject;
import org.openengsb.experiment.ekb.onto.api.service.OntoService;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Selector;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;

public class OntoServiceImpl implements OntoService {
	
	OntModel model;
	
	public OntoServiceImpl(String StorageType, 
			String StorageName, 
			String InferenceType) {
		model = ModelFactory.createOntologyModel();
	}
	
	public OntoServiceImpl(String URL) {
		model = ModelFactory.createOntologyModel();
		try {
			InputStream in = FileManager.get().open(URL);
			if(in==null) throw new IllegalArgumentException("File: '"+URL+"' not found");
			model.read(in, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Long OntoCommit(EKBCommit ekbCommit) {
		
		List<OpenEngSBModel> insert = ekbCommit.getInserts();
		List<OpenEngSBModel> update = ekbCommit.getUpdates();
		List<OpenEngSBModel> delete = ekbCommit.getDeletes();
		
		return null;
	}
	
	

	@Override
	public List<OntoObject> GetClassInstances(String conceptName) {
		List<OntoObject> retVal = new ArrayList<OntoObject>();
		
		String classURI = getDefaultNS()+conceptName;
		OntClass ontClass = model.getOntClass(classURI);
		
		Iterator<OntProperty> classProperties;
		
		if(ontClass!=null) {
			Iterator<? extends OntResource> classInstances = ontClass.listInstances();
			while(classInstances.hasNext()) {
				classProperties = ontClass.listDeclaredProperties();
				OntResource classInstance = classInstances.next();
				OntoObject ontoObject = new OntoObject(classInstance.getLocalName());
				
				while(classProperties.hasNext()) {
					OntProperty classProperty = classProperties.next();
					Selector propertySelector = new SimpleSelector(classInstance, classProperty, (RDFNode) null);
					StmtIterator state = model.listStatements(propertySelector);
					
					while(state.hasNext()) {
						ontoObject.add(state.next());
					}
				}
				
				if(!ontoObject.isEmpty()) {
					retVal.add(ontoObject);
				}
			}
		}
		
		return retVal;
	}
	
	private String getDefaultNS() {
		return model.getNsPrefixURI("");
	}

	@Override
	public List<Object> SelectQuery(String query) {
		List<Object> retVal = new ArrayList<Object>();
		Map<String,String> retValEntry;
		
		Query q = QueryFactory.create(query);
		QueryExecution qExe = QueryExecutionFactory.create(q);
		ResultSet results = qExe.execSelect();
		List<String> vars = results.getResultVars();
		
		while(results.hasNext()) {
			QuerySolution row = results.next();
			retValEntry = new HashMap<String, String>();
			for (String var : vars) {
				retValEntry.put(var, row.get(var).toString());
			}
		}
		
		return retVal;
	}

	@Override
	public Map<String, String> getNameSpaces() {
		return model.getNsPrefixMap();
	}

	@Override
	public void addNamespace(String key, String value) {
		model.setNsPrefix(key, value);
	}
	
	public static void main(String[] args) {
		OntoService os = new OntoServiceImpl("smallonto.owl");
		List<OntoObject> obj = os.GetClassInstances("ResponseVariable");
		for (OntoObject ontoObject : obj) {
			System.out.println(ontoObject.toString());
		}
		
//		System.out.println("--");
//		os.GetClassInstances("Experiment");
//		System.out.println("--");
//		os.GetClassInstances("Hypothesis");
//		
//		OntModel model = ModelFactory.createOntologyModel();
//		
//		try {
//			InputStream in = FileManager.get().open("smallonto.owl");
//			
//			if(in==null) throw new IllegalArgumentException("File: not found");
//			model.read(in, null);
//
//			Iterator<OntProperty> iterOntProps = model.listOntProperties();
//			while(iterOntProps.hasNext()) {
//				System.out.println(iterOntProps.next().getURI());
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		Statement s = model.getProperty(model.getResource("http://www.cdl.ifs.tuwien.ac.at/emse_inspection.owl#ISPIS_Planning"), 
//				model.getProperty("http://www.cdl.ifs.tuwien.ac.at/emse_inspection.owl#experimentID"));
//		
//		System.out.println(s.toString());
//		System.out.println("--");
//		Resource r = s.createReifiedStatement();
//		System.out.println(r.isAnon());
//		
//		model.add(s);
	}
}
