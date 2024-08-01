package framework.testtools;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import org.openqa.selenium.WebElement;

import com.jacob.com.LibraryLoader;

import autoitx4java.AutoItX;

public class AutoItFunctions implements ITestToolFunctions {

	class AutoItObj {
		public String title;
		public String text;
		public String controlID;

		public void setAutoItObj(String title, String text, String controlID) {
			this.title = title;
			this.text = text;
			this.controlID = controlID;
		}

		public void captureAttach(String locator) {
			// this.title=Utils.CaptureProp(locator, "TITLE");
			// this.text=Utils.CaptureProp(locator, "TEXT");
			// this.controlID=Utils.CaptureProp(locator, "CONTROLID");
		}
	}

	private final AutoItObj autoItObj = new AutoItObj();
	private AutoItX autoIt;

	public AutoItFunctions() {
		try {
			if (autoIt == null) {
				File file = new File("lib", "jacob-1.19-x64.dll"); // path to the jacob dll
				System.setProperty(LibraryLoader.JACOB_DLL_PATH, file.getAbsolutePath());

				autoIt = new AutoItX();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void ConfigAutoIt(String locator) {
		autoItObj.captureAttach(locator);
		autoIt.winWait(autoItObj.title);
		autoIt.winActivate(autoItObj.title);
	}

	@Override
	public Object guiObject(String locator) throws Exception {
		throw new Exception("[GuiObject]: Not implemented");
	}

	@Override
	public void attach() throws Exception {
		throw new Exception("[Attach]: Not implemented");
	}

	@Override
	public String captureCss(String locator) throws Exception {
		throw new Exception("[CaptureCss]: Not implemented");
	}

	@Override
	public String captureCssColor(String locator) throws Exception {
		throw new Exception("[CaptureCssColor]: Not implemented");
	}

	@Override
	public String captureText(String locator) throws Exception {
		ConfigAutoIt(locator);
		return autoIt.controlGetText(autoItObj.title, autoItObj.text, autoItObj.controlID);
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
	public boolean visible(String locator) throws Exception {
		ConfigAutoIt(locator);
		return autoIt.controlCommandIsVisible(autoItObj.title, autoItObj.text, autoItObj.controlID);
	}

	@Override
	public boolean exists(String locator) throws Exception {
		ConfigAutoIt(locator);
		if (autoItObj.controlID.equals("") && autoItObj.text.equals(""))
			return autoIt.winExists(autoItObj.title);
		else
			return autoIt.controlCommandIsVisible(autoItObj.title, autoItObj.text, autoItObj.controlID);
	}

	@Override
	public boolean enabled(String locator) throws Exception {
		ConfigAutoIt(locator);
		return autoIt.controlCommandIsEnabled(autoItObj.title, autoItObj.text, autoItObj.controlID);
	}

	@Override
	public boolean disabled(String locator) throws Exception {
		ConfigAutoIt(locator);
		return !autoIt.controlCommandIsEnabled(autoItObj.title, autoItObj.text, autoItObj.controlID);
	}

	@Override
	public boolean checked(String locator) throws Exception {
		ConfigAutoIt(locator);
		return !autoIt.controlCommandIsChecked(autoItObj.title, autoItObj.text, autoItObj.controlID);
	}

	@Override
	public void openApp(String app) throws Exception {
		Runtime.getRuntime().exec(app);
	}

	@Override
	public void openDeviceBrowser(String url) throws Exception {
		throw new Exception("[OpenDeviceBrowser]: Not implemented");
	}

	@Override
	public void click(String locator, int x, int y) throws Exception {
		ConfigAutoIt(locator);
		if (x > 0 && y > 0)
			autoIt.controlClick(autoItObj.title, autoItObj.text, autoItObj.controlID, "", 1, x, y);
		else
			click(locator);
	}

	@Override
	public void click(String locator) throws Exception {
		ConfigAutoIt(locator);
		autoIt.controlClick(autoItObj.title, autoItObj.text, autoItObj.controlID);
	}

	@Override
	public void doubleClick(String locator) throws Exception {
		ConfigAutoIt(locator);
		autoIt.controlClick(autoItObj.title, autoItObj.text, autoItObj.controlID, "", 2);
	}

	@Override
	public void doubleClick(String locator, int x, int y) throws Exception {
		ConfigAutoIt(locator);

		if (x > 0 && y > 0)
			autoIt.controlClick(autoItObj.title, autoItObj.text, autoItObj.controlID, "", 2, x, y);
		else
			doubleClick(locator);
	}

	@Override
	public void clearField(String locator) throws Exception {
		setValue(locator, "");
	}

	@Override
	public void setValue(String locator, String value) throws Exception {
		ConfigAutoIt(locator);
		autoIt.ControlSetText(autoItObj.title, autoItObj.text, autoItObj.controlID, value);
	}

	@Override
	public void selectValue(String locator, String tipo, String value) throws Exception {
		ConfigAutoIt(locator);
		autoIt.ControlSetText(autoItObj.title, autoItObj.text, autoItObj.controlID, value);

		autoIt.controlSend(autoItObj.title, autoItObj.text, autoItObj.controlID, "{DOWN}");
	}

	@Override
	public void checkBox(String locator, Boolean checkedAction) throws Exception {
		ConfigAutoIt(locator);
		if (autoIt.controlCommandIsChecked(autoItObj.title, autoItObj.text, autoItObj.controlID) != checkedAction)
			autoIt.controlClick(autoItObj.title, autoItObj.text, autoItObj.controlID);
	}

	@Override
	public void setFocus(String locator) throws Exception {
		ConfigAutoIt(locator);
		autoIt.controlFocus(autoItObj.title, autoItObj.text, autoItObj.controlID);
	}

	@Override
	public void maximazedWindow(String locator) throws Exception {
		ConfigAutoIt(locator);
		autoIt.winMinimizeAll();
	}

	@Override
	public void mouseMove(String locator, int x, int y) throws Exception {
		ConfigAutoIt(locator);
		autoIt.mouseMove(x, y);
	}

	@Override
	public void closeWindow(String locator) throws Exception {
		ConfigAutoIt(locator);
		autoIt.winClose(autoItObj.title);
	}

	@Override
	public void textSelect(String locator) throws Exception {
		ConfigAutoIt(locator);
		autoIt.controlGetText(autoItObj.title, autoItObj.text, autoItObj.controlID);
	}

	@Override
	public void selectTdTable(String locator, String Valor, String event) throws Exception {
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
	public boolean waitObject(String locator, String sec) throws Exception {
		for (int i = 0; i < Integer.parseInt(sec); i++) {
			if (exists(locator))
				return true;
			Thread.sleep(1000);
		}
		return false;
	}

	@Override
	public void closeExe(String application) throws Exception {
		closeWindow(application);
	}

	@Override
	public void drag(String locator, int x, int y) throws Exception {
		throw new Exception("[Drag]: Not implemented");
	}

	@Override
	public void scroll(String direction, int offSet, int time) throws Exception {
		throw new Exception("[Scroll]: Not implemented");
	}

	@Override
	public void scroll(String locator, String direction, int offSet, int time) throws Exception {
		throw new Exception("[Scroll]: Not implemented");
	}

	@Override
	public String getAttribute(String locator, String attribute) throws Exception {
		throw new Exception("[GetAttribute]: Not implemented");
	}

	@Override
	public void sendKeys(String locator, String keys) throws Exception {
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
	public void scrollToObject(String locatorStr) throws Exception {
		throw new Exception("[ScrollToObject]: Not implemented");
	}

	@Override
	public String captureCss(String locator, String attribute) throws Exception {
		throw new Exception("[CaptureCss]: Not implemented");
	}

	@Override
	public String captureCssColor(String locator, String attribute) throws Exception {
		throw new Exception("[CaptureCssColor]: Not implemented");
	}

	@Override
	public Object getDataGrid(String starcAttach) throws Exception {
		throw new Exception("[GetDataGrid]: Not implemented");
	}

	@Override
	public WebElement ReturnElement(String locator) throws Exception {
		throw new Exception("[ReturnElement]: Not implemented");
	}

	@Override
	public WebElement returnElement(WebElement obj, String locator, String type) throws Exception {
		throw new Exception("[ReturnElement]: Not implemented");
	}

	@Override
	public List<WebElement> returnList(String locator) throws Exception {
		throw new Exception("[ReturnList]: Not implemented");
	}

	@Override
	public void switchTab() throws Exception {
		throw new Exception("[SwitchTab]: Not implemented");
	}

	@Override
	public String getLocation(String locator) throws Exception {
		throw new Exception("[GetLocation]: Not implemented");
	}

	@Override
	public void toURL(String string) throws Exception {
		throw new Exception("[ToURL]: Not implemented");
	}

	@Override
	public String captureSelectedOption(String locator) throws Exception {
		throw new Exception("[CaptureSelectedOption]: Not implemented");
	}

	@Override
	public List<WebElement> returnSelectOptions(String locator) throws Exception {
		throw new Exception("[ReturnSelectOptions]: Not implemented");
	}

	@Override
	public int returnSize(String locator) throws Exception {
		throw new Exception("[ReturnSize]: Not implemented");
	}

	@Override
	public void pinchZoom() throws Exception {
		throw new Exception("[PinchZoom]: Not implemented");
	}

	@Override
	public boolean setIframe(String locator) throws Exception {
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
	public void waitVisibilityOfObject(String locator, boolean visibility) throws Exception {
		throw new Exception("[WaitVisibilityOfObject]: Not implemented");
	}

	@Override
	public void switchTab(Integer tabIndex) throws Exception {
		throw new Exception("[SwitchTab]: Not implemented");
	}

	@Override
	public void highlight(String locator) throws Exception {
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