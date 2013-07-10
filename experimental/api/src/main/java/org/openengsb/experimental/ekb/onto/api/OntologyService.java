package org.openengsb.experimental.ekb.onto.api;

import org.openengsb.core.ekb.api.EKBCommit;

public interface OntologyService {
	/**
	 * Create a new ontology-based repository model 
	 * 
	 * @param ekbCommit list of concept and class for the new ontology
	 * @return timestamp of ontology model creation time
	 */
	public Long OntoCreate(EKBCommit ekbCommit);
	
	/**
	 * Commit all element of EKBCommit into the ontology
	 * 
	 * @param ekbCommit
	 * @return
	 */
	public Long OntoCommit(EKBCommit ekbCommit);
	
	/**
	 * Execution of the query given
	 * 
	 * @param query user's sparql query
	 * @return
	 */
	public Object OntoSPARQLQuery(String query);
}
