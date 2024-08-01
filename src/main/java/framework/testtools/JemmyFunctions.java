package framework.testtools;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.lang.reflect.Method;
import java.util.List;

import org.netbeans.jemmy.ClassReference;
import org.netbeans.jemmy.JemmyProperties;
import org.netbeans.jemmy.operators.ContainerOperator;
import org.netbeans.jemmy.operators.JDialogOperator;
import org.netbeans.jemmy.operators.JFrameOperator;
import org.openqa.selenium.WebElement;

public class JemmyFunctions implements ITestToolFunctions {

	ContainerOperator mainFrame;
	long WaitComponentTimeout = 20000;

	public int runIt(Object param) {
		return 1;
	}

	private Object[] guiObjectChild(String stringObj) throws Exception {
		String methodName;
		Method method;
		methodName = stringObj.substring(0, stringObj.indexOf("("));
		String stringObjCompl = "org.netbeans.jemmy.operators." + methodName;
		String methodParameters = stringObj.substring(stringObj.indexOf("(") + 1, stringObj.indexOf(")")).replace("\"",
				"");
		String[] parametersArray = methodParameters.split(",");
		Class methodClass = Class.forName(stringObjCompl);
		Object objRet;

		objRet = methodClass.getConstructor(ContainerOperator.class, String.class, int.class).newInstance(mainFrame,
				parametersArray[0], Integer.parseInt(parametersArray[1]));

		return new Object[] { methodClass, objRet };
	}

	private ContainerOperator guiObjectWindow(String stringObj) throws Exception {
		String methodName;
		Method method;
		methodName = stringObj.substring(0, stringObj.indexOf("("));
		String stringObjCompl = "org.netbeans.jemmy.operators." + methodName;
		String methodParameters = stringObj.substring(stringObj.indexOf("(") + 1, stringObj.indexOf(")")).replace("\"",
				"");
		String[] parametersArray = methodParameters.split(",");
		Class methodClass = Class.forName(stringObjCompl);
		Object objRet;

		objRet = methodClass.getConstructor(String.class, int.class).newInstance(parametersArray[0],
				Integer.parseInt(parametersArray[1]));

		// method=((Class)methodClass).getMethod("setTimeouts",int.class);
		// method.invoke(objRet);

		return (ContainerOperator) objRet;
	}

	private String getAttach(String attach) {
		return attach.replace("\"", "");
	}

	public void attach(String text) throws Exception {
		JemmyProperties.setCurrentTimeout("ComponentOperator.WaitComponentTimeout", WaitComponentTimeout);

		mainFrame = guiObjectWindow(text);

	}

	@Override
	public void attach() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public String captureCss(String attach) throws Exception {
		throw new Exception("[CaptureCss]: Not implemented");
	}

	@Override
	public String captureCssColor(String attach) throws Exception {
		throw new Exception("[CaptureCssColor]: Not implemented");
	}

	@Override
	public String captureText(String attach) throws Exception {
		attach = getAttach(attach);

		Object[] obj = (Object[]) guiObject(attach);
		Method method = ((Class) obj[0]).getMethod("getText");
		String ret = (String) method.invoke(obj[1]);
		return ret;
	}

	@Override
	public String verificaIndice(List<WebElement> objeto) throws Exception {
		throw new Exception("[VerificaIndice]: Not implemented");
	}

	@Override
	public String VerificaIndice(String Attach) throws Exception {
		throw new Exception("[VerificaIndice]: Not implemented");
	}

	@Override
	public boolean visible(String Attach) throws Exception {
		throw new Exception("[Visible]: Not implemented");
	}

	@Override
	public boolean exists(String Attach) throws Exception {
		try {
			JemmyProperties.setCurrentTimeout("ComponentOperator.WaitComponentTimeout", 1);

			guiObject(Attach);

			JemmyProperties.setCurrentTimeout("ComponentOperator.WaitComponentTimeout", WaitComponentTimeout);

			return true;
		} catch (Exception e) {
			JemmyProperties.setCurrentTimeout("ComponentOperator.WaitComponentTimeout", WaitComponentTimeout);
			return false;
		}
	}

	@Override
	public boolean enabled(String attach) throws Exception {
		attach = getAttach(attach);

		Object[] obj = (Object[]) guiObject(attach);
		Method method = ((Class) obj[0]).getMethod("isEnabled");
		String ret = (String) method.invoke(obj[1]);
		ret = ret.toUpperCase();
		return ret == "TRUE" || ret == "1";
	}

	@Override
	public boolean disabled(String attach) throws Exception {
		throw new Exception("[Disabled]: Not implemented");
	}

	@Override
	public boolean checked(String attach) throws Exception {
		throw new Exception("[Checked]: Not implemented");
	}

	@Override
	public void openApp(String appText) throws Exception {
		try {
			JemmyProperties.setCurrentDispatchingModel(JemmyProperties.ROBOT_MODEL_MASK);
			final String appText2 = appText;
			Thread thread = new Thread(new Runnable() {
				String str;

				@Override
				public void run() {
					try {
						new ClassReference(appText2).startApplication();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			});

			thread.run(); // should be start();

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void openDeviceBrowser(String url) throws Exception {
		throw new Exception("[OpenDeviceBrowser]: Not implemented");
	}

	@Override
	public void click(String objectAttach, int x, int y) throws Exception {
		try {
			// TODO Auto-generated method stub
			Class[] parTypeList = new Class[3];
			Object[] parValueList = new Object[3];

			parTypeList[0] = int.class;
			parValueList[0] = x;

			parTypeList[1] = int.class;
			parValueList[1] = y;

			parTypeList[2] = int.class;
			parValueList[2] = 1;

			objectAttach = getAttach(objectAttach);

			Object[] obj = (Object[]) guiObject(objectAttach);
			Method method = ((Class) obj[0]).getMethod("clickMouse", parTypeList);
			method.invoke(obj[1], parValueList);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void click(String objectAttach) throws Exception {
		objectAttach = getAttach(objectAttach);
		try {
			Object[] obj = (Object[]) guiObject(objectAttach);
			Method method = ((Class) obj[0]).getMethod("push");
			method.invoke(obj[1]);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void doubleClick(String objectAttach) throws Exception {
		try {
			// TODO Auto-generated method stub
			Class[] parTypeList = new Class[1];
			Object[] parValueList = new Object[1];

			parTypeList[0] = int.class;
			parValueList[0] = 2;

			objectAttach = getAttach(objectAttach);

			Object[] obj = (Object[]) guiObject(objectAttach);
			Method method = ((Class) obj[0]).getMethod("clickMouse", parTypeList);
			method.invoke(obj[1], parValueList);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void doubleClick(String objectAttach, int x, int y) throws Exception {
		try {
			// TODO Auto-generated method stub
			Class[] parTypeList = new Class[3];
			Object[] parValueList = new Object[3];

			parTypeList[0] = int.class;
			parValueList[0] = x;

			parTypeList[1] = int.class;
			parValueList[1] = y;

			parTypeList[2] = int.class;
			parValueList[2] = 2;

			objectAttach = getAttach(objectAttach);

			Object[] obj = (Object[]) guiObject(objectAttach);
			Method method = ((Class) obj[0]).getMethod("clickMouse", parTypeList);
			method.invoke(obj[1], parValueList);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void clearField(String attach) throws Exception {
		setValue(attach, "");
	}

	@Override
	public void setValue(String attach, String value) throws Exception {
		Class[] parTypeList = new Class[1];
		Object[] parValueList = new Object[1];

		attach = getAttach(attach);

		Object[] obj = (Object[]) guiObject(attach);
		Method method = ((Class) obj[0]).getMethod("setText", String.class);
		method.invoke(obj[1], value);
	}

	@Override
	public void selectValue(String attach, String tipo, String value) throws Exception {
		Class[] parTypeList = new Class[1];
		Object[] parValueList = new Object[1];

		attach = getAttach(attach);

		Object[] obj = (Object[]) guiObject(attach);
		Method method = ((Class) obj[0]).getMethod("selectItem", String.class);
		method.invoke(obj[1], value);
	}

	@Override
	public void checkBox(String Attach, Boolean checkedAction) throws Exception {
		Class[] parTypeList = new Class[1];
		Object[] parValueList = new Object[1];

		parTypeList[0] = boolean.class;
		parValueList[0] = checkedAction;

		Attach = getAttach(Attach);

		Object[] obj = (Object[]) guiObject(Attach);
		Method methodExists = ((Class) obj[0]).getMethod("isSelected");
		boolean ret = (Boolean) methodExists.invoke(obj[1]);
		if (((!ret) && checkedAction) || ((ret) && !checkedAction)) {
			Method method = ((Class) obj[0]).getMethod("setSelected", parTypeList);
			method.invoke(obj[1], parValueList);
		}
	}

	@Override
	public void setFocus(String attach) throws Exception {
		attach = getAttach(attach);

		Object[] obj = (Object[]) guiObject(attach);
		Method method = ((Class) obj[0]).getMethod("requestFocus");
		method.invoke(obj[1]);
	}

	@Override
	public void maximazedWindow(String attach) throws Exception {
		throw new Exception("[MaximizeWindow]: Not implemented");
	}

	@Override
	public void mouseMove(String attach, int x, int y) throws Exception {
		Class[] parTypeList = new Class[2];
		Object[] parValueList = new Object[2];

		parTypeList[0] = int.class;
		parValueList[0] = x;

		parTypeList[1] = int.class;
		parValueList[1] = y;

		attach = getAttach(attach);

		Object[] obj = (Object[]) guiObject(attach);
		Method method = ((Class) obj[0]).getMethod("moveMouse", parTypeList);
		method.invoke(obj[1], parValueList);
	}

	@Override
	public void closeWindow(String attach) throws Exception {
		try {
			if (attach != "") {
				JemmyProperties.setCurrentTimeout("WindowWaiter.WaitWindowTimeout", 1);
				JemmyProperties.setCurrentTimeout("DialogWaiter.WaitDialogTimeout", 1);
				JemmyProperties.setCurrentTimeout("FrameWaiter.WaitFrameTimeout", 1);
				JemmyProperties.setCurrentTimeout("ComponentOperator.WaitComponentTimeout", 1);
				mainFrame = guiObjectWindow(attach);

			}
			((JFrameOperator) mainFrame).dispose();
		} catch (Exception e) {
			try {
				((JDialogOperator) mainFrame).dispose();
			} catch (Exception e2) {
			}

		}
		JemmyProperties.setCurrentTimeout("WindowWaiter.WaitWindowTimeout", WaitComponentTimeout);
		JemmyProperties.setCurrentTimeout("DialogWaiter.WaitDialogTimeout", WaitComponentTimeout);
		JemmyProperties.setCurrentTimeout("FrameWaiter.WaitFrameTimeout", WaitComponentTimeout);
		JemmyProperties.setCurrentTimeout("ComponentOperator.WaitComponentTimeout", WaitComponentTimeout);
	}

	@Override
	public void textSelect(String attach) throws Exception {
		throw new Exception("[TextSelect]: Not implemented");
	}

	@Override
	public void selectTdTable(String attach, String Valor, String event) throws Exception {
		throw new Exception("[SelectTdTable]: Not implemented");
	}

	@Override
	public void capturePrint() throws Exception {
		throw new Exception("[CapturePrint]: Not implemented");
	}

	@Override
	public void capturePrintThread() throws Exception {
		throw new Exception("[CapturePrintThread]: Not implemented");
	}

	@Override
	public void waitForPageLoaded() throws Exception {
		throw new Exception("[WaitForPageLoaded]: Not implemented");
	}

	@Override
	public boolean waitObject(String attach, String sec) throws Exception {
		try {

			JemmyProperties.setCurrentTimeout("ComponentOperator.WaitComponentTimeout", 1);

			for (int i = 0; i < Integer.parseInt(sec); i++) {
				try {
					guiObject(attach);
					JemmyProperties.setCurrentTimeout("ComponentOperator.WaitComponentTimeout", WaitComponentTimeout);
					return true;
				} catch (Exception e) {
					Thread.sleep(1000);
				}
			}

			JemmyProperties.setCurrentTimeout("ComponentOperator.WaitComponentTimeout", WaitComponentTimeout);

			return false;
		} catch (Exception e) {
			JemmyProperties.setCurrentTimeout("ComponentOperator.WaitComponentTimeout", WaitComponentTimeout);
			return false;
		}
	}

	@Override
	public void closeExe(String application) throws Exception {
		try {
			closeWindow(application);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void drag(String attach, int x, int y) throws Exception {
		throw new Exception("[Drag]: Not implemented");
	}

	@Override
	public void scroll(String direction, int offSet, int time) throws Exception {
		throw new Exception("[Scroll]: Not implemented");
	}

	@Override
	public void scroll(String attach, String direction, int offSet, int time) throws Exception {
		throw new Exception("[Scroll]: Not implemented");
	}

	@Override
	public String getAttribute(String attach, String attribute) throws Exception {
		throw new Exception("[GetAttribute]: Not implemented");
	}

	@Override
	public void sendKeys(String attach, String keys) throws Exception {
		throw new Exception("[SendKeys]: Not implemented");
	}

	@Override
	public void closeBrowser() throws Exception {
		throw new Exception("[CloseBrowser]: Not implemented");
	}

	@Override
	public void dragAndDrop() throws Exception {
		throw new Exception("[DragAndDrop]: Not implemented");
	}

	@Override
	public void scrollToObject(String attachStr) throws Exception {
		throw new Exception("[ScrollToObject]: Not implemented");
	}

	@Override
	public String captureCss(String attach, String attribute) throws Exception {
		throw new Exception("[CaptureCss]: Not implemented");
	}

	@Override
	public String captureCssColor(String attach, String attribute) throws Exception {
		throw new Exception("[CaptureCssColor]: Not implemented");
	}

	@Override
	public Object getDataGrid(String starcAttach) throws Exception {
		throw new Exception("[GetDataGrid]: Not implemented");
	}

	@Override
	public WebElement ReturnElement(String attach) throws Exception {
		throw new Exception("[ReturnElement]: Not implemented");
	}

	@Override
	public WebElement returnElement(WebElement obj, String attach, String type) throws Exception {
		throw new Exception("[ReturnElement]: Not implemented");
	}

	@Override
	public List<WebElement> returnList(String attach) throws Exception {
		throw new Exception("[ReturnList]: Not implemented");
	}

	@Override
	public void switchTab() throws Exception {
		throw new Exception("[SwitchTab]: Not implemented");
	}

	@Override
	public String getLocation(String attach) throws Exception {
		throw new Exception("[GetLocation]: Not implemented");
	}

	@Override
	public void toURL(String url) throws Exception {
		throw new Exception("[ToURL]: Not implemented");
	}

	@Override
	public String captureSelectedOption(String attach) throws Exception {
		throw new Exception("[CaptureSelectedOption]: Not implemented");
	}

	@Override
	public List<WebElement> returnSelectOptions(String attach) throws Exception {
		throw new Exception("[ReturnSelectOptions]: Not implemented");
	}

	@Override
	public int returnSize(String attach) throws Exception {
		throw new Exception("[ReturnSize]: Not implemented");
	}

	@Override
	public void pinchZoom() throws Exception {
		throw new Exception("[PinchZoom]: Not implemented");
	}

	@Override
	public boolean setIframe(String attach) throws Exception {
		throw new Exception("[SetIframe]: Not implemented");
	}

	@Override
	public void switchTo(String id) throws Exception {
		throw new Exception("[SwitchTo]: Not implemented");
	}

	@Override
	public void switchTo() throws Exception {
		throw new Exception("[SwitchTo]: Not implemented");
	}

	@Override
	public void waitVisibilityOfObject(String attach, boolean visibility) throws Exception {
		throw new Exception("[WaitVisibilityOfObject]: Not implemented");
	}

	@Override
	public void switchTab(Integer tabIndex) throws Exception {
		throw new Exception("[SwitchTab]: Not implemented");
	}

	@Override
	public void highlight(String attach) throws Exception {
		throw new Exception("[Highlight]: Not implemented");
	}

	@Override
	public BufferedImage takeScreenshot(String printPath) throws Exception {
		return new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
	}

	@Override
	public boolean isAlertPresent() throws Exception {
		throw new Exception("[IsAlertPresent]: Not implemented");
	}

	@Override
	public void closeDriver() {
		// TODO Auto-generated method stub
	}

	@Override
	public void checkAlert(String value) throws Exception {
		throw new Exception("[CheckAlert]: Not implemented");
	}

	@Override
	public Object guiObject(String stringObj) throws Exception {
		try {
			String[] stringObjs = stringObj.split("\\.");

			if (stringObjs.length > 1) {
				String methodParameters = stringObj.substring(stringObj.indexOf("(") + 1, stringObj.indexOf(")"))
						.replace("\"", "");
				String[] parametersArray = methodParameters.split(",");
				if (stringObjs[0].toUpperCase().contains("JDIALOGOPERATOR")) {
					if (parametersArray.length == 0)
						mainFrame = new JDialogOperator(methodParameters);
					else if (parametersArray.length == 2)
						mainFrame = new JDialogOperator(parametersArray[0], Integer.parseInt(parametersArray[1]));
				} else {
					if (parametersArray.length == 0)
						mainFrame = new JFrameOperator(methodParameters);
					else if (parametersArray.length == 2)
						mainFrame = new JFrameOperator(parametersArray[0], Integer.parseInt(parametersArray[1]));
				}
				return guiObjectChild(stringObjs[1]);
			} else {
				return guiObjectChild(stringObj);
			}
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void acessaMenu(String locator) throws Exception {
		throw new Exception("[AcessaMenu]: Not Implemented");
	}

	@Override
	public String getAllTextFromPage() throws Exception {
		throw new Exception("[getAllTextFromPage]: Not Implemented");
	}

	@Override
	public void setWindowHandled() throws Exception {
		throw new Exception("[SetWindowHandled]: Not Implemented");
	}

	@Override
	public void waitObjectToVanish(String locator, int timeOutInSeconds) throws Exception {
		throw new Exception("[SetWindowHandled]: Not Implemented");
	}

	@Override
	public List<WebElement> getElements(String locator) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveFile() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void switchWindow() throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void WaitObjectVisibility(String locator, int timeOutInSeconds) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void acceptAlert() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void acceptAlert(String printPath) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void hover(String locator) throws Exception {
		throw new Exception("[CaptureCss]: Not implemented");
	}

	@Override
	public boolean exists(String Attach, int timeout) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void closeWindow() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void switchWindowSinistro() throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean switchWindowEfetivacao() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void switchWindowSinistro2() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void getShadowRootElement(String locator) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void releaseShadowRootElement() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public byte[] takeScreenshotForCucumberReport() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void submit(String locator) throws Exception {
		// TODO Auto-generated method stub

	}

}
