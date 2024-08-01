package framework;

import java.util.Map;

import framework.data.entities.Procedure;
import framework.enums.TypeError;
import framework.helpers.ExecutionStatusHelper;
import framework.helpers.GeneralHelper;

public class TestData {
	
	private Procedure _procedure;
	private Map<String, String> _attributes;
	
	public void testData(Procedure procedure) throws Exception {
		
		_procedure = procedure;		
		_attributes = GeneralHelper.getMapAttributes(_procedure);

        if ((int) procedure.MethodId == 158) { //GenerateData
            generateData();
        }
	}
	
	private void generateData() throws Exception {
		try {
			String batPath = GeneralHelper.cNull(_attributes.get("BatPath"));
			if(batPath.isEmpty())
				throw new Exception("Invalid Bat Path");
			
			Process  p = Runtime.getRuntime().exec(batPath);
			p.waitFor();
		}
		catch(Exception e) {
			String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
			String comment = GeneralHelper.getCommentError(methodName,  e.getMessage());
			
			ExecutionStatusHelper helper = new ExecutionStatusHelper();
			helper.setStatusErrorOnProcedure(_procedure, TypeError.OperationalError, comment);
			
			throw e;
		}
	}
}
