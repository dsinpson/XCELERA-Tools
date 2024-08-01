package framework.testtools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;

import framework.data.entities.Capabilities;
import framework.testtools.utils.SeleniumUtils;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import org.openqa.selenium.Dimension;

import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;

public class AppiumFunctions implements ITestToolFunctions {

	private AppiumDriver<WebElement> driver;
	private DesiredCapabilities capabilities;
	private final List<Capabilities> __listCapabilities;
	private final Map<String, String> _executionConfigs;

	private final int TIME_OUT = 30; // segundos

	private boolean switchTab;

	public AppiumDriver<WebElement> getDriver() {
		return this.driver;
	}

	public AppiumFunctions(List<Capabilities> listCapabilities, Map<String, String> executionConfigs) {
		__listCapabilities = listCapabilities;
		_executionConfigs = executionConfigs;
	}

	private void setMobileConfiguration(String deviceStarcName) throws Exception {

		capabilities = new DesiredCapabilities();

		for (Capabilities item : __listCapabilities) {
			capabilities.setCapability(item.CapabilitiesName, item.Value);
		}
	}

	public WebElement FindObject(String locator) throws Exception {
		WebElement element = SeleniumUtils.findObject(driver, locator, null, TIME_OUT);
		if (element == null)
			throw new Exception("Element " + locator + " not found");

		return element;
	}

	public List<WebElement> FindObjects(String locator) throws Exception {
		List<WebElement> elements = SeleniumUtils.findObjects(driver, locator, null, TIME_OUT);
		if (elements == null)
			throw new Exception("Element " + locator + " not found");

		return elements;
	}

	public WebElement FindObjectClickable(String locator) throws Exception {
		WebElement element = SeleniumUtils.findObjectClickable(driver, locator, null, TIME_OUT);
		if (element == null)
			throw new Exception("Element " + locator + " not found");

		return element;
	}

	@Override
	public Object guiObject(String locator) throws Exception {
		Object obj = null;
		try {
			obj = FindObject(locator);
			return obj;
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	@Override
	public void attach() throws Exception {
		throw new Exception("[Attach]: Not implemented");
	}

	@Override
	public String captureCss(String locator) throws Exception {
		WebElement element = FindObject(locator);

		if (element.isDisplayed()) {
			if (!(element.getAttribute("style") == null || element.getAttribute("style") == "")) {
				return element.getAttribute("style");
			} else {
				return element.getCssValue("style");
			}
		} else
			return "";
	}

	@Override
	public String captureCssColor(String locator) throws Exception {
		WebElement element = FindObject(locator);

		if (element.isDisplayed()) {
			if (!(element.getAttribute("style") == null || element.getAttribute("style") == "")) {
				return element.getAttribute("style");
			} else {
				return element.getCssValue("style");
			}
		} else
			return "";
	}

	@Override
	public String captureText(String locator) throws Exception {
		WebElement element = FindObject(locator);

		if (element.isDisplayed()) {
			if (!(element.getText() == null || element.getText().equals("")))
				return element.getText();
			else if (!(element.getAttribute("value") == null || element.getAttribute("value").equals("")))
				return element.getAttribute("value");
			else
				return "";
		} else
			return "";
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
		WebElement element = FindObject(locator);

		if (element.isDisplayed())
			return true;

		else if (exists(locator)) {

			scrollToObject(locator);
			return element.isDisplayed();

		} else
			return false;

	}

	@Override
	public boolean exists(String locator) throws Exception {
		try {
			WebElement element = FindObject(locator);
			if (element == null)
				return false;

			return element.getSize().getWidth() != 0;

		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean exists(String Attach, int timeout) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean enabled(String locator) throws Exception {
		WebElement element = FindObject(locator);

		if (element.isDisplayed()) {
			return element.isEnabled();
		} else
			return false;
	}

	@Override
	public boolean disabled(String locator) throws Exception {
		WebElement element = FindObject(locator);

		return visible(locator) && !element.isEnabled();
	}

	@Override
	public boolean checked(String locator) throws Exception {
		WebElement element = FindObject(locator);

		if (element.isDisplayed())
			return element.isSelected();
		else
			return false;
	}

	@Override
	public void openApp(String app) throws Exception {
		try {
			setMobileConfiguration("");

			String mobileOS = _executionConfigs.get("Mobile.OperationalSystem");

			switch (mobileOS) {
			case "Android":
				capabilities.setCapability("plataformName", _executionConfigs.get("Mobile.Capabilities.platformName"));
				capabilities.setCapability("plataformVersion",
						_executionConfigs.get("Mobile.Capabilities.platformVersion"));
				capabilities.setCapability("deviceName", _executionConfigs.get("Mobile.Capabilities.deviceName"));
				capabilities.setCapability("udid", _executionConfigs.get("Mobile.Capabilities.udid"));
				capabilities.setCapability("autoGrantPermissions", true);
				capabilities.setCapability("automationName", "UiAutomator2");
				capabilities.setCapability("avdArgs", "-no-window");
				driver = new AndroidDriver<WebElement>(new URL("http://localhost:4723/wd/hub"), capabilities);

				break;
			case "iOS":
				driver = new IOSDriver<WebElement>(new URL("http://localhost:4723/wd/hub"), capabilities);

				break;
			default:
				System.out.println("No browser default found.");
				break;
			}

			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			Thread.sleep(2000);

		} catch (Exception e) {
			e.printStackTrace();
			driver.quit();
		}
	}

	@Override
	public void openDeviceBrowser(String url) throws Exception {
		throw new Exception("[OpenDeviceBrowser]: Not implemented");

	}

	@Override
	public void click(String locator, int x, int y) throws Exception {
		WebElement element = FindObject(locator);

		if (x != 0 && y != 0) {
			Actions actions = new Actions(driver);
			actions.moveToElement(element, x, y).click().perform();
		} else
			click(locator);
	}

	@Override
	public void click(String locator) throws Exception {
		WebElement element = FindObject(locator);

		try {
			element.click();
		} catch (Exception e) {
			JavascriptExecutor jse = driver;
			jse.executeScript("arguments[0].click();", element);
		}
	}

	@Override
	public void doubleClick(String locator) throws Exception {
		WebElement element = FindObject(locator);

		Actions actions = new Actions(driver);
		actions.doubleClick(element).perform();
	}

	@Override
	public void doubleClick(String locator, int x, int y) throws Exception {
		WebElement element = FindObject(locator);

		if (x != 0 && y != 0) {
			Actions actions = new Actions(driver);
			actions.moveToElement(element, x, y).doubleClick().perform();
		} else
			doubleClick(locator);
	}

	@Override
	public void clearField(String locator) throws Exception {
		WebElement element = FindObject(locator);
		element.clear();
	}

	@Override
	public void setValue(String locator, String value) throws Exception {
		WebElement element = FindObject(locator);

		element.clear();
		element.sendKeys(value);
	}

	@Override
	public void selectValue(String locator, String tipo, String value) throws Exception {

		WebElement element = FindObject(locator);
		switch (tipo.toLowerCase()) {
		case "text":
			new Select(element).selectByVisibleText(value);
			break;
		case "value":
			new Select(element).selectByValue(value);
			break;
		default:
			new Select(element).selectByVisibleText(value);
		}
	}

	@Override
	public void checkBox(String locator, Boolean checkedAction) throws Exception {
		WebElement element = FindObject(locator);

		if (checkedAction)
			if (!element.isSelected())
				element.click();
			else if (element.isSelected())
				element.click();
	}

	@Override
	public void setFocus(String locator) throws Exception {
		WebElement element = FindObject(locator);

		Actions actions = new Actions(driver);
		actions.moveToElement(element).perform();
	}

	@Override
	public void maximazedWindow(String attach) throws Exception {
		driver.manage().window().maximize();
	}

	@Override
	public void mouseMove(String locator, int x, int y) throws Exception {
		if (locator.isEmpty()) {
			Robot robot = new Robot();
			robot.mouseMove(x, y);

			return;
		}

		WebElement element = FindObject(locator);

		Actions actions = new Actions(driver);
		actions.moveToElement(element).build().perform();
	}

	@Override
	public void closeWindow(String attach) throws Exception {
		throw new Exception("[CloseWindow]: Not implemented");
	}

	@Override
	public void closeWindow() throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void textSelect(String locator) throws Exception {
		WebElement element = FindObject(locator);

		element.getAttribute("text");
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
		driver.manage().timeouts().pageLoadTimeout(TIME_OUT, TimeUnit.SECONDS);
	}

	@Override
	public boolean waitObject(String locator, String sec) throws Exception {

		int intPause;
		if (sec == "")
			sec = "0";

		intPause = 0;

		while (!exists(locator)) {
			intPause++;
			Thread.sleep(1000);
			if (intPause == Integer.parseInt(sec))
				break;
		}

		return exists(locator);
	}

	@Override
	public void closeExe(String application) throws Exception {
		throw new Exception("[CloseExe]: Not implemented");
	}

	@Override
	public void drag(String attach, int x, int y) throws Exception {
		throw new Exception("[Drag]: Not implemented");
	}

	@Override
	public void scroll(String direction, int offSet, int time) throws Exception {
		String cont;
		cont = driver.getContext();
		System.out.println(cont);
		Dimension size;
		driver.context("NATIVE_APP");
		size = driver.manage().window().getSize();
		System.out.println(size);
		int starty;
		int endy;

		switch (direction.toLowerCase()) {
		case "up":
			endy = (int) (size.height * 0.80);
			starty = endy - size.height / 4;
			break;
		case "down":
			starty = (int) (size.height * 0.80);
			endy = starty - size.height / 4;
			break;
		default:
			starty = (int) (size.height * 0.80);
			endy = starty - size.height / 4;
			break;
		}

		if (direction.equals("up")) {
			for (int i = 0; i < 3; i++) {
				driver.context("NATIVE_APP");
				driver.context(cont);
			}
		} else {
			for (int i = 0; i < 3; i++) {
				driver.context("NATIVE_APP");
				driver.context(cont);
			}
		}
	}

	@Override
	public void scroll(String locator, String direction, int offSet, int time) throws Exception {
		WebElement element = FindObject(locator);

		new Actions(driver).moveToElement(element).build().perform();
	}

	@Override
	public String getAttribute(String locator, String attribute) throws Exception {
		WebElement element = FindObject(locator);
		return element.getAttribute(attribute);
	}

	@Override
	public void sendKeys(String locator, String keys) throws Exception {
		WebElement element = FindObject(locator);
		element.sendKeys(keys);
	}

	@Override
	public void closeBrowser() throws Exception {
		Runtime.getRuntime().exec("taskkill /f /im chromedriver.exe /t").waitFor();
	}

	@Override
	public void dragAndDrop() throws Exception {
		String cont;
		cont = driver.getContext();
		System.out.println(cont);
		Dimension size;
		driver.context("NATIVE_APP");
		size = driver.manage().window().getSize();
		System.out.println(size);

		driver.context("NATIVE_APP");
		System.out.println(cont);
		driver.context(cont);
		System.out.println(cont);
	}

	@Override
	public void scrollToObject(String locator) throws Exception {
		WebElement element = FindObject(locator);

		String cont;
		cont = driver.getContext();

		driver.context("NATIVE_APP");

		driver.context(cont);

		while (!element.isDisplayed()) {
			driver.context("NATIVE_APP");
			System.out.println(cont);

			driver.context(cont);
			System.out.println(cont);
		}

		driver.context(cont);
	}

	@Override
	public String captureCss(String locator, String attribute) throws Exception {
		WebElement element = FindObject(locator);

		if (attribute.equals("Color"))
			return element.getCssValue("color");
		else if (attribute.equals("bgColor")) {
			String s = element.getCssValue("background-color");
			String[] cor = new String[10];
			int i = 0;

			s = s.substring(s.indexOf("(") + 1, s.indexOf(")"));
			StringTokenizer st = new StringTokenizer(s);

			while (st.hasMoreElements()) {
				cor[i] = st.nextToken(",").trim();
				i++;
			}

			s = "";
			for (int j = 0; j < 3; j++) {
				s += "" + cor[j];
			}

			return s;
		} else
			return "";
	}

	@Override
	public String captureCssColor(String locator, String attribute) throws Exception {
		WebElement element = FindObject(locator);

		if (attribute.equals("Color"))
			return element.getCssValue("color");
		else if (attribute.equals("bgColor")) {
			String s = element.getCssValue("background-color");
			String[] cor = new String[10];
			int i = 0;

			s = s.substring(s.indexOf("(") + 1, s.indexOf(")"));
			StringTokenizer st = new StringTokenizer(s);

			while (st.hasMoreElements()) {
				cor[i] = st.nextToken(",").trim();
				i++;
			}

			s = "";
			for (int j = 0; j < 3; j++) {
				s += "" + cor[j];
			}

			return s;
		} else
			return "";
	}

	@Override
	public Object getDataGrid(String locator) throws Exception {
		WebElement table_element = FindObject(locator);

		List<WebElement> tr_collection = table_element.findElements(By.xpath(locator + "/tbody/tr"));

		String result = "";
		for (WebElement trElement : tr_collection) {
			try {
				List<WebElement> td_collection = trElement.findElements(By.xpath("td"));
				List<WebElement> th_collection = trElement.findElements(By.xpath("th"));

				if (td_collection.size() != 0 && th_collection.size() != 0) {
					WebElement tdElement = td_collection.get(0);
					WebElement thElement = th_collection.get(0);
					result = result + thElement.getText() + ";" + tdElement.getText() + ";";
				} else {
					if (td_collection.size() != 0) {
						for (WebElement tdElement : td_collection)
							result = result + tdElement.getText() + " ";
					} else {
						if (th_collection.size() != 0) {
							for (WebElement thElement : th_collection)
								result = result + thElement + " ";
						}
					}
				}
			} catch (NoSuchElementException e) {
				System.out.println(e.getMessage());
			}
		}

		return result;
	}

	@Override
	public WebElement ReturnElement(String locator) throws Exception {
		return FindObject(locator);
	}

	@Override
	public WebElement returnElement(WebElement obj, String locator, String type) throws Exception {
		if (type.equals("xpath"))
			return obj.findElement(By.xpath(locator));
		else if (type.equals("id"))
			return obj.findElement(By.id(locator));
		else if (type.equals("name"))
			return obj.findElement(By.name(locator));
		else if (type.equals("cssSelector"))
			return obj.findElement(By.cssSelector(locator));
		else
			return null;
	}

	@Override
	public List<WebElement> returnList(String locator) throws Exception {
		return FindObjects(locator);
	}

	@Override
	public void switchTab() throws Exception {

		ArrayList<String> tabs2 = new ArrayList<String>(driver.getWindowHandles());

		if (switchTab) {
			driver.close();
			driver.switchTo().window(tabs2.get(0));
			switchTab = false;
		} else {
			driver.switchTo().window(tabs2.get(1));
			switchTab = true;
		}
	}

	@Override
	public String getLocation(String locator) throws Exception {
		WebElement element = FindObject(locator);
		return element.getLocation().x + ";" + element.getLocation().y;
	}

	@Override
	public void toURL(String url) throws Exception {
		driver.navigate().to(url);
	}

	@Override
	public String captureSelectedOption(String locator) throws Exception {
		WebElement element = FindObject(locator);
		return new Select(element).getFirstSelectedOption().getText();
	}

	@Override
	public List<WebElement> returnSelectOptions(String attach) throws Exception {
		throw new Exception("[ReturnSelectOptions]: Not implemented");
	}

	@Override
	public int returnSize(String locator) throws Exception {
		List<WebElement> elemnts = FindObjects(locator);
		return elemnts.size();
	}

	@Override
	public void pinchZoom() throws Exception {
		Thread.sleep(1000);
		String contex;
		contex = driver.getContext();
		driver.context("NATIVE_APP");
		int scrHeight = driver.manage().window().getSize().getHeight(); // To get the mobile screen height
		int scrWidth = driver.manage().window().getSize().getWidth();// To get the mobile screen width
		driver.context(contex);
		MultiTouchAction multiTouch = new MultiTouchAction(driver);
		System.out.println("scrWidth/2,scrHeight/2 ::::::  " + scrWidth / 2 + "," + scrHeight / 2);

		// multiTouch.add(new TouchAction<WebElement>(driver)).add(new
		// TouchAction(driver));
		multiTouch.perform();// now perf

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
		if (tabIndex == null)
			switchTab();
		else {
			driver.switchTo().window(driver.getWindowHandles().toArray()[tabIndex].toString());
		}
	}

	@Override
	public void highlight(String attach) throws Exception {
		throw new Exception("[Highlight]: Not implemented");
	}

	@Override
	public BufferedImage takeScreenshot(String printPath) throws Exception {

		byte[] image = driver.getScreenshotAs(OutputType.BYTES);

		InputStream in = new ByteArrayInputStream(image);
		BufferedImage bImageFromConvert = ImageIO.read(in);

		return bImageFromConvert;

	}

	@Override
	public boolean isAlertPresent() throws Exception {
		throw new Exception("[IsAlertPresent]: Not implemented");
	}

	@Override
	public void closeDriver() {
		if (driver != null)
			driver.quit();
	}

	@Override
	public void checkAlert(String value) throws Exception {

		Alert alert = driver.switchTo().alert();
		String alertMessage = alert.getText();

		System.out.println(">> MASSA: " + value.trim());
		System.out.println(">>  TELA: " + alertMessage);
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
		return FindObjects(locator);
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
		try {
			return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void submit(String locator) throws Exception {
		WebElement element = FindObject(locator);
		element.submit();
	}

	@Override
	public void switchWindowSinistro2() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void hover(String locator) throws Exception {
		Actions action = new Actions(driver);
		WebElement element = SeleniumUtils.findObjectClickable(driver, locator, null, TIME_OUT);
		if (element != null) {
			try {
				action.moveToElement(element).perform();
			} catch (StaleElementReferenceException e) {
				element = SeleniumUtils.findObjectClickable(driver, locator, null, TIME_OUT);
				element.click();
			} catch (WebDriverException e) {
				SeleniumUtils.waitLoadHdiSeguros(driver, TIME_OUT);
				element = driver.findElement(By.xpath(locator));
				element.click();
			}
		}
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

}
