package org.openengsb.experimental.ekb.onto.general;

import java.util.List;

import org.openengsb.core.api.model.ModelDescription;
import org.openengsb.core.ekb.api.TransformationEngine;
import org.openengsb.core.ekb.api.transformation.TransformationDescription;

public class TransformationEngineImpl implements TransformationEngine {

	@Override
	public void saveDescription(TransformationDescription description) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveDescriptions(List<TransformationDescription> descriptions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteDescription(TransformationDescription description) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteDescriptionsByFile(String fileName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TransformationDescription> getDescriptionsByFile(String fileName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object performTransformation(ModelDescription sourceModel,
			ModelDescription targetModel, Object source) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object performTransformation(ModelDescription sourceModel,
			ModelDescription targetModel, Object source, Object target) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object performTransformation(ModelDescription sourceModel,
			ModelDescription targetModel, Object source, List<String> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object performTransformation(ModelDescription sourceModel,
			ModelDescription targetModel, Object source, Object target,
			List<String> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isTransformationPossible(ModelDescription sourceModel,
			ModelDescription targetModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isTransformationPossible(ModelDescription sourceModel,
			ModelDescription targetModel, List<String> ids) {
		// TODO Auto-generated method stub
		return null;
	}

}
