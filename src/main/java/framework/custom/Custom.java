package framework.custom;

import java.util.Map;


import framework.custom.procedures.fm_pattern_procedures.*;
import framework.data.dynamicValues.DynamicValuesHelper;
import framework.data.entities.ExecutionData;
import framework.data.entities.Procedure;
import framework.enums.TypeError;
import framework.helpers.ExecutionStatusHelper;
import framework.helpers.GeneralHelper;
import framework.helpers.ScreenshotHelper;
import framework.testtools.ITestToolFunctions;
public class Custom {
	private ITestToolFunctions _testToolFunctions;
	private Map<String, String> _executionConfigs;
	private ScreenshotHelper _screenshotHelper;
	private Procedure _procedure;
	private Map<String, String> _attributes;
	private String _valueFromTestProcedure;
	private String _locator;
	boolean isWindows = System.getProperty("os.name").toLowerCase().contains("windows");
	private ProcedureBase _procedureBase;
	private	ExecutionData _testToExecute;
	DynamicValuesHelper _dynamicValueUtils;
	ExecutionStatusHelper helper = new ExecutionStatusHelper();



	public Custom(ITestToolFunctions testToolFunctions, Map<String, String> executionConfigs,
			ScreenshotHelper screenshotHelper) {
		_testToolFunctions = testToolFunctions;
		_executionConfigs = executionConfigs;
		_screenshotHelper = screenshotHelper;

	}
	//constructor to unit test
	public Custom (){}

	public void executeAction(String repositoryType, Procedure procedure, ExecutionData testToExecute) throws Exception {
		_procedure = procedure;
		_attributes = GeneralHelper.getMapAttributes(_procedure);
		_valueFromTestProcedure = _procedure.Value != null && !_procedure.Value.isEmpty() ? _procedure.Value : "";
		_locator = GeneralHelper.getLocatorProcedure(Integer.parseInt(_executionConfigs.get("System.Locator")),
				_procedure);


		_testToExecute=testToExecute;

		switch ((int) procedure.MethodId) {
		case 473:
			prepararDatosPrueba();
			break;
		case 476:
			validacionAmbientesAuthentic();
			break;
		case 469:
			iniciarConfiguracionRobot();
			break;
		case 472:
			configurarTarjeta();
			break;
		case 464:
			ejecutaRobot();
			break;
		case 475:
			validacionAuthentic();
			break;
		case 465:
			waitSeconds();
			break;
		case 468:
			captureOCR();
			break;
		case 470:
			validarTiempoPantalla();
			break;
		case 429:
			prepararDatosPrueba();
			break;
		case 427:
			validacionAmbientesAuthentic();
			break;
		case 428:
			iniciarConfiguracionRobot();
			break;
		case 430:
			configurarTarjeta();
			break;
		case 431:
			ejecutaRobot();
			break;
		case 434:
			validacionAuthentic();
			break;
		case 435:
			waitSeconds();
			break;
		case 432:
			captureOCR();
			break;
		case 433:
			validarTiempoPantalla();
			break;
			case 471:
			validacionIseries();
			break;
		case 902:
			verificarTransaccionEnTira();
			break;
		default:
			break;
		}
	}


	public void prepararDatosPrueba() throws Exception {
		_procedureBase = new TestDataPreparationFactory(_procedure,_screenshotHelper);
		_procedureBase.executeCustomProcedure();
	}

	public void validacionAmbientesAuthentic() throws Exception {
		_procedureBase = new AuthenticEnvironmentValidationFactory(_procedure,_screenshotHelper);
		_procedureBase.executeCustomProcedure();
	}

	public void iniciarConfiguracionRobot() throws Exception {
		_procedureBase = new RobotConfigurationStarFactory();
		_procedureBase.executeCustomProcedure();
	}

	public void configurarTarjeta() throws Exception {
		_procedureBase = new CardConfigurationFactory(_procedure,_screenshotHelper);
		_procedureBase.executeCustomProcedure();
	}

	public void ejecutaRobot() throws Exception {
		try {
			_procedureBase = new RobotExecutorFactory(_procedure, _screenshotHelper);
			_procedureBase.executeCustomProcedure();
		}catch (Exception e){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			String mensaje = "Fallo la ejecucion del robot ->" + e.getMessage();
			String comment = GeneralHelper.getCommentError(methodName, mensaje);
			_screenshotHelper.takeScreenshot();
			helper.setStatusErrorOnProcedure(_procedure, TypeError.OperationalError, comment);
		}
	}

	public void validacionAuthentic() throws Exception {
		_procedureBase = new AuthenticValidationFactory(_procedure,_screenshotHelper);
		_procedureBase.executeCustomProcedure();
	}
	public void verificarTransaccionEnTira() throws Exception {
		_procedureBase = new VerificarTransaccionEnTiraFactory(_procedure,_screenshotHelper);
		_procedureBase.executeCustomProcedure();
	}

	private void waitSeconds() throws Exception {
		_procedureBase = new PauserFactory(_procedure,_screenshotHelper);
		_procedureBase.executeCustomProcedure();

	}

	private void captureOCR() throws Exception {
		_procedureBase = new OCRCapturerFactory(_testToExecute,_procedure,_screenshotHelper,_testToolFunctions);
		_procedureBase.executeCustomProcedure();
	}

	private void validarTiempoPantalla() throws Exception {
		_procedureBase = new ScreenTimeValidatorFactory(_procedure,_screenshotHelper);
		_procedureBase.executeCustomProcedure();
	}

	private void validacionIseries() throws Exception{
		_procedureBase = new  BalanceIseriesFactory(_procedure,_screenshotHelper);
		_procedureBase.executeCustomProcedure();
	}






}
