package org.openengsb.experimental.ekb;

import org.openengsb.core.ekb.api.EKBCommit;
import org.openengsb.core.ekb.api.EKBException;
import org.openengsb.core.ekb.api.PersistInterface;
import org.openengsb.core.ekb.api.SanityCheckException;
import org.openengsb.core.ekb.api.SanityCheckReport;

public class PersistInterfaceImpl implements PersistInterface {

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
