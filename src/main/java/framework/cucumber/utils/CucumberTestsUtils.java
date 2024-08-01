package framework.cucumber.utils;

import java.util.HashMap;
import java.util.Map;

import framework.data.dynamicValues.DynamicValuesHelper;

public class CucumberTestsUtils {

	private final Map<String, String> _executionSettings;
	
	public CucumberTestsUtils(Map<String, String> executionSettings) {
		this._executionSettings = executionSettings;
	}
	
	public String getValueFromAttribute(String attribute) throws Exception {
		
		String attributeValue = attribute.substring(attribute.indexOf("=") + 1).trim();
		
		DynamicValuesHelper dynamicValuesHelper = new DynamicValuesHelper();		
		return dynamicValuesHelper.changeDynamicValueData(attributeValue, _executionSettings);
	}
	
	public String getAttributeNameFromAttribute(String attribute) {
		return attribute.substring(0, attribute.indexOf("=")).trim();
	}
	
	public Map<String, String> convertToDictionaryOfAttribute(String attributesAndValues) {
		
		Map<String, String> attributes = new HashMap<String, String>();
		
		if(attributesAndValues.contains(";")) {
			String[] attributePlusValue = attributesAndValues.split(";");
		
			for (String attributeAndValue : attributePlusValue) {
				String[] values = attributeAndValue.split("=");

				try {
					attributes.put(values[0], values[1]);
				}
				catch (IndexOutOfBoundsException e) {
					attributes.put(values[0], "");
				}				
			}
			
		} else {
			String[] values = attributesAndValues.split("=");
			attributes.put(values[0], values[1]);
		}
		
		return attributes;		
	}
}
