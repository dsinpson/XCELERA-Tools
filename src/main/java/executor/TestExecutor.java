package executor;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import framework.custom.procedures.OCRCapturer;
import framework.custom.utils.Evidencia;
import org.junit.Test;

import framework.Interaction;
import framework.LayoutFile;
import framework.Navigation;
import framework.RestWebService;
import framework.Validation;
import framework.Wait;
import framework.custom.Custom;
import framework.custom.entities.StaticFields;
import framework.data.dynamicValues.DynamicValuesHelper;
import framework.data.entities.ActionData;
import framework.data.entities.Capabilities;
import framework.data.entities.ExecutionConfig;
import framework.data.entities.ExecutionData;
import framework.data.entities.LayoutFileData;
import framework.data.entities.Prints;
import framework.data.entities.Procedure;
import framework.data.entities.WebServiceData;
import framework.enums.TypeError;
import framework.helpers.ExecutionStatusHelper;
import framework.helpers.GeneralHelper;
import framework.helpers.ScreenshotHelper;
import framework.localAgent.ScheduledExecution;
import framework.testtools.ITestToolFunctions;
import framework.testtools.utils.TestToolUtils;
import framework.custom.procedures.OCRCapturer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestExecutor {

	private ExecutionData _testToExecute;
	private Prints _prints;
	private ITestToolFunctions _testToolFunctions;
	private ITestToolFunctions _testToolFunctionsSystem;
	private ITestToolFunctions _testToolFunctionsMobile;
	private ExecutionStatusHelper _statusHelper;
	private ScreenshotHelper _screenshotHelper;
	private ScheduledExecution _schedule;
	private Map<String, String> _executionConfigs;
	private final String  _directoryLogs="dirDefaultLogs";
	private final String  _directoryPrints="dirDefaultPrints";

	DynamicValuesHelper _dynamicValueUtils;

	@Test
	public void executeTest() {

		try {
			_schedule = new ScheduledExecution();

			while (true) {
				try {
					_testToExecute = _schedule.getNextTestToExecute();
					if (_testToExecute == null) {
						GeneralHelper.waitingNextExecution();
						continue;
					}
					StaticFields.clearContextFields();

					startTestExecution();

					GeneralHelper.CreateFolder(_testToExecute, _directoryPrints);
                    GeneralHelper.CreateFolder (_testToExecute, _directoryLogs);
					List<ActionData> actions = _testToExecute.Actions;
					String scenarioName = _testToExecute.TestCaseName;
					System.out.println("Executing test: " + scenarioName);
					//FarolBot.chamarEndPoint(StaticFields.getFarolEndPoint(), StaticFields.getWorkerName(), scenarioName,false);
					int procIndex = 1;
					for (ActionData action : actions) {
						try {
							System.out.println("[ " + procIndex + " ] - " + action.Name);

							if (_testToExecute.UserId <= 0 || (_testToExecute.UserId > 0 && action.ReadyToPlay
									&& action.Status.equalsIgnoreCase("Executing"))){

								executeAction(_testToExecute.TestTypeId, action);

								if (action.Status.equals("Failed")) {
									_statusHelper.setStatusErrorOnExecution(_testToExecute, action);
									_screenshotHelper.takeScreenshot();
									break;
								}
							}
						} catch (Exception e) {
							_statusHelper.setStatusErrorOnExecution(_testToExecute, action);
							_screenshotHelper.takeScreenshot();
							break;
						}
						procIndex++;
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {

					try {
						finishTestExecution();
						//FarolBot.chamarEndPoint(StaticFields.getFarolEndPoint(), StaticFields.getWorkerName(), "",true);
					} catch (UnknownHostException ignore) {
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void executeAction(long testTypeId, ActionData action) throws Exception {
		try {
			startActionExecution(action);
			if (action.LayoutFile != null) {
				try {
					executeLayoutFile(action.LayoutFile);
				} catch (Exception e) {
					_statusHelper.setStatusErrorOnAction(action, e.getMessage());
					throw e;
				}
			} else if (action.WebService != null) {
				try {
					executeWebService(testTypeId, action.WebService);
				} catch (Exception e) {
					_statusHelper.setStatusErrorOnAction(action, e.getMessage());
					throw e;
				}
			} else if (action.Procedures != null) {
				for (Procedure procedure : action.Procedures) {
					try {
						executeProcedure(action.RepositoryType, procedure);

						if (procedure.Status.equals("Failed")) {
							_statusHelper.setStatusErrorOnAction(action, procedure);
							break;
						}else {
							_statusHelper.setStatusLogOkAction(action, procedure);
						}

					} catch (Exception e) {
						_statusHelper.setStatusErrorOnAction(action, procedure);
						throw e;
					}
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			finishActionExecution(action);
		}
	}

	private void executeProcedure(String repositoryType, Procedure procedure) throws Exception {
		try {

			startProcedureExecution(procedure);

			if (repositoryType.equals("S"))
				_dynamicValueUtils.changeDynamicValue(Integer.parseInt(_executionConfigs.get("System.Locator")),
						procedure, _executionConfigs);
			else if (repositoryType.equals("M"))
				_dynamicValueUtils.changeDynamicValue(Integer.parseInt(_executionConfigs.get("Mobile.Locator")),
						procedure, _executionConfigs);

			if (procedure.Value != null) {
				if (procedure.Value.contains("<IGNORE>"))
					return;
			}
			switch ((int) procedure.CategoryMethodId) {
			case 1:
				new Interaction(_testToolFunctions, _executionConfigs, _screenshotHelper).executeAction(repositoryType,
						procedure);

				break;
			case 2:
				new Validation(_testToolFunctions, _executionConfigs).executeAction(repositoryType, procedure);

				break;
			case 17:
				new Wait(_testToolFunctions, _executionConfigs).executeAction(repositoryType, procedure);

				break;
			case 25:
				new Navigation(_testToolFunctions, _executionConfigs)

						.executeAction(repositoryType, procedure);

				break;
			default:
				new Custom(_testToolFunctions, _executionConfigs, _screenshotHelper).executeAction(repositoryType,
						procedure,_testToExecute);

				break;
			}
			System.out.println(procedure.CategoryMethodName + "." + procedure.MethodName
					+ (procedure.Object.Name != null ? (" -> " + procedure.Object.Name) : ""));
		} catch (Exception e) {
			throw e;
		} finally {
			finishProcedureExecution(procedure);
		}
	}

	private void executeWebService(long testTypeId, WebServiceData webService) throws Exception {
		try {
			RestWebService restWebService = new RestWebService(_executionConfigs);
			restWebService.executeAction(testTypeId, webService);
		} catch (Exception e) {
			throw e;
		}
	}

	private void executeLayoutFile(LayoutFileData layoutFileData) throws Exception {
		try {
			_dynamicValueUtils.changeDynamicPathAndFilename(layoutFileData);

			LayoutFile layoutFile = new LayoutFile();
			layoutFile.testFile(layoutFileData);
		} catch (Exception e) {
			throw e;
		}
	}

	private void startTestExecution() throws Exception {
		try {
			boolean ocr = OCRCapturer.openCamWebMonitor();
			if(true){
				_statusHelper = new ExecutionStatusHelper();
				_executionConfigs = GeneralHelper.getMapConfigs(_testToExecute);
				_dynamicValueUtils = new DynamicValuesHelper();

				TestToolUtils testToolUtils = new TestToolUtils();
				if (_executionConfigs.get("System.TestTool") != null) {
					_testToolFunctionsSystem = testToolUtils.setTestTool(_executionConfigs.get("System.TestTool"),
							_executionConfigs.get("System.Browser"), null, _executionConfigs);

				}

				if (_executionConfigs.get("Mobile.TestTool") != null) {
					List<Capabilities> capabilities = new ArrayList<Capabilities>();
					for (ExecutionConfig config : _testToExecute.ExecutionConfig) {
						Capabilities cap = new Capabilities();
						cap.CapabilitiesName = config.ConfigName.replaceAll("Mobile.Capabilities.", "");
						cap.Value = config.ConfigValue;
						capabilities.add(cap);
					}

					_testToolFunctionsMobile = testToolUtils.setTestTool(_executionConfigs.get("Mobile.TestTool"),
							_executionConfigs.get("System.Browser"), capabilities, _executionConfigs);
				}

				_testToExecute.StartDate = GeneralHelper.getDate();
				_testToExecute.Status = "Passed";

			}
		}catch (Exception e){
			e.printStackTrace();
		}


	}

	private void finishTestExecution() throws Exception {
		if (_testToExecute != null) {
			_testToExecute.EndDate = GeneralHelper.getDate();
			_screenshotHelper.waitScreenshotThreadToFinish();

			if (_testToolFunctionsSystem != null)
				_testToolFunctionsSystem.closeDriver();

			if (_testToolFunctionsMobile != null)
				_testToolFunctionsMobile.closeDriver();

			_schedule.sendExecutionData(_testToExecute);
			Evidencia.getLog(_testToExecute);
			Evidencia.clearText();
			Evidencia.savePDF(_testToExecute);
			Evidencia.compressPdf(_testToExecute);
		}
	}

	private void startActionExecution(ActionData action) throws Exception {
		action.StartDate = GeneralHelper.getDate();
		action.Status = "Passed";
		action.Comment = "";

		switch (action.RepositoryType) {
		case "S": {
			_testToolFunctions = _testToolFunctionsSystem;
			break;
		}
		case "M": {
			_testToolFunctions = _testToolFunctionsMobile;
			break;
		}
		default: {
			_testToolFunctions = _testToolFunctionsSystem;
		}

		}

		_prints = new Prints();
		_prints.ExecutionId = _testToExecute.Id;
		_prints.ActionId = action.ActionId;
		_prints.Order = action.Order;
		_prints.Print = new ArrayList<String>();
		_screenshotHelper = new ScreenshotHelper(_testToolFunctions, _testToExecute, action, _prints);
	}

	private void finishActionExecution(ActionData action) throws Exception {
		action.EndDate = GeneralHelper.getDate();

		if (!action.RepositoryType.equals("W")) {
			_screenshotHelper.waitScreenshotThreadToFinish();
			Prints printsCopy = new Prints();
			printsCopy.ExecutionId = _prints.ExecutionId;
			printsCopy.ActionId = _prints.ActionId;
			printsCopy.Order = _prints.Order;
			printsCopy.Print = _prints.Print;
			new Thread() {
				public void run() {
					try {

						_schedule.SendPrintScreen(printsCopy);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
	}

	private void startProcedureExecution(Procedure procedure) throws Exception {
		try {
			procedure.StartDate = GeneralHelper.getDate();
			procedure.Status = "Passed";
			procedure.Comment = "";

		} catch (Exception e) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			String comment = GeneralHelper.getCommentError(methodName, e.getMessage());
			ExecutionStatusHelper helper = new ExecutionStatusHelper();
			helper.setStatusErrorOnProcedure(procedure, TypeError.OperationalError, comment);
			throw e;
		}
	}

	private void finishProcedureExecution(Procedure procedure) throws Exception {
		procedure.EndDate = GeneralHelper.getDate();
	}

}
