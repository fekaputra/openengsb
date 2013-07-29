package org.openengsb.experimental.ekb.onto.general;

import java.util.Calendar;
import java.util.List;

import org.openengsb.core.ekb.api.EKBCommit;
import org.openengsb.core.ekb.api.EKBException;
import org.openengsb.core.ekb.api.PersistInterface;
import org.openengsb.core.ekb.api.SanityCheckException;
import org.openengsb.core.ekb.api.SanityCheckReport;
import org.openengsb.core.ekb.api.hooks.EKBPostCommitHook;
import org.openengsb.core.ekb.api.hooks.EKBPreCommitHook;
import org.openengsb.experimental.ekb.onto.api.OntologyService;
import org.openengsb.experimental.ekb.onto.file.OntologyServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Juang
 */
public class PersistInterfaceImpl implements PersistInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersistInterfaceImpl.class);

    private OntologyService ontoService;
    private List<EKBPreCommitHook> preCommitHooks;
    private List<EKBPostCommitHook> postCommitHooks;
	
	public PersistInterfaceImpl() {
		ontoService = new OntologyServiceImpl();
	}

	@Override
	public void commit(EKBCommit commit) throws SanityCheckException,
			EKBException {
		// TODO Auto-generated method stub
		ontoService.OntoCommit(commit);
	}

	@Override
	public void forceCommit(EKBCommit commit) throws EKBException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SanityCheckReport check(EKBCommit commit) throws EKBException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * To be executed
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(Calendar.getInstance().getTimeInMillis());
		
//		EKBCommit commit = new EKBCommit();
//		PersistInterface pi = new PersistInterfaceImpl();
//		pi.commit(commit);
	}
}
