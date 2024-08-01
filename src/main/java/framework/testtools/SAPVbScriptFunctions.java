package framework.testtools;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import org.openqa.selenium.WebElement;
import framework.dataProviders.ConfigFileReader;

public class SAPVbScriptFunctions implements ITestToolFunctions {

	private String executeCommandSAP(String commandName,String attach,String value) throws Exception
	{
		return executeCommandSAP(commandName,attach,value,0, 0);
	}
	private String executeCommandSAP(String commandName,String attach,String value,int x, int y) throws Exception
	{
		ConfigFileReader reader = new ConfigFileReader("configs/config.properties");
		String dirDefaultFolder = reader.getPropertyByKey("dirDefault");
		
		Process p =  Runtime.getRuntime().exec("wscript " + dirDefaultFolder + "SAP\\SAPCommand.vbs" + " \"" + commandName + "\" \"" + attach + "\" \"" + value +  "\" \"" + x + "\" \"" + y + "\"");
		BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));

	    // Do something with stream, read etc.
	    String line;
	    while (!(line = is.readLine()).contains("<END_SAP>"))
	    {
	    	if(line != null && line != "")
	    	{
		    	if(line.contains("<ERROR_SAP_SCRIPT>"))
		    		throw new Exception(line.replace("<ERROR_SAP_SCRIPT>", "").replace("<END_SAP>", ""));
		    	else
		    		return line.replace("<END_SAP>", "");
	    	}
	    	Thread.sleep(100);
	    }
	    return "";
	}
	
	@Override
	public Object guiObject(String attach) throws Exception {
		throw new Exception("[GuiObject]: Not implemented");
	}

	@Override
	public void attach() throws Exception {
		throw new Exception("[Attach]: Not implemented");
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
		return executeCommandSAP("gettext", "", "");
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
		return executeCommandSAP("visible", Attach, "")=="true";
	}

	@Override
	public boolean exists(String Attach) throws Exception {
		return executeCommandSAP("exists", Attach, "")=="true";
	}

	@Override
	public boolean enabled(String attach) throws Exception {
		return executeCommandSAP("enabled", attach, "")=="true";
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
	public void openApp(String app) throws Exception {
		Runtime.getRuntime().exec(app);
	}

	@Override
	public void openDeviceBrowser(String url) throws Exception {
		throw new Exception("[OpenDeviceBrowser]: Not implemented");
	}

	@Override
	public void click(String objectAttach, int x, int y) throws Exception {
		executeCommandSAP("click", objectAttach, "");		
	}

	@Override
	public void click(String objectAttach) throws Exception {
		click(objectAttach,0,0);
	}

	@Override
	public void doubleClick(String objectAttach) throws Exception {
		doubleClick(objectAttach,0,0);
	}

	@Override
	public void doubleClick(String objectAttach, int x, int y) throws Exception {
		executeCommandSAP("doubleclick", objectAttach, "");
	}

	@Override
	public void clearField(String attach) throws Exception {
		setValue(attach, "");
	}

	@Override
	public void setValue(String objectAttach, String value) throws Exception {
		executeCommandSAP("settext", objectAttach, value);
	}

	@Override
	public void selectValue(String attach, String tipo, String value) throws Exception {
		executeCommandSAP("select", attach, value);
	}

	@Override
	public void checkBox(String Attach, Boolean checkedAction) throws Exception {
		executeCommandSAP("checkbox", Attach, checkedAction.toString());
	}

	@Override
	public void setFocus(String attach) throws Exception {
		executeCommandSAP("setfocus", attach, "");
	}

	@Override
	public void maximazedWindow(String attach) throws Exception {
		executeCommandSAP("maximize", attach, "");
	}

	@Override
	public void mouseMove(String attach, int x, int y) throws Exception {
		executeCommandSAP("mousemove", attach, "",x,y);
	}

	@Override
	public void closeWindow(String attach) throws Exception {
		executeCommandSAP("closewindow", attach, "");
	}

	@Override
	public void textSelect(String attach) throws Exception {
		throw new Exception("[TextSelect]: Not Implemented");
	}

	@Override
	public void selectTdTable(String attach, String Valor, String event) throws Exception {
		throw new Exception("[SelectTdTable]: Not Implemented");
	}

	@Override
	public void capturePrint() throws Exception {
		throw new Exception("[CapturePrint]: Not Implemented");
	}

	@Override
	public void capturePrintThread() throws Exception {
		throw new Exception("[CapturePrintThread]: Not Implemented");
	}

	@Override
	public void waitForPageLoaded() throws Exception {
		throw new Exception("[WaitForPageLoaded]: Not Implemented");
	}

	@Override
	public boolean waitObject(String attach, String sec) throws Exception {
		for (int i = 0; i < Integer.parseInt(sec); i++) 
		{
			if(exists(attach))
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
	public void drag(String attach, int x, int y) throws Exception {
		throw new Exception("[Drag]: Not Implemented");
	}

	@Override
	public void scroll(String direction, int offSet, int time) throws Exception {
		throw new Exception("[Scroll]: Not Implemented");
	}

	@Override
	public void scroll(String attach, String direction, int offSet, int time) throws Exception {
		throw new Exception("[Scroll]: Not Implemented");
	}

	@Override
	public String getAttribute(String attach, String attribute) throws Exception {
		throw new Exception("[GetAttribute]: Not Implemented");
	}

	@Override
	public void sendKeys(String attach, String keys) throws Exception {
		throw new Exception("[SendKeys]: Not Implemented");
	}

	@Override
	public void closeBrowser() throws Exception {
		throw new Exception("[CloseBrowser]: Not Implemented");
	}

	@Override
	public void dragAndDrop() throws Exception {
		throw new Exception("[DragAndDrop]: Not Implemented");
	}

	@Override
	public void scrollToObject(String attachStr) throws Exception {
		throw new Exception("[ScrollToObject]: Not Implemented");
	}

	@Override
	public String captureCss(String attach, String attribute) throws Exception {
		throw new Exception("[CaptureCss]: Not Implemented");
	}
	@Override
	public String captureCssColor(String attach, String attribute) throws Exception {
		throw new Exception("[CaptureCssColor]: Not Implemented");
	}

	@Override
	public Object getDataGrid(String starcAttach) throws Exception {
		throw new Exception("[GetDataGrid]: Not Implemented");
	}

	@Override
	public WebElement ReturnElement(String attach) throws Exception {
		throw new Exception("[ReturnElement]: Not Implemented");
	}

	@Override
	public WebElement returnElement(WebElement obj, String attach, String type) throws Exception {
		throw new Exception("[ReturnElement]: Not Implemented");
	}
	
	@Override
	public List<WebElement> returnList(String attach) throws Exception {
		throw new Exception("[ReturnList]: Not Implemented");
	}

	@Override
	public void switchTab() throws Exception {
		throw new Exception("[SwitchTab]: Not Implemented");
	}

	@Override
	public String getLocation(String attach) throws Exception {
		throw new Exception("[GetLocation]: Not Implemented");
	}

	@Override
	public void toURL(String string) throws Exception {
		throw new Exception("[ToURL]: Not Implemented");
	}

	@Override
	public String captureSelectedOption(String attach) throws Exception {
		throw new Exception("[CaptureSelectedOption]: Not Implemented");
	}

	@Override
	public List<WebElement> returnSelectOptions(String attach) throws Exception {
		throw new Exception("[ReturnSelectOptions]: Not Implemented");
	}

	@Override
	public int returnSize(String attach) throws Exception {
		throw new Exception("[ReturnSize]: Not Implemented");
	}

	@Override
	public void pinchZoom() throws Exception {
		throw new Exception("[PinchZoom]: Not Implemented");
	}

	@Override
	public boolean setIframe(String attach) throws Exception {
		throw new Exception("[SetIframe]: Not Implemented");
	}

	@Override
	public void switchTo(String id) throws Exception {
		throw new Exception("[SwitchTo]: Not Implemented");
	}

	@Override
	public void switchTo() throws Exception {
		throw new Exception("[SwitchTo]: Not Implemented");
	}

	@Override
	public void waitVisibilityOfObject(String attach, boolean visibility) throws Exception {
		throw new Exception("[WaitVisibilityOfObject]: Not Implemented");
	}

	@Override
	public void switchTab(Integer tabIndex) throws Exception {
		throw new Exception("[SwitchTab]: Not Implemented");
	}

	@Override
	public void highlight(String attach) throws Exception {
		throw new Exception("[Highlight]: Not Implemented");
	}

	@Override
	public BufferedImage takeScreenshot(String printPath) throws Exception {		
		return new Robot().createScreenCapture(
			new Rectangle(Toolkit.getDefaultToolkit().getScreenSize())
		);
	}

	@Override
	public boolean isAlertPresent() throws Exception {
		throw new Exception("[IsAlertPresent]: Not Implemented");
	}

	@Override
	public void closeDriver() {
		// TODO Auto-generated method stub
	}

	@Override
	public void checkAlert(String value) throws Exception {
		throw new Exception("[CheckAlert]: Not Implemented");
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
	

}
