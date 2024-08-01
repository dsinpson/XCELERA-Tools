package framework.testtools.utils;

import java.util.List;
import java.util.Map;

import framework.data.entities.Capabilities;
import framework.testtools.*;

public class TestToolUtils {
	public ITestToolFunctions setTestTool(String testTool ,
	String browser , List<Capabilities> listCapabilities,
	Map<String, String> executionConfigs) {
		
		switch(testTool) {
			case "Selenium":
				return new SeleniumFunctions(executionConfigs);
			case "Appium":
				return new AppiumFunctions(listCapabilities, executionConfigs);
			case "AutoIt":
				return new AutoItFunctions();
			case "Jemmy":
				return new JemmyFunctions();
			case "SAPVbScript":
				return new SAPVbScriptFunctions();
			default:
				return new SeleniumFunctions(executionConfigs);
		}
	}

}