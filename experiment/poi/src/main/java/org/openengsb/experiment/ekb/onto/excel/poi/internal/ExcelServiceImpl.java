package org.openengsb.experiment.ekb.onto.excel.poi.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openengsb.core.api.model.OpenEngSBModel;
import org.openengsb.experiment.ekb.onto.api.service.OntoService;
import org.openengsb.experiment.ekb.onto.excel.api.service.ExcelService;
import org.openengsb.experiment.ekb.onto.excel.api.service.ExcelToOntoConceptMap;
import org.openengsb.experiment.ekb.onto.excel.api.service.ExcelToOntoConceptMapEntry;
import org.openengsb.experiment.ekb.onto.excel.api.service.QueryFilter;
import org.openengsb.experiment.ekb.onto.excel.models.Experiment;
import org.openengsb.experiment.ekb.onto.excel.models.Hypothesis;

import com.hp.hpl.jena.util.FileManager;

public class ExcelServiceImpl implements ExcelService {
	
	private OntoService ontoService;
	
	public ExcelServiceImpl(OntoService ontoService) {
		this.ontoService = ontoService;
	}
	
	private static XSSFWorkbook readFile(String fileString) throws IOException {
		InputStream in = FileManager.get().open(fileString);
		if(in==null) throw new IllegalArgumentException("File: '"+fileString+"' not found");
		return new XSSFWorkbook(in);
	}

	public File exportExcelFile() {
		// TODO Auto-generated method stub
		return null;
	}

	public File exportExcelFile(String query) {
		// TODO Auto-generated method stub
		return null;
	}

	public File exportExcelFile(List<QueryFilter> filters) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean importExcelFile(String excelFile, String config) {
		try {
			XSSFWorkbook workbook = ExcelServiceImpl.readFile(excelFile);
			Map<String, ExcelToOntoConceptMap> mappingBook = processMappingSheet(workbook.getSheet("Ontology Mapping"));
			
			for (XSSFSheet xssfSheet : workbook) {
				processEachSheet(xssfSheet, mappingBook.get(xssfSheet.getSheetName()));
			}
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	private Map<String, ExcelToOntoConceptMap> processMappingSheet(XSSFSheet sheet) {
		Map<String, ExcelToOntoConceptMap> retVal = new HashMap<String, ExcelToOntoConceptMap>();
		for (Row row: sheet) {
			if(row.getRowNum()==0) continue;			
			
			// OntologyConcept
			String sheetName = row.getCell(0).getStringCellValue();
			String concept = row.getCell(1).getStringCellValue();
			ExcelToOntoConceptMap mapper;
			if(retVal.containsKey(sheetName)) {
				mapper = retVal.get(sheetName);
			} else {
				mapper = new ExcelToOntoConceptMap(sheetName, concept, null);
				retVal.put(sheetName, mapper);
			}
			
			String excelColumn = row.getCell(2).getStringCellValue();
			String ontoProperty = row.getCell(3).getStringCellValue();
			String key = row.getCell(4).getStringCellValue();
			String datatype = row.getCell(5).getStringCellValue();
			
			ExcelToOntoConceptMapEntry entry = new ExcelToOntoConceptMapEntry(excelColumn, ontoProperty, key, datatype);
			mapper.getMappingMap().put(excelColumn, entry);
		}
		
		return retVal;
	}
	
	private List<OpenEngSBModel> processEachSheet(XSSFSheet sheet, ExcelToOntoConceptMap mapper) {
		List<OpenEngSBModel> retVal = new ArrayList<OpenEngSBModel>();
		if(sheet.getSheetName().equalsIgnoreCase("Experiment")) {
			for(Row row: sheet) {
				if(row.getRowNum()==0) continue;
				Experiment experiment = new Experiment();
				experiment.set_id(row.getCell(0).getStringCellValue());
				
				List<String> objectives = new ArrayList<String>();
				objectives.add(row.getCell(3).getStringCellValue());
				experiment.setExperimentObjective(objectives);
				
				List<String> designTypes = new ArrayList<String>();
				designTypes.add(row.getCell(3).getStringCellValue());
				experiment.setExperimentDesignType(designTypes);
				
				retVal.add((OpenEngSBModel) experiment);
			}
		} else if(sheet.getSheetName().equalsIgnoreCase("Hypothesis")) {
			for(Row row: sheet) {
				if(row.getRowNum()==0) continue;
				Hypothesis hypothesis = new Hypothesis();
				hypothesis.set_id(generateKey(sheet.getSheetName()));
				
				List<String> _text = new ArrayList<String>();
				_text.add(row.getCell(1).getStringCellValue());
				hypothesis.set_text(null);
				
				retVal.add((OpenEngSBModel) hypothesis);
			}
		}
		
		return retVal;
	}
	
	private String generateKey(String conceptName) {
		Random rand = new Random(Calendar.getInstance().getTimeInMillis());
		StringBuilder sb = new StringBuilder();
		sb.append(conceptName).append("_").append(rand.nextInt());
		return sb.toString();
	}
	
	
}
