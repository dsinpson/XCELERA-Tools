package framework;

import java.util.Map;

import framework.data.entities.Procedure;
import framework.enums.TypeError;
import framework.helpers.ExecutionStatusHelper;
import framework.helpers.GeneralHelper;
import framework.testtools.ITestToolFunctions;

public class Wait {

	private final ITestToolFunctions _testToolFunctions;
	private final Map<String, String> _executionConfigs;

	private Procedure _procedure;
	private Map<String, String> _attributes;

	public Wait(ITestToolFunctions testToolFunctions, Map<String, String> executionConfigs) {
		_testToolFunctions = testToolFunctions;
		_executionConfigs = executionConfigs;
	}

	public void executeAction(String repositoryType, Procedure procedure) throws Exception {

		_procedure = procedure;
		_attributes = GeneralHelper.getMapAttributes(_procedure);

		int methodId = (int) procedure.MethodId;

		switch (methodId) {
		case 74:
			waitSeconds();
			break;
		case 57:
			waitObject();
			break;
		default:
			break;
		}
	}

	private void waitSeconds() throws Exception {
		try {

			String seconds = _attributes.get("Sec");
			if (seconds == null || seconds.isEmpty())
				seconds = "5";

			System.out.println("Waiting: " + seconds + " seconds");
			Thread.sleep(Integer.parseInt(seconds) * 1000L);
		} catch (Exception e) {
			e.printStackTrace();
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			String comment = GeneralHelper.getCommentError(methodName, e.getMessage());
			ExecutionStatusHelper helper = new ExecutionStatusHelper();
			helper.setStatusErrorOnProcedure(_procedure, TypeError.OperationalError, comment);
			throw e;
		}
	}

	private void waitObject() throws Exception {
		try {

			String sec = _attributes.get("Sec");
			String condition = _attributes.get("Condition") == null ? "Appear" : _attributes.get("Condition");
			String identify = _attributes.get("Identify");

			boolean exists = !condition.equals("Vanish");

			if (identify == null || identify.equals(""))
				identify = GeneralHelper.getLocatorProcedure(Integer.parseInt(_executionConfigs.get("System.Locator")),
						_procedure);

			if (identify.equals("//*[@id=\"btnReplicarItensPainel\"]"))
				sec = "120";

			if (sec == null || sec.equals(""))
				sec = "60";

			if (exists)
				_testToolFunctions.WaitObjectVisibility(identify, Integer.parseInt(sec));
			else
				_testToolFunctions.waitObjectToVanish(identify, Integer.parseInt(sec));
		} catch (Exception e) {
			e.printStackTrace();
			// String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
			// String comment = GeneralHelper.getCommentError(methodName, e.getMessage());
			ExecutionStatusHelper helper = new ExecutionStatusHelper();
			helper.setStatusErrorOnProcedure(_procedure, TypeError.OperationalError, e.getMessage());
			throw e;
		}
	}
}
