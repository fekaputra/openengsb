package org.openengsb.experiment.ekb.onto.excel.poi.internal.test;

import org.junit.Test;
import org.openengsb.experiment.ekb.onto.api.service.OntoService;
import org.openengsb.experiment.ekb.onto.excel.api.service.ExcelService;
import org.openengsb.experiment.ekb.onto.excel.poi.internal.ExcelServiceImpl;
import org.openengsb.experiment.ekb.onto.jena.impl.OntoServiceImpl;

public class ExcelServiceImplTest {

	@Test
	public void testWriteToOnto() {
		OntoService ontoService = new OntoServiceImpl("smallonto.owl");
		ExcelService excelService = new ExcelServiceImpl(ontoService);
		excelService.importExcelFile("small_trial.xlsx", null);
		
		assert(true);
	}

}
