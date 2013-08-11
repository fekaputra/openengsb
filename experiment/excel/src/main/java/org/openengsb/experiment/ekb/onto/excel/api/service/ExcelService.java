package org.openengsb.experiment.ekb.onto.excel.api.service;

import java.io.File;
import java.util.List;

public interface ExcelService {

	public File exportExcelFile();
	public File exportExcelFile(String query);
	public File exportExcelFile(List<QueryFilter> filters);
	
	public boolean importExcelFile(String excelFile, String config);
	
}
