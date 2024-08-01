package framework.cucumber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import framework.cucumber.report.ReportGenerator;
import framework.data.entities.Capabilities;
import framework.dataProviders.ConfigFileReader;
import framework.testtools.AppiumFunctions;
import framework.testtools.ITestToolFunctions;
import framework.testtools.SeleniumFunctions;

public class TestContext {	
	private ITestToolFunctions testToolFunctions;
	private final Map<String, String> executionSettings;
	private final ReportGenerator report;
	
	public TestContext() throws Exception{		
		this.executionSettings = convertExecutionSettingsFromProperties();
		
		buildInstanceTestToolFunctions();				
		
		this.report = new ReportGenerator();
		
	}
	
	public ITestToolFunctions getTestToolFunctions() {
		return this.testToolFunctions;
	}
	
	public Map<String, String> getExecutionSettings() {
		return this.executionSettings;
	}
	
	public ReportGenerator getReport() {
		return this.report;
	}	
	
	private void buildInstanceTestToolFunctions() throws Exception{
		String testType = this.executionSettings.get("General.TestType");		
		switch (testType) {
			case "System":
				testToolFunctions = new SeleniumFunctions(this.executionSettings);
				break;
			case "Mobile":				
				List<Capabilities> capabilities = getListOfCapabilities(this.executionSettings);
				
				testToolFunctions = new AppiumFunctions(capabilities, this.executionSettings);
				break;
			default:
				throw new Exception("Invalid General.TestType configuration at cucumber.properties file");
		}
	}
	
	private Map<String, String> convertExecutionSettingsFromProperties() {
		ConfigFileReader reader = new ConfigFileReader("configs/cucumber.properties");
		
		Map<String, String> attributes = new HashMap<String, String>();
		
		reader.getProperties().forEach((key, value) -> attributes.put(key.toString(), value.toString().trim()));		
		
		return attributes;		
	}
	
	private List<Capabilities> getListOfCapabilities(Map<String, String> executionSettings) {
		List<Capabilities> capabilities = new ArrayList<Capabilities>();
		for (String config : executionSettings.keySet()) {
			if(!config.contains("Mobile.Capabilities"))
				continue;
				
			Capabilities cap = new Capabilities();
			cap.CapabilitiesName = config.replaceAll("Mobile.Capabilities.", "");
			cap.Value = executionSettings.get(config);
			capabilities.add(cap);
		}
		
		return capabilities;
	}
}
