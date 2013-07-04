package org.openengsb.experimental.ekb;

import java.lang.annotation.Annotation;
import java.util.List;

import org.openengsb.core.api.model.ModelDescription;
import org.openengsb.core.ekb.api.ModelRegistry;

public class ModelRegistryImpl implements ModelRegistry {

	@Override
	public void registerModel(ModelDescription model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterModel(ModelDescription model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Class<?> loadModel(ModelDescription model)
			throws ClassNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAnnotatedFields(ModelDescription model,
			Class<? extends Annotation> annotationClass)
			throws ClassNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}
