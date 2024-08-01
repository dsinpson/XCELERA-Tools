package framework.data.dynamicValues;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

import framework.dataProviders.ConfigFileReader;
import io.restassured.path.json.JsonPath;

public class DynamicValuesExcel {
	
	private static final ConfigFileReader reader = new ConfigFileReader("configs/config.properties");
	private static JsonPath jsonPath = null;
	
	public DynamicValuesExcel(String excelPath) {
		jsonPath = getJsonPath(excelPath);
	}
	
	public DynamicValuesExcel() {
		jsonPath = getJsonPath();
	}
	
	public String getValue(int executionCount, String params) {
		try {
			String key = params.replace("@", "");
			key = "data[".concat(String.valueOf(executionCount).concat("]").concat(".").concat(key));
			if(jsonPath != null) {
				return jsonPath.getString(key);
			}
			else {
				jsonPath = getJsonPath();
				return getValue(executionCount, params);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String getValue(String params, int indexRow) {
		String key = params.replace("@", "");
		key = "data[".concat(String.valueOf(indexRow).concat("]").concat(".").concat(key));
		return jsonPath.getString(key);
	}
	
	private static JsonPath getJsonPath() {
		try {
			HashMap<String, String> dataMap = new HashMap<>();
			List<List<String>> dataList = new ArrayList<List<String>>();
			FileInputStream file = new FileInputStream(new File(reader.getPropertyByKey("planilhaMassa")));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);

			List<String> header = new ArrayList<>();
			Row headerRow = sheet.getRow(0);
			for (Cell cell : headerRow) {
				header.add(cell.getStringCellValue());
				
			}
			
			for (Row row : sheet) {
				List<String> tempList = new ArrayList<String>();
				for (Cell cell : row) {
					tempList.add(cell.getStringCellValue());
					if(tempList.size() == header.size() && dataList.size() <= sheet.getLastRowNum()) {
						dataList.add(tempList);
					}
				}
			}
			
			JSONObject json = new JSONObject();
			
			for(int n = 0; n < dataList.size(); n++) {
				dataMap.clear();
				for(int i = 0; i < header.size(); i++) {
					dataMap.put(header.get(i), dataList.get(n).get(i));
				}
				json.append("data", dataMap.clone());
			}
			
			JsonPath jsonPath = new JsonPath(json.toString());
			
			file.close();
			workbook.close();
			return jsonPath;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static JsonPath getJsonPath(String excelPath) {
		try {
			HashMap<String, String> dataMap = new HashMap<>();
			List<List<String>> newList = new ArrayList<List<String>>();
			FileInputStream file = new FileInputStream(new File(excelPath));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);

			List<String> header = new ArrayList<>();
			Row headerRow = sheet.getRow(0);
			for (Cell cell : headerRow) {
				header.add(cell.getStringCellValue());
			}
			
			for (Row row : sheet) {
				List<String> tempList = new ArrayList<String>();
				for (Cell cell : row) {
					tempList.add(cell.getStringCellValue());
					if(tempList.size() == header.size() && newList.size() <= sheet.getLastRowNum()) {
						newList.add(tempList);
					}
				}
			}
			
			JSONObject json = new JSONObject();
			
			for(int n = 0; n < newList.size(); n++) {
				dataMap.clear();
				for(int i = 0; i < header.size(); i++) {
					dataMap.put(header.get(i), newList.get(n).get(i));
				}
				
				json.append("data", dataMap.clone());
			}
			
			JsonPath jsonPath = new JsonPath(json.toString());
			
			file.close();
			workbook.close();
			
			return jsonPath;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
