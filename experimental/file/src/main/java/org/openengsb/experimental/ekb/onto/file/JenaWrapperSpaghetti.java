package org.openengsb.experimental.ekb.onto.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang.time.StopWatch;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.hp.hpl.jena.ontology.FunctionalProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.update.UpdateAction;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateRequest;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;

public class JenaWrapperSpaghetti {
	
	String NS;
	OntModel model = null;

	public JenaWrapperSpaghetti() {
		model = ModelFactory.createOntologyModel();
	}
	
	public JenaWrapperSpaghetti(String URL) {
		model = ModelFactory.createOntologyModel();
		
		try {
			InputStream in = FileManager.get().open(URL);
			
			if(in==null) throw new IllegalArgumentException("File: '"+URL+"' not found");
			model.read(in, null);
			
//			Map<String,String> tes = model.getNsPrefixMap();
//			System.out.println(tes.toString());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// create owl resource - based on owl file
	public void importOWLFile(String URL, String new_name) {
		try {
			InputStream in = FileManager.get().open(URL);
			
			if(in==null) throw new IllegalArgumentException("File: '"+URL+"' not found");
			model.read(in, "http://cdl.tuwien.ac.at/"+new_name+"/");
			
			OutputStream out = new FileOutputStream(new File(new_name));
			model.write(out);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// import xls -- instances
	public void importXLSFile(String URL, String targetOWL, Vector<Object> transformList) {
		String xlsFile = "people.xls";
		String prefix =  "http://www.cdl.ifs.tuwien.ac.at/pm.owl#";
		
		try {
			InputStream inp = new FileInputStream(xlsFile);
		    Workbook wb = WorkbookFactory.create(inp);
		    Sheet sh = wb.getSheetAt(0);
		    
		    for(int i=1; i<5; i++) {
		    	Row row = sh.getRow(i);
		    	Cell c0 = row.getCell(0);
		    	Cell c1 = row.getCell(1);
		    	Cell c2 = row.getCell(2);
		    	
		    	Individual person = model.createIndividual(prefix+c0, model.getOntClass(prefix+c1));
		    	person.addProperty(model.getProperty(prefix+"hasEmail"), c2.getStringCellValue());
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// import mapping -- Better w/ openEngSB
	public void importMappingFile(Vector<Object> transformList) {
		
	}
	
	// select Query -- get result
	public HashMap<String, Object> selectQuery(String SPARQLQuery, String OWLFile) {

		String prefix =  "PREFIX cdl: <http://www.cdl.ifs.tuwien.ac.at/pm.owl#> ";
		prefix += "PREFIX rdf: <"+model.getNsPrefixURI("rdf")+"> ";
		String defaultQuery = prefix+" select * where { ?S rdf:type cdl:Person }";
		if(SPARQLQuery != null) defaultQuery = SPARQLQuery; 
		Query q = QueryFactory.create(defaultQuery);
		QueryExecution qE = QueryExecutionFactory.create(q,model);
		
		try {
			ResultSet rs = qE.execSelect();
			
			while(rs.hasNext()) {
				QuerySolution soln = rs.nextSolution();
				RDFNode S = soln.get("S");
//				RDFNode O = soln.get("O");
//				s.visitWith(new RDFVisitor() {
//					
//					@Override
//					public Object visitURI(Resource r, String uri) {
//						// TODO Auto-generated method stub
//						return null;
//					}
//					
//					@Override
//					public Object visitLiteral(Literal l) {
//						// TODO Auto-generated method stub
//						return null;
//					}
//					
//					@Override
//					public Object visitBlank(Resource r, AnonId id) {
//						// TODO Auto-generated method stub
//						return null;
//					}
//					
//				});
//				RDFNode p = soln.get("P");
//				RDFNode o = soln.get("O");

				System.out.println(S.toString());
//				System.out.println(p.toString());
//				System.out.println(O.toString());
				System.out.println("-----");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			qE.close();
		}
		
		return null;
	}
	
	public void truncateOWL() {
		
	}
	
	public Vector<String> listAllOwlClasses() {
		Vector<String> listClasses = new Vector<String>();
		Iterator<OntClass> i = model.listClasses();
		while(i.hasNext()) {
			OntClass oc = i.next();
			System.out.println(oc.getURI());
		}
		
		return listClasses;
	}
		
	public Vector<String> listAllOwlProperties() {
		Vector<String> listProps = new Vector<String>();
		Iterator<OntProperty> i = model.listAllOntProperties();
		while(i.hasNext()) {
			OntProperty oc = i.next();
			System.out.println(oc.getURI());
		}
		
		return listProps;
	}

	// insert/update Query -- get result
	public boolean insertQuery(String SPARQLQuery, String OWLFile) {
		String prefix = 	"PREFIX cdl: <http://www.cdl.ifs.tuwien.ac.at/pm.owl#> "+ 
							"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
							"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>";
		StopWatch sw = new StopWatch();
		UpdateRequest request = UpdateFactory.create();
		
		for(int i=0;i<100000;i++) {
			String defUpdateQuery = "insert data " +
					"{ " +
					"	cdl:Richard_Mordinyi_"+i+" rdf:type cdl:Person; " +
					" 		rdfs:label \"Richard Mordinyi\" . " +
					"}";

			String uq = prefix+defUpdateQuery;
			
			request.add(uq);
		}
		sw.start();
		UpdateAction.execute(request, model);
		save(OWLFile);
		sw.stop();
		System.out.println("TIME: "+sw.getTime());
		
		return true;
	}
	
	public void save(String targetFile) {
		String def = "model-2.owl";
		if(targetFile != null) def = targetFile;
		
		try {
			model.write(new FileWriter(def), "RDF/XML");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setNonFunctional() {
		Iterator<OntProperty> ont = model.listOntProperties();
		while(ont.hasNext()) {
			OntProperty p = ont.next();
			if(p.isFunctionalProperty()) {
				p.removeProperty(RDF.type, OWL.FunctionalProperty);
			}
		}
	}
	
	public void showAllPropertys() {
		Iterator<OntProperty> ont = model.listOntProperties();
		while(ont.hasNext()) {
			OntProperty p = ont.next();
			if(p.isObjectProperty()) {
//				System.out.println("object: "+p.getLocalName());
			} else if(p.isDatatypeProperty()) {
//				System.out.println("datatype: "+p.getLocalName());
			} else {
				System.out.println("else: "+p.getLocalName());
			}
		}
	}
	
	public void testConstruct() {
		
	}
	
	public static void main(String[] args) {
//		String prefix = 	
//				"PREFIX cdl: <http://www.cdl.ifs.tuwien.ac.at/pm.owl#> "+ 
//				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
//				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> ";
//		prefix+= "select ?s where { ?s rdf:type cdl:Person }";
//		JenaWrapper jw = new JenaWrapper();
//		jw.importOWLFile("pm-onto.owl", "model-2.owl");
//		jw.importXLSFile(null, null, null);
//		jw.insertQuery(null, null);
//		jw.save();
//		jw.listAllOwlClasses();
//		jw.listAllOwlProperties();
		JenaWrapperSpaghetti jw = new JenaWrapperSpaghetti("pm-onto.owl");
		jw.showAllPropertys();
//		jw.setNonFunctional();
//		jw.save("gate_control2.owl");
//		jw.insertQuery(null, null);
//		jw.selectQuery(null, null);
		
	}
}
