package framework;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Map;

import framework.data.entities.*;
import framework.enums.TypeError;
import framework.helpers.ExecutionStatusHelper;
import framework.helpers.GeneralHelper;
import framework.helpers.ScreenshotHelper;
import framework.testtools.ITestToolFunctions;

public class Interaction {

	private final ITestToolFunctions _testToolFunctions;
	private ScreenshotHelper _screenshotHelper;
	private Map<String, String> _executionConfigs;

	Procedure _procedure = null;

	public Interaction(ITestToolFunctions testToolFunctions, Map<String, String> executionConfigs,
			ScreenshotHelper screenshotHelper) {
		_testToolFunctions = testToolFunctions;
		_screenshotHelper = screenshotHelper;
		_executionConfigs = executionConfigs;
	}

	public Interaction(ITestToolFunctions testToolFunctions) {
		_testToolFunctions = testToolFunctions;
	}

	public void executeAction(String repositoryType, Procedure procedure) throws Exception {

		_procedure = procedure;

		String locator = "";
		Map<String, String> attributes = GeneralHelper.getMapAttributes(procedure);
		String valueFromTestProcedure = procedure.Value != null && !procedure.Value.isEmpty() ? procedure.Value : "";

		if (repositoryType.equals("S"))
			locator = GeneralHelper.getLocatorProcedure(Integer.parseInt(_executionConfigs.get("System.Locator")),
					procedure);
		else if (repositoryType.equals("M"))
			locator = GeneralHelper.getLocatorProcedure(Integer.parseInt(_executionConfigs.get("Mobile.Locator")),
					procedure);

		switch ((int) procedure.MethodId) {
		case 1: // Click
			click(locator, GeneralHelper.cNull(attributes.get("X")), GeneralHelper.cNull(attributes.get("Y")),
					GeneralHelper.cNull(attributes.get("IfExists")));
			break;
		case 73: // doubleClick
			doubleClick(locator, GeneralHelper.cNull(attributes.get("X")), GeneralHelper.cNull(attributes.get("Y")));
			break;
		case 2: // SetValue
			setValue(locator, valueFromTestProcedure, GeneralHelper.cNull(attributes.get("IfExists")));
			break;
		case 3: // Select
			selectValue(locator, GeneralHelper.cNull(attributes.get("Type")),
					GeneralHelper.cNull(attributes.get("MustExist")), valueFromTestProcedure);
			break;
		case 5: // PrintScreen
			capturePrint();
			break;
		case 14: // MaximizedWindow
			maximazedWindow(locator);
			break;
		case 87: // Set value with comparison
			setValueWithComparison(locator, valueFromTestProcedure,
					GeneralHelper.cNull(attributes.get("CompareCaseSensitive")),
					GeneralHelper.getFormatValue(procedure));
			break;
		case 88: // SetFocus
			setFocus(locator);
			break;
		case 89: // MouseMove
			mouseMoveStr(locator, GeneralHelper.cNull(attributes.get("X")), GeneralHelper.cNull(attributes.get("Y")));
			break;
		case 90: // CheckBox
			checkBox(locator, valueFromTestProcedure, GeneralHelper.cNull(attributes.get("Checked")));
			break;
		case 91: // ClearField
			clearField(locator);
			break;
		case 92:
			textSelect(locator);
			break;
		case 93:
			drag(locator, GeneralHelper.cNull(attributes.get("X")), GeneralHelper.cNull(attributes.get("Y")));
			break;
		case 94:
			clickWhileExists(locator, GeneralHelper.cNull(attributes.get("ClickLimit")),
					GeneralHelper.cNull(attributes.get("MustExist")));
			break;
		case 95: // SendKeys
			sendKeys(valueFromTestProcedure);
			break;
		case 135: // SendKeys
			radioButton(locator, valueFromTestProcedure, GeneralHelper.cNull(attributes.get("Checked")));
			break;
		default:
			break;
		}
	}

	public void radioButton(String locator, String value, String checked) throws Exception {
		try {
			if (value.contains("<IGNORE>"))
				return;

			if (value.isEmpty())
				value = checked;

			if (value.isEmpty())
				value = "False";

			if (!Boolean.parseBoolean(value)) {
            }
			else
				_testToolFunctions.click(locator);
		} catch (Exception e) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			String comment = GeneralHelper.getCommentError(methodName, e.getMessage());

			ExecutionStatusHelper helper = new ExecutionStatusHelper();
			helper.setStatusErrorOnProcedure(_procedure, TypeError.OperationalError, comment);

			throw e;
		}
	}

	public void loadPosition(String locator, String x, String y, int width, int height, int topValue, int leftValue,
			String mustExist) throws Exception {

		if (x == null && width != 0) {

			int centerX = leftValue + (width / 2);
			int centerY = topValue + (height / 2);

			x = Integer.toString(centerX);
			y = Integer.toString(centerY);
		}

		if (mustExist.isEmpty())
			mustExist = "True";

		if (!Boolean.parseBoolean(mustExist) && !_testToolFunctions.exists(locator)) {
        }
	}

	public void click(String locator, String x, String y, String ifExists) throws Exception {
		try {
			if (ifExists == null || ifExists.isEmpty())
				ifExists = "False";

			if (Boolean.parseBoolean(ifExists)) {
				try {
					_testToolFunctions.WaitObjectVisibility(locator, 5);
				} finally {
					if (!_testToolFunctions.enabled(locator))
						return;
				}
			}

			if (!(x.isEmpty()) && !(y.isEmpty())) {
				loadPosition(locator, x, y, 0, 0, 0, 0, ifExists);
				_testToolFunctions.click(locator, Integer.parseInt(x), Integer.parseInt(y));
			} else
				_testToolFunctions.click(locator);
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

	public void doubleClick(String locator, String x, String y) throws Exception {
		try {
			if (x.isEmpty()) {
				loadPosition(locator, x, y, 0, 0, 0, 0, "");
				_testToolFunctions.doubleClick(locator, Integer.parseInt(x), Integer.parseInt(y));
			} else
				_testToolFunctions.doubleClick(locator);

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

	public void mouseMoveStr(String locator, String x, String y) throws Exception {
		try {
			if (x != null) {
				loadPosition(locator, x, y, 0, 0, 0, 0, "");
				_testToolFunctions.mouseMove(locator, Integer.parseInt(x), Integer.parseInt(y));
			}
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

	public void setValue(String locator, String value, String ifExist) throws Exception {

		try {

			if (value.contains("<IGNORE>"))
				return;
			if (ifExist == null || ifExist.isEmpty()) {
				ifExist = "false";
			}

			if (Boolean.parseBoolean(ifExist) && !_testToolFunctions.enabled(locator)) {
				return;
			}
			_testToolFunctions.setValue(locator, value);
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

	public void selectValue(String locator, String tipo, String mustExist, String value) throws Exception {
		try {
			if (value.contains("<IGNORE>"))
				return;

			if (mustExist == null || mustExist.isEmpty())
				mustExist = "True";

			if (Boolean.parseBoolean(mustExist) && !_testToolFunctions.enabled(locator))
				return;

			if (value.contains(";")) {

				String[] array = value.split(";");

				tipo = array[0];
				String valor = array[1];
				_testToolFunctions.selectValue(locator, tipo, valor);

			} else {
				_testToolFunctions.selectValue(locator, tipo, value);
			}
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

	public void setValueWithComparison(String locator, String value, String compareCaseSensitive, String format)
			throws Exception {
		try {
			if (value.contains("<IGNORE>"))
				return;

			setValue(locator, value, "False");

			String textCaptured = _testToolFunctions.captureText(locator);
			String defaultFormat = format;
			defaultFormat = defaultFormat != null ? defaultFormat : "";

			if (compareCaseSensitive.isEmpty())
				compareCaseSensitive = "False";

			if (compareCaseSensitive.equals("True")) {
				if (textCaptured != defaultFormat) {
					if (_testToolFunctions.exists(locator)) {
                    } else {
						throw new Exception("Element don't exist");
					}
				}
			} else {
				if (textCaptured.toUpperCase() != defaultFormat.toUpperCase()) {
					if (_testToolFunctions.exists(locator)) {
                    } else {
						throw new Exception("Element don't exist");
					}
				}
			}
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

	public void maximazedWindow(String locator) throws Exception {
		try {
			_testToolFunctions.maximazedWindow(locator);
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

	public void setFocus(String locator) throws Exception {
		try {
			_testToolFunctions.setFocus(locator);
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

	public void capturePrint() throws Exception {

		try {
			_screenshotHelper.takeScreenshot();
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

	public void checkBox(String locator, String value, String checked) throws Exception {
		try {
			if (value.contains("<IGNORE>"))
				return;

			String checkedAction = value;

			if (checkedAction == null || checkedAction.isEmpty())
				checkedAction = checked;

			boolean chekedActionBool = GeneralHelper.convertToBool(checkedAction);
			_testToolFunctions.checkBox(locator, chekedActionBool);
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

	public void clearField(String locator) throws Exception {
		try {
			_testToolFunctions.clearField(locator);
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

	public void textSelect(String locator) throws Exception {
		try {
			_testToolFunctions.textSelect(locator);
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

	public void drag(String locator, String x, String y) throws Exception {

		try {
			loadPosition(locator, x, y, 0, 0, 0, 0, "");
			_testToolFunctions.drag(locator, Integer.parseInt(x), Integer.parseInt(y));
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

	public void clickWhileExists(String locator, String clickLimit, String mustExist) throws Exception {
		try {

			if (mustExist.isEmpty())
				mustExist = "True";

			if (!Boolean.parseBoolean(mustExist) && !_testToolFunctions.exists(locator))
				return;

			if (clickLimit.isEmpty())
				clickLimit = "30";

			// Click without limit
			if (clickLimit.equalsIgnoreCase("Nolimit")) {
				while (_testToolFunctions.exists(locator)) {
					click(locator, "", "", mustExist);
					Thread.sleep(500);
				}
			} else {
				int clickLimitPar = Integer.parseInt(clickLimit);
				int auxClickLimit = 0;

				while (_testToolFunctions.exists(locator) && auxClickLimit < clickLimitPar) {
					click(locator, "", "", mustExist);
					auxClickLimit++;
					Thread.sleep(500);
				}

				if (_testToolFunctions.exists(locator)) {
					String comment = "Object exists";
					ExecutionStatusHelper helper = new ExecutionStatusHelper();
					helper.setStatusErrorOnProcedure(_procedure, TypeError.Defect, comment);
                }
			}
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

	public void sendKeys(String value) throws Exception {
		try {

			String keys = value;

			if (keys.isEmpty() || keys.equalsIgnoreCase("<IGNORE>"))
				return;

			Thread.sleep(200);

			Robot robot = new Robot();
			robot.setAutoDelay(50);

			if (keys.startsWith("VK_") && keys.contains(";")) {
				robot.keyPress(GeneralHelper.returnVk(keys.split(";")[0]));

				robot.keyPress(GeneralHelper.returnVk(keys.split(";")[1]));
				robot.keyRelease(GeneralHelper.returnVk(keys.split(";")[1]));

				robot.keyRelease(GeneralHelper.returnVk(keys.split(";")[0]));

			} else if (keys.contains("VK_")) {
				robot.keyPress(GeneralHelper.returnVk(keys));
				robot.keyRelease(GeneralHelper.returnVk(keys));

			} else {
				char[] tecla = keys.toCharArray();
				for (int i = 0; i < tecla.length; i++) {
					String digitar = GeneralHelper.getSendKeys(tecla[i]);

					if (digitar.contains("VK_") && digitar.contains(";")) {
						robot.keyPress(GeneralHelper.returnVk(digitar.split(";")[0]));

						robot.keyPress(GeneralHelper.returnVk(digitar.split(";")[1]));
						robot.keyRelease(GeneralHelper.returnVk(digitar.split(";")[1]));

						robot.keyRelease(GeneralHelper.returnVk(digitar.split(";")[0]));

					} else if (digitar.contains("VK_")) {

						robot.keyPress(GeneralHelper.returnVk(digitar));
						robot.keyRelease(GeneralHelper.returnVk(digitar));

					} else {
						robot.keyPress(KeyEvent.VK_ALT);
						for (int j = 0; j < digitar.length(); j++) {
							robot.keyPress(GeneralHelper.returnVk(GeneralHelper.getSendKeys(digitar.charAt(j))));
							robot.keyRelease(GeneralHelper.returnVk(GeneralHelper.getSendKeys(digitar.charAt(j))));
						}
						robot.keyRelease(KeyEvent.VK_ALT);
					}
				}
			}
			Thread.sleep(300);
			robot.keyPress(KeyEvent.VK_DOWN);
			robot.keyRelease(KeyEvent.VK_DOWN);
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

	public void submit(String locator) throws Exception {
		try {
			_testToolFunctions.submit(locator);
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
}
