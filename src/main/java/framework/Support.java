package framework;

import java.util.Map;

import framework.data.entities.Procedure;
import framework.enums.TypeError;
import framework.helpers.ExecutionStatusHelper;
import framework.helpers.GeneralHelper;
import framework.testtools.ITestToolFunctions;

public class Support {
	private final ITestToolFunctions _testToolFunctions;
	private Map<String, String> _executionConfigs;	
	
	private Procedure _procedure;
	
	public Support(ITestToolFunctions testToolFunctions, Map<String, String> executionConfigs) {
		_testToolFunctions = testToolFunctions;
		_executionConfigs = executionConfigs;
	}
	
	public Support(ITestToolFunctions testToolFunctions) {
		_testToolFunctions = testToolFunctions;
	}
	
	public void executeAction(String repositoryType, Procedure procedure) throws Exception {
		_procedure = procedure;
		
		String locator = "";
		
		if(repositoryType.equals("S"))
			locator = GeneralHelper.getLocatorProcedure(Integer.parseInt(_executionConfigs.get("System.Locator")), procedure);
		else if(repositoryType.equals("M"))
			locator = GeneralHelper.getLocatorProcedure(Integer.parseInt(_executionConfigs.get("Mobile.Locator")), procedure);
		
		switch((int) procedure.MethodId){
			case 6: //getShadowRootElement
				getShadowRootElement(locator);
				break;
			case 7: //releaseShadowRootElement
				releaseShadowRootElement();
				break;
			default:
				break;				
		}		
	}
	
	public void getShadowRootElement(String locator) throws Exception {
		try {
			_testToolFunctions.getShadowRootElement(locator);
		} catch(Exception e) {
			String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
			String comment = GeneralHelper.getCommentError(methodName,  e.getMessage());
			
			ExecutionStatusHelper helper = new ExecutionStatusHelper();
			helper.setStatusErrorOnProcedure(_procedure, TypeError.OperationalError, comment);
			
			throw e;
		}
	}
	
	public void releaseShadowRootElement() throws Exception {
		try {
			_testToolFunctions.releaseShadowRootElement();
		} catch(Exception e) {
			String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
			String comment = GeneralHelper.getCommentError(methodName,  e.getMessage());
			
			ExecutionStatusHelper helper = new ExecutionStatusHelper();
			helper.setStatusErrorOnProcedure(_procedure, TypeError.OperationalError, comment);
			
			throw e;
		}
	}
}
