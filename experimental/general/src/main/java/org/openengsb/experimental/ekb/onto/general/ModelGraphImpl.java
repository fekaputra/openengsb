package org.openengsb.experimental.ekb.onto.general;

import java.util.List;

import org.openengsb.core.api.model.ModelDescription;
import org.openengsb.core.ekb.api.ModelGraph;
import org.openengsb.core.ekb.api.transformation.TransformationDescription;

public class ModelGraphImpl implements ModelGraph {

	@Override
	public void addModel(ModelDescription model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeModel(ModelDescription model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTransformation(TransformationDescription description) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeTransformation(TransformationDescription description) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TransformationDescription> getTransformationsPerFileName(
			String filename) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransformationDescription> getTransformationPath(
			ModelDescription source, ModelDescription target, List<String> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isTransformationPossible(ModelDescription source,
			ModelDescription target, List<String> ids) {
		// TODO Auto-generated method stub
		return null;
	}

}
