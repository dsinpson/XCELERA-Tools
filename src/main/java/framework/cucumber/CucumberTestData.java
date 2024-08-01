package framework.cucumber;

import java.io.FileReader;

import com.fasterxml.jackson.databind.ObjectMapper;

import framework.data.entities.LayoutFileData;
import framework.data.entities.WebServiceData;

public class CucumberTestData {

	public WebServiceData getWebServiceData(String jsonFile) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		return  mapper.readValue(new FileReader("src/main/java/cucumberTests/data/ws/"+jsonFile+".json"), WebServiceData.class);
	}
	
	public LayoutFileData getLayoutFileData(String jsonFile) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		return  mapper.readValue(new FileReader("src/main/java/cucumberTests/data/layoutFiles/"+jsonFile+".json"), LayoutFileData.class);
	}
}
