package framework;

import java.util.Map;

import framework.data.entities.*;
import framework.enums.TypeError;
import framework.helpers.ExecutionStatusHelper;
import framework.helpers.GeneralHelper;
import framework.testtools.ITestToolFunctions;

public class Navigation {
	
	private final ITestToolFunctions _testToolFunctions;
	private Map<String, String> _executionConfigs;
	
	private Procedure _procedure;
	
	public Navigation(ITestToolFunctions testToolFunctions, Map<String, String> executionConfigs) {
		_testToolFunctions = testToolFunctions;
		_executionConfigs = executionConfigs;
	}
	
	public Navigation(ITestToolFunctions testToolFunctions) {
		_testToolFunctions = testToolFunctions;
	}
	
	public void executeAction(String repositoryType, Procedure procedure) throws Exception {
		_procedure = procedure;
		
		String locator = "";
		Map<String, String> attributes = GeneralHelper.getMapAttributes(_procedure);
		
		if(repositoryType.equals("S"))
			locator = GeneralHelper.getLocatorProcedure(Integer.parseInt(_executionConfigs.get("System.Locator")), procedure);
		else if(repositoryType.equals("M"))
			locator = GeneralHelper.getLocatorProcedure(Integer.parseInt(_executionConfigs.get("Mobile.Locator")), procedure);
		
		int methodId = (int) procedure.MethodId;
		
		switch(methodId) {
			case 76:
				openBrowser(GeneralHelper.cNull(attributes.get("URL"))); //open browser
				break;
			case 96: //Close
				closeWindow(locator);
				break;
				case 97: //Close
				forceCloseBrowser(_executionConfigs.get("System.BrowserName"));
				break;
			case 98: //CloseBrowser
				closeBrowser();
				break;
			case 99: //OpenExe
				openExe(GeneralHelper.cNull(attributes.get("Path")));
				break;
			case 101: //closeEXE
				closeExe(GeneralHelper.cNull(attributes.get("Application")));
				break;
			case 102://Scroll
				scroll(locator);
				break;
			case 103:	//switchTo
				switchTo(GeneralHelper.cNull(attributes.get("Index")), GeneralHelper.cNull(attributes.get("Identify")));	
				break;
			case 104:	//switchTab
				switchTab();
				break;
			default:
				break;
		}
	}	
	
	public void openBrowser(String url) throws Exception{
		try {
			_testToolFunctions.openApp(url);
		}
		catch(Exception e) {
			e.printStackTrace();
			
			String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
			String comment = GeneralHelper.getCommentError(methodName,  e.getMessage());
			
			ExecutionStatusHelper helper = new ExecutionStatusHelper();
			helper.setStatusErrorOnProcedure(_procedure, TypeError.OperationalError, comment);
			
			throw e; 
		}		
	}
	
	public void closeWindow(String locator) throws Exception {
		try {
			_testToolFunctions.closeWindow(locator);
		}
		catch(Exception e) {
			e.printStackTrace();
			String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
			String comment = GeneralHelper.getCommentError(methodName,  e.getMessage());
			ExecutionStatusHelper helper = new ExecutionStatusHelper();
			helper.setStatusErrorOnProcedure(_procedure, TypeError.OperationalError, comment);
			throw e; 
		}
	}
	
	public void forceCloseBrowser(String browserName) throws Exception{
		try {
			
			switch (browserName) {
				case "IE":
					Runtime.getRuntime().exec("taskkill /im iexplore.exe");
					Thread.sleep(2000);	
					break;
				case "CHROME":
					Runtime.getRuntime().exec("taskkill /im chrome.exe");
					Thread.sleep(2000);	
					break;	
				case "FIREFOX":
					Runtime.getRuntime().exec("taskkill /im firefox.exe");
					Thread.sleep(2000);	
					break;						
				default:
					Runtime.getRuntime().exec("taskkill /im iexplore.exe");
					Thread.sleep(2000);					
					break;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
			String comment = GeneralHelper.getCommentError(methodName,  e.getMessage());
			ExecutionStatusHelper helper = new ExecutionStatusHelper();
			helper.setStatusErrorOnProcedure(_procedure, TypeError.OperationalError, comment);
			throw e;
		}
		
	}
	
	public void closeBrowser() throws Exception {
		try {
			_testToolFunctions.closeBrowser();
		}
		catch(Exception e) {
			e.printStackTrace();
			String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
			String comment = GeneralHelper.getCommentError(methodName,  e.getMessage());
			ExecutionStatusHelper helper = new ExecutionStatusHelper();
			helper.setStatusErrorOnProcedure(_procedure, TypeError.OperationalError, comment);
			throw e;
		}		
	}
	
	public void openExe(String path) throws Exception{
		try {
			String newPath="";
			
			//Separa no \\
			String[] teste = path.split("\\\\");
	
			//pega tudo que vem antes do nome do aruqivo . exe e guarda na newPath
			for(int i = 0; i < teste.length; i++){
				if (i!=teste.length-1){
					newPath = newPath+teste[i]+"\\";
				}
			}
			//Cria um arquivo com a pasta de onde esta o exe
			java.io.File f = new java.io.File(newPath); 
	
			System.out.println(f.getAbsolutePath()+"\\"+teste[teste.length-1]);
	
			//executa a pasta, somando com \arquivo.exe
			if(path.endsWith(".jar")){
				Runtime.getRuntime().exec("java -jar "+f.getAbsolutePath()+"\\"+teste[teste.length-1] );
			}else{
				//Runtime.getRuntime().exec(f.getAbsolutePath()+"\\"+teste[teste.length-1] );
				Runtime.getRuntime().exec(path);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
			String comment = GeneralHelper.getCommentError(methodName,  e.getMessage());
			ExecutionStatusHelper helper = new ExecutionStatusHelper();
			helper.setStatusErrorOnProcedure(_procedure, TypeError.OperationalError, comment);
			throw e;
		}		
	}
	
	public void closeExe(String app) throws Exception{
		try {
			Runtime.getRuntime().exec("cmd /c taskkill /im "+ app);
		}
		catch(Exception e) {
			e.printStackTrace();
			String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
			String comment = GeneralHelper.getCommentError(methodName,  e.getMessage());
			ExecutionStatusHelper helper = new ExecutionStatusHelper();
			helper.setStatusErrorOnProcedure(_procedure, TypeError.OperationalError, comment);
			throw e;
		}		
	}
	
	public void scroll(String locator) throws Exception{
		try {
			_testToolFunctions.scroll(locator, 0, 0);
		}
		catch(Exception e) {
			e.printStackTrace();
			String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
			String comment = GeneralHelper.getCommentError(methodName,  e.getMessage());
			ExecutionStatusHelper helper = new ExecutionStatusHelper();
			helper.setStatusErrorOnProcedure(_procedure, TypeError.OperationalError, comment);
			throw e;	
		}		
	}
	
	public void switchTo(String index, String identify) throws Exception{
				try {
			String locator = identify;		
			
			if(locator == null) locator = index;
			
			if(locator == null) locator = "";
			
			_testToolFunctions.switchTo(locator);			
		}
		catch(Exception e) {
			e.printStackTrace();
			String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
			String comment = GeneralHelper.getCommentError(methodName,  e.getMessage());
			ExecutionStatusHelper helper = new ExecutionStatusHelper();
			helper.setStatusErrorOnProcedure(_procedure, TypeError.OperationalError, comment);
			throw e;	
		}		
	}
	
	public void switchTab() throws Exception{
		try {
			_testToolFunctions.switchTab();
		}
		catch(Exception e) {
			e.printStackTrace();
			String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
			String comment = GeneralHelper.getCommentError(methodName,  e.getMessage());
			ExecutionStatusHelper helper = new ExecutionStatusHelper();
			helper.setStatusErrorOnProcedure(_procedure, TypeError.OperationalError, comment);
			throw e;	
		}		
	}
}
