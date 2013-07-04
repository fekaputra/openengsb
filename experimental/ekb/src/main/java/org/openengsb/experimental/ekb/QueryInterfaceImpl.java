package org.openengsb.experimental.ekb;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.openengsb.core.ekb.api.QueryInterface;

public class QueryInterfaceImpl implements QueryInterface {

	@Override
	public <T> T getModel(Class<T> model, String oid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> getModelHistory(Class<T> model, String oid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> getModelHistoryForTimeRange(Class<T> model, String oid,
			Long from, Long to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> queryForModelsAtTimestamp(Class<T> model, String query,
			String timestamp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> queryForModels(Class<T> model, String query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> queryForModelsByQueryMapAtTimestamp(Class<T> model,
			Map<String, Object> queryMap, Long timestamp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> queryForModelsByQueryMap(Class<T> model,
			Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> queryForActiveModelsByQueryMap(Class<T> model,
			Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> queryForActiveModels(Class<T> model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UUID getCurrentRevisionNumber() {
		// TODO Auto-generated method stub
		return null;
	}

}
