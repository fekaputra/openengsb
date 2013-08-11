package org.openengsb.experiment.ekb.onto.excel.api.service;

import java.util.HashMap;
import java.util.Map;

public class ExcelToOntoConceptMap {
	
	public ExcelToOntoConceptMap(String sheetName, String concept, Map<String, ExcelToOntoConceptMapEntry> entries) {
		excelSheetName = sheetName;
		ontoConcept = concept;
		if(entries!=null) {
			mappingMap = entries;
		} else {
			mappingMap = new HashMap<String, ExcelToOntoConceptMapEntry>();
		}
	}

	private String excelSheetName;
	private String ontoConcept;
	
	Map<String, ExcelToOntoConceptMapEntry> mappingMap;
	
	public String getExcelSheetName() {
		return excelSheetName;
	}
	public void setExcelSheetName(String excelSheetName) {
		this.excelSheetName = excelSheetName;
	}
	public String getOntoConcept() {
		return ontoConcept;
	}
	public void setOntoConcept(String ontoConcept) {
		this.ontoConcept = ontoConcept;
	}
	
	public Map<String, ExcelToOntoConceptMapEntry> getMappingMap() {
		return mappingMap;
	}
}
