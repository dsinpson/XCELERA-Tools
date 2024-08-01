package framework;

import java.util.HashMap;
import java.util.Map;

import framework.data.entities.Procedure;
import framework.enums.TypeError;
import framework.helpers.ExecutionStatusHelper;
import framework.helpers.GeneralHelper;
import framework.testtools.ITestToolFunctions;

public class Validation {

	private final ITestToolFunctions _testToolFunctions;
	private Map<String, String> _executionConfigs;

	private Procedure _procedure;

	public Validation(ITestToolFunctions testToolFunctions, Map<String, String> executionConfigs) {
		_testToolFunctions = testToolFunctions;
		_executionConfigs = executionConfigs;
	}

	public Validation(ITestToolFunctions testToolFunctions) {
		_testToolFunctions = testToolFunctions;
	}

	public void executeAction(String repositoryType, Procedure procedure) throws Exception {
		_procedure = procedure;

		String locator = "";
		Map<String, String> attributes = GeneralHelper.getMapAttributes(_procedure);
		String valueFromProcedure = _procedure.Value != null && !_procedure.Value.isEmpty() ? _procedure.Value : "";

		if (repositoryType.equals("S"))
			locator = GeneralHelper.getLocatorProcedure(Integer.parseInt(_executionConfigs.get("System.Locator")),
					procedure);
		else if (repositoryType.equals("M"))
			locator = GeneralHelper.getLocatorProcedure(Integer.parseInt(_executionConfigs.get("Mobile.Locator")),
					procedure);

		int methodId = (int) procedure.MethodId;

		switch (methodId) {
		case 105: // checkValue
			if (valueFromProcedure.isEmpty())
				valueFromProcedure = GeneralHelper.cNull(attributes.get("Value"));

			checkValue(locator, GeneralHelper.cNull(attributes.get("Contains")), valueFromProcedure);
			break;
		case 106: // checkAttribute
			if (!valueFromProcedure.isEmpty())
				attributes = convertToDictionaryOfAttribute(valueFromProcedure);

			checkAttribute(locator, attributes, GeneralHelper.cNull(attributes.get("Text")),
					GeneralHelper.cNull(attributes.get("ColorBG")), GeneralHelper.cNull(attributes.get("Color")));
			break;
		case 108: // checkLabel
			checkLabel(locator, GeneralHelper.cNull(attributes.get("Contains")), valueFromProcedure);
			break;
		case 109: // CheckAlert
			checkAlert(locator);
			break;
		default:
			break;
		}
	}

	public void checkValue(String locator, String contains, String text) throws Exception {
		try {
			String textCaptured = _testToolFunctions.captureText(locator).toUpperCase().trim();
			text = text != null ? text.toUpperCase().trim() : "";

			if (!contains.isEmpty()) {
				boolean statusElement = textCaptured.contains(text.toUpperCase());
				boolean desiredStatusContains = GeneralHelper.convertToBool(contains);
				String erroMessage = "the text is not the same status expected in contains";
				compareStatus(statusElement, desiredStatusContains, erroMessage);
			} else {
				String erroMessage = "the text is not equal from expected";
				areEqual(textCaptured, text, erroMessage);
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

	public void checkLabel(String locator, String contains, String text) throws Exception {
		try {
			checkValue(locator, contains, text);
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

	public void checkAlert(String locator) throws Exception {
		try {
			_testToolFunctions.checkAlert(locator);
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

	public void checkAttribute(String locator, Map<String, String> attributes, String text, String ColorBG,
			String Color) throws Exception {
		try {
			for (String attribute : attributes.keySet()) {
				String value = GeneralHelper.cNull(attributes.get(attribute));
				if (!value.isEmpty())
					checkByAttribute(locator, attribute, value, text, ColorBG, Color);
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

	public void checkByAttribute(String locator, String attribute, String value, String text, String ColorBG,
			String Color) throws Exception {

		String erroMessage;
		boolean statusElement;
		boolean desiredStatus;

		String valueCaptured;
		switch (GeneralHelper.cNull(attribute)) {
		case "Contains": // checkValue
			text = GeneralHelper.cNull(text);
			checkValue(locator, value, text);

			break;

		case "Color": // Text-Color
			valueCaptured = _testToolFunctions.captureCssColor(locator, "Color");
			erroMessage = "The element is not expected color " + value + " not same " + valueCaptured;

			areEqual(valueCaptured, value, erroMessage);

			break;

		case "ColorBG":
			valueCaptured = _testToolFunctions.captureCss(locator, "bgColor");
			erroMessage = "The element is not expected ColorBG " + value + " not same " + valueCaptured;
			areEqual(valueCaptured, value, erroMessage);
			// checkValue(valueCaptured,value,erroMessage);
			break;

		case "Visible":
			statusElement = _testToolFunctions.visible(locator);
			desiredStatus = GeneralHelper.convertToBool(value);
			erroMessage = "The element is not in the same visibility status";
			compareStatus(statusElement, desiredStatus, erroMessage);

			break;

		case "Exist": // Object exists
			statusElement = _testToolFunctions.exists(locator);
			desiredStatus = GeneralHelper.convertToBool(value);
			erroMessage = "The element is not in the same exist status";
			compareStatus(statusElement, desiredStatus, erroMessage);

			break;

		case "TextExists":
			text = GeneralHelper.cNull(text);
			checkValue(locator, value, text);

			break;

		case "Check":
			statusElement = _testToolFunctions.checked(locator);
			desiredStatus = GeneralHelper.convertToBool(value);
			erroMessage = "The checkbox is not in the same check status";
			compareStatus(statusElement, desiredStatus, erroMessage);

			break;

		case "Enabled":
			statusElement = _testToolFunctions.enabled(locator);
			desiredStatus = GeneralHelper.convertToBool(value);
			erroMessage = "The element is not in the same enable status";
			compareStatus(statusElement, desiredStatus, erroMessage);

			break;

		case "dataGrid":
			valueCaptured = _testToolFunctions.getDataGrid(locator).toString().trim();
			erroMessage = "TThe element dataGrid is not equals";
			areEqual(valueCaptured, value, erroMessage);

			break;

		case "Text":
			// Must not do anything
			break;

		default:
			throw new Exception("The attribute: " + attribute + "is not implemented");
		}
	}

	private Map<String, String> convertToDictionaryOfAttribute(String attributesAndValues) {

		Map<String, String> attributes = new HashMap<String, String>();

		if (attributesAndValues.contains(";")) {
			String[] attributePlusValue = attributesAndValues.split(";");

			for (String attributeAndValue : attributePlusValue) {
				String[] values = attributeAndValue.split("=");
				attributes.put(values[0], values[1]);
			}

		} else {
			String[] values = attributesAndValues.split("=");
			attributes.put(values[0], values[1]);
		}

		return attributes;

	}

	private void compareStatus(boolean statusElement, boolean desiredStatus, String erroMessage) throws Exception {
		if (statusElement != desiredStatus) {
			throw new Exception(erroMessage);
		}
	}

	private void areEqual(String textCaptured, String text, String erroMessage) throws Exception {

		if (!textCaptured.equals(text)) {
			throw new Exception(erroMessage);
		}
	}
}