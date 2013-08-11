package org.openengsb.experimental.ekb.onto.file;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.Vector;

import org.openengsb.core.api.context.ContextHolder;
import org.openengsb.core.api.model.FileWrapper;
import org.openengsb.core.api.model.OpenEngSBModel;
import org.openengsb.core.api.model.OpenEngSBModelEntry;
import org.openengsb.core.ekb.api.EKBCommit;
import org.openengsb.core.ekb.api.ModelRegistry;
import org.openengsb.experimental.ekb.onto.api.ConnectorInformation;
import org.openengsb.experimental.ekb.onto.api.OntoObjectEntry;
import org.openengsb.experimental.ekb.onto.api.OntologyService;
import org.openengsb.experimental.ekb.onto.persistence.Client;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDF;

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
			
//			for(Map.Entry<String, String> entry : model.getNsPrefixMap().entrySet()) {
//				System.out.println(entry.getKey() + "/" + entry.getValue());
//			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// --
	public List<OntoObjectEntry> convertModelToOntoObject(Object model, ConnectorInformation info) {
		List<OntoObjectEntry> listObj = new ArrayList<OntoObjectEntry>();
		
		if(!OpenEngSBModel.class.isAssignableFrom(model.getClass())) {
			throw new IllegalArgumentException("the model is unassignable to the OpenEngSBModel");
		}
		if(model != null) {
			convertModelToTriples((OpenEngSBModel)model, info, listObj);
		}
		
		return listObj;
	}
	
	private String convertModelToTriples(OpenEngSBModel model, ConnectorInformation info, List<OntoObjectEntry> listObj) {
        String contextId = ContextHolder.get().getCurrentContextId();
        String URI = createOID(model, contextId); // model instance URI
        listObj.add(new OntoObjectEntry(URI, RDF.type.getURI(), model.getClass().getSimpleName()));
        
		for (OpenEngSBModelEntry entry : model.toOpenEngSBModelEntries()) {
			if(entry.getValue() == null) {
				// null
				continue;
				
			} else if(entry.getType().equals(FileWrapper.class)) {
				// ignore for now
				continue; 
				
			} else if(OpenEngSBModel.class.isAssignableFrom(entry.getType())) { // if objectProperty
				// Functional ObjectProperty
				OpenEngSBModel temp = (OpenEngSBModel) entry.getValue();
				String subURI = convertModelToTriples(temp, info, listObj);
				listObj.add(new OntoObjectEntry(URI, entry.getKey(), subURI));
				
			} else if(List.class.isAssignableFrom(entry.getValue().getClass())) {
				// Non-Functional property
                List<?> list = (List<?>) entry.getValue();
                if (list == null || list.size() == 0) {
                    continue;
                }
                for (int i = 0; i < list.size(); i++) {
                    Object item = list.get(i);
                    if(OpenEngSBModel.class.isAssignableFrom(item.getClass())) {
                    	// Non-Functional Object Property
                    	String subURI = convertModelToTriples((OpenEngSBModel) item, info, listObj);
        				listObj.add(new OntoObjectEntry(URI, entry.getKey(), subURI));
                    } else {
                    	// Non-Functional Datatype Property
//                        System.out.println("ITEM: "+item.toString());
                    	listObj.add(new OntoObjectEntry(URI, entry.getKey(),item.toString()));
                    }
                }
				
			} else {
//    			System.out.println("HOIIII: "+entry.getClass()+":: "+entry.toString());
				// functional DataTypeProperty
            	listObj.add(new OntoObjectEntry(URI, entry.getKey(), entry.getValue().toString()));
            	
			}
		}
		
		return URI;
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

    /**
     * Creates the OID for a model
     */
    public static String createOID(OpenEngSBModel model, String contextId) {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        builder.append(model.getClass().getSimpleName()).append("_").append(Math.abs(random.nextInt()));
//        Object modelId = model.retrieveInternalModelId();
//        if (modelId != null) {
//            builder.append(modelId.toString());
//        } else {
//            builder.append(UUID.randomUUID().toString());
//        }

        return builder.toString();
    }

	@Override
	public Long OntoCommit(EKBCommit ekbCommit) {
		// TODO Auto-generated method stub
		ConnectorInformation ci = new ConnectorInformation(ekbCommit);
		
		List<OpenEngSBModel> insert = ekbCommit.getInserts();
		List<OpenEngSBModel> update = ekbCommit.getUpdates();
		List<OpenEngSBModel> delete = ekbCommit.getDeletes();
		
		List<OntoObjectEntry> fullObj = new ArrayList<OntoObjectEntry>();
		
		fullObj.addAll(ConvertToOnto(insert, ci));
		fullObj.addAll(ConvertToOnto(update, ci));
		fullObj.addAll(ConvertToOnto(delete, ci));
		
//		for(OntoObjectEntry entry : fullObj) {
//			System.out.println("::"+entry.getSubject()+"<>"+entry.getPredicate()+"<>"+entry.getObject()+"::");
//		}
		
		insertIntoOnto(fullObj);
		save("test2.owl");
		
		return null;
	}
	
	public void insertIntoOnto(List<OntoObjectEntry> all) {
		String NS = model.getNsPrefixURI("");
		
		for (OntoObjectEntry triple : all) {
			
			Resource subject = ResourceFactory.createResource(NS+triple.getSubject());
			Property predicate;
			RDFNode object;
			if(triple.getPredicate().equalsIgnoreCase(RDF.type.toString())) {
				predicate = ResourceFactory.createProperty(triple.getPredicate());
				object = ResourceFactory.createResource(NS+triple.getObject());
			} else {
				predicate = ResourceFactory.createProperty(NS+triple.getPredicate());
				object = ResourceFactory.createPlainLiteral(triple.getObject());
			}
			Statement s = ResourceFactory.createStatement(subject, predicate, object);
			
			model.add(s);
		}
	}
	
	public List<OntoObjectEntry> ConvertToOnto(List<OpenEngSBModel> listObj, ConnectorInformation ci) {
		List<OntoObjectEntry> oo = new ArrayList<OntoObjectEntry>();
		
		if(listObj!=null) {
			for (OpenEngSBModel model : listObj) {
				oo.addAll(convertModelToOntoObject(model, ci));
			}
		}
		
		return oo;
	}

	@Override
	public Object OntoSPARQLQuery(String query) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static EKBCommit generateTestCase() {
		EKBCommit commit = new EKBCommit();
		
		for(int i=1; i<10; i++) {
			Client client = new Client();
			Vector<String> email = new Vector<String>();
			email.add("email"+i);
			client.setName("name"+i);
			client.setHasEmail(email);
			
			commit.addInsert(client);
		}
		
		for(OpenEngSBModel insert : commit.getInserts()) {
//			System.out.println(insert.toString());
			for(OpenEngSBModelEntry entry : insert.toOpenEngSBModelEntries()) {
				//
			}
		}
		
		return commit;
	}
	
	public void save(String targetFile) {
		String def = "model-2.owl";
		if(targetFile != null) def = targetFile;
			
//		model.write(System.out);
		
		try {
			model.write(new FileWriter(def), "RDF/XML");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
