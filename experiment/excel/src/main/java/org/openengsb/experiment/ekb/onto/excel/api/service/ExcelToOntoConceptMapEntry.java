package org.openengsb.experiment.ekb.onto.excel.api.service;

public class ExcelToOntoConceptMapEntry {
	private String excelColumnName;
	private String ontoProperty;
	private String key;
	private String datatype;
	
	public ExcelToOntoConceptMapEntry(String columnName, String ontoProperty, String key, String datatype) {
		this.excelColumnName = columnName;
		this.ontoProperty = ontoProperty;
		this.key = key;
		this.datatype = datatype;
	}
	
	public String getExcelColumnName() {
		return excelColumnName;
	}
	public void setExcelColumnName(String excelColumnName) {
		this.excelColumnName = excelColumnName;
	}
	public String getOntoProperty() {
		return ontoProperty;
	}
	public void setOntoProperty(String ontoProperty) {
		this.ontoProperty = ontoProperty;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
}
