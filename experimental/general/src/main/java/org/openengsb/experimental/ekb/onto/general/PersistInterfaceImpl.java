package org.openengsb.experimental.ekb.onto.general;

import java.util.List;

import org.openengsb.core.ekb.api.EKBCommit;
import org.openengsb.core.ekb.api.EKBException;
import org.openengsb.core.ekb.api.PersistInterface;
import org.openengsb.core.ekb.api.SanityCheckException;
import org.openengsb.core.ekb.api.SanityCheckReport;
import org.openengsb.core.ekb.api.hooks.EKBPostCommitHook;
import org.openengsb.core.ekb.api.hooks.EKBPreCommitHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Juang
 */
public class PersistInterfaceImpl implements PersistInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersistInterfaceImpl.class);

    private List<EKBPreCommitHook> preCommitHooks;
    private List<EKBPostCommitHook> postCommitHooks;
	
	public PersistInterfaceImpl() {
		
	}

	@Override
	public void commit(EKBCommit commit) throws SanityCheckException,
			EKBException {
		// TODO Auto-generated method stub
		
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

}
