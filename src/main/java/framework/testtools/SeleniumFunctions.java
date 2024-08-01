package framework.testtools;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.ScriptTimeoutException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.assertthat.selenium_shutterbug.utils.web.ScrollStrategy;
import com.google.common.primitives.Ints;

import framework.dataProviders.ConfigFileReader;
import framework.testtools.utils.SeleniumUtils;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class SeleniumFunctions implements ITestToolFunctions {
	private WebDriver _driver;
	private final Map<String, String> _executionConfigs;
	private final String _browserName;
	private String _windowHandled;
	private WebElement _shadowRootElement = null;
	boolean isWindows = System.getProperty("os.name").toLowerCase().contains("windows");

	private final int TIME_OUT = 10; // segundos

	public SeleniumFunctions(Map<String, String> executionConfigs) {
		_executionConfigs = executionConfigs;
		_browserName = _executionConfigs.get("System.Browser");
	}

	private WebElement findObject(String locator) throws Exception {
		WebElement element = SeleniumUtils.findObject(_driver, locator, _shadowRootElement, TIME_OUT);
		if (element == null)
			throw new Exception("Element " + locator + " not found");

		return element;
	}

	private List<WebElement> findObjects(String locator) throws Exception {
		List<WebElement> elements = SeleniumUtils.findObjects(_driver, locator, _shadowRootElement, TIME_OUT);
		if (elements == null)
			throw new Exception("Element " + locator + " not found");

		return elements;
	}

	private WebElement findObjectClickable(String locator) throws Exception {
		WebElement element = SeleniumUtils.findObjectClickable(_driver, locator, _shadowRootElement, TIME_OUT);
		if (element == null)
			throw new Exception("Element " + locator + " not found");

		return element;
	}

	public WebDriver getDriver() {
		return this._driver;
	}

	@Override
	public void getShadowRootElement(String locator) throws Exception {
		WebElement parentElement = findObject(locator);

		JavascriptExecutor jse = (JavascriptExecutor) _driver;
		_shadowRootElement = (WebElement) jse.executeScript("return arguments[0].shadowRoot;", parentElement);
	}

	@Override
	public void releaseShadowRootElement() throws Exception {
		_shadowRootElement = null;
	}

	@Override
	public void setWindowHandled() throws Exception {
		if (_driver != null) {
			_windowHandled = _driver.getWindowHandle();
		}
	}

	@Override
	public Object guiObject(String locator) throws Exception {
		Object obj = null;
		obj = findObject(locator);
		return obj;
	}

	@Override
	public void attach() throws Exception {
		_driver.navigate().refresh();
		// throw new Exception("Not implemented");
	}

	@Override
	public String captureCss(String locator) throws Exception {
		WebElement element = findObject(locator);

		if (element.isDisplayed()) {
			if (!(element.getAttribute("style") == null || element.getAttribute("style") == ""))
				return element.getAttribute("style");
			else
				return element.getCssValue("style");

		} else
			return "";
	}

	@Override
	public String captureCssColor(String locator) throws Exception {
		WebElement element = findObject(locator);

		if (element.isDisplayed()) {
			if (!(element.getAttribute("style") == null || element.getAttribute("style") == ""))
				return element.getAttribute("style");
			else
				return element.getCssValue("style");

		} else
			return "";
	}

	@Override
	public String captureText(String locator) throws Exception {

		WebElement element = findObject(locator);
		String alertMessage = "";

		if (element == null)
			return "";

		if (element.isDisplayed()) {
			if (!(element.getText() == null || element.getText().equals(""))) {
				return element.getText();
			} else if (!(element.getAttribute("value") == null || element.getAttribute("value").equals(""))) {
				return element.getAttribute("value");
			} else
				return "";

		} else {
			try {
				alertMessage = _driver.switchTo().alert().getText();
			} catch (Exception e) {
				alertMessage = "";
			}
			return alertMessage;
		}
	}

	@Override
	public String verificaIndice(List<WebElement> objeto) throws Exception {
		System.out.println("Verifica Indice 0: " + objeto.get(0).getText());
		return "0";
	}

	@Override
	public String VerificaIndice(String locator) throws Exception {
		WebElement element = findObject(locator);
		System.out.println("Verifica Indice 0: " + element.getText());
		return "0";
	}

	@Override
	public boolean visible(String locator) throws Exception {
		try {
			WebElement element = findObject(locator);

			return element.isDisplayed();

		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean exists(String locator) throws Exception {
		try {
			WebElement element = SeleniumUtils.findObject(_driver, locator, _shadowRootElement, 2);

			return element.getSize().getWidth() != 0;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean exists(String locator, int timeout) throws Exception {
		try {
			WebElement element = SeleniumUtils.findObject(_driver, locator, _shadowRootElement, timeout);
			return element.getSize().getWidth() != 0;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean enabled(String locator) throws Exception {

		WebElement element = SeleniumUtils.findObject(_driver, locator, _shadowRootElement, 2);

		if (element != null) {
			if (element.isDisplayed() && visible(locator)) {
				return element.isEnabled();
			}
		}
		return false;
	}

	@Override
	public boolean disabled(String locator) throws Exception {
		WebElement element = findObject(locator);

		return visible(locator) && !element.isEnabled();
	}

	@Override
	public boolean checked(String locator) throws Exception {
		WebElement element = findObject(locator);

		if (element.isDisplayed())
			return element.isSelected();
		else
			return false;
	}

	public WebDriver ConfigureFirefox() {
		System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + File.separator + "DriverSelenium"
				+ File.separator + "geckodriver.exe");
		FirefoxOptions fireFoxOptions = new FirefoxOptions();
		fireFoxOptions.addArguments("--disable-web-security");
		fireFoxOptions.addArguments("disable-infobars");
		// fireFoxOptions.addArguments("--headless");
		fireFoxOptions.addArguments("--window-size=1920,1080");
		fireFoxOptions.setCapability("marionette", true);
		WebDriver driver = new FirefoxDriver(fireFoxOptions);

		return driver;
	}

	@Override
	public void openApp(String app) throws Exception {

		System.out.println("Setting driver for [" + _browserName + "].");

		switch (_browserName.toUpperCase()) {
		case "IE":
			if (_driver != null) { // Quit Previous Driver
				_driver.quit();
				_driver.close();
			}

			System.setProperty("webdriver.ie.driver", System.getProperty("user.dir") + File.separator + "DriverSelenium"
					+ File.separator + "IEDriverServer.exe");
			_driver = new InternetExplorerDriver();
			break;

		case "FIREFOX":
			if (_driver != null) { // Quit Previous Driver
				_driver.quit();
				_driver.close();
			}

			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + File.separator
					+ "DriverSelenium" + File.separator + "geckodriver.exe");
			_driver = new FirefoxDriver();

			break;

		case "CHROME":
			if (_driver != null) {
				// Quit Previous Driver
				_driver.close();
				_driver.quit();
			}

			if (isWindows) {
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + File.separator
						+ "DriverSelenium" + File.separator + "chromedriver.exe");
			} else {
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + File.separator
						+ "DriverSelenium" + File.separator + "linux" + File.separator + "chromedriver");
			}

			System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");
			Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
			ChromeOptions chromeOptions = new ChromeOptions();
			// chromeOptions.addArguments("--user-data-dir=C:/Users/grupohdi01/AppData/Local/Google/Chrome/User
			// Data");
			// chromeOptions.addArguments("--profile-directory=Default");
			chromeOptions.addArguments("--lang=pt");
			chromeOptions.addArguments("--no-sandbox");
			chromeOptions.addArguments("--disable-web-security");
			chromeOptions.addArguments("disable-infobars");
			try {
				ConfigFileReader reader = new ConfigFileReader("configs/config.properties");
				String workerName = reader.getPropertyByKey("workerName");
				if (!Character.isDigit(workerName.charAt(workerName.length() - 1))) {
					chromeOptions.addArguments("--headless");
				}
			} catch (Exception e) {
				// ignore exception
			}
			chromeOptions.addArguments("--window-size=1920,1080");
			chromeOptions.setPageLoadStrategy(PageLoadStrategy.NONE);
//			chromeOptions.addExtensions(new File(System.getProperty("user.dir") + File.separator + "Extensions"
//					+ File.separator + "extension_2_3_164_0.crx"));

			_driver = new ChromeDriver(chromeOptions);
			_driver.manage().deleteAllCookies();

			break;

		default:
			System.out.println("No browser default found.");
			return;
		}

		System.out.println("Driver set.");
		_driver.manage().window().maximize();
		_driver.get(app);
		_driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		// _driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);

	}

	@Override
	public void openDeviceBrowser(String url) throws Exception {
		throw new Exception("Not implemented");
	}

	@Override
	public void click(String locator, int x, int y) throws Exception {
		WebElement element = findObject(locator);

		if (x != 0 && y != 0) {
			Actions actions = new Actions(_driver);
			actions.moveToElement(element, x, y).click().perform();

		} else
			click(locator);
	}

	@Override
	public void click(String locator) throws Exception {
		WebElement element = findObjectClickable(locator);
		if (element == null)
			throw new Exception("Element " + locator + " not found");

		try {
			element.click();
		} catch (StaleElementReferenceException e) {
			element = findObjectClickable(locator);
			element.click();
		} catch (WebDriverException e) {
			SeleniumUtils.waitLoadHdiSeguros(_driver, TIME_OUT);
			element = findObject(locator);
			element.click();
		}
	}

	private void findAndClick(WebElement element, String locator) throws Exception {
		element = findObject(locator);
		element.click();
	}

	@Override
	public void doubleClick(String locator) throws Exception {

		WebElement element = findObject(locator);

		Actions actions = new Actions(_driver);
		actions.doubleClick(element).perform();
	}

	@Override
	public void hover(String locator) throws Exception {
		Actions action = new Actions(_driver);
		WebElement element = SeleniumUtils.findObjectClickable(_driver, locator, _shadowRootElement, TIME_OUT);
		if (element != null) {
			try {
				action.moveToElement(element).perform();
			} catch (StaleElementReferenceException e) {
				element = SeleniumUtils.findObjectClickable(_driver, locator, _shadowRootElement, TIME_OUT);
				element.click();
			} catch (WebDriverException e) {
				SeleniumUtils.waitLoadHdiSeguros(_driver, TIME_OUT);
				element = _driver.findElement(By.xpath(locator));
				element.click();
			}
		}
	}

	@Override
	public void doubleClick(String locator, int x, int y) throws Exception {
		WebElement element = findObject(locator);

		if (x != 0 && y != 0) {
			Actions actions = new Actions(_driver);
			actions.moveToElement(element, x, y).doubleClick().perform();
		} else
			doubleClick(locator);
	}

	@Override
	public void clearField(String locator) throws Exception {
		WebElement element = findObject(locator);
		element.clear();
	}

	@Override
	public void setValue(String locator, String value) throws Exception {
		WebElement element = findObject(locator);

		try {
			element.click();
		} catch (StaleElementReferenceException e) {
			findAndClick(element, locator);
		} catch (WebDriverException e) {
			findAndClick(element, locator);
		}

		// @Gambiarra
		if (value.startsWith("#ENTER")) {
			Thread.sleep(3000);
			element.sendKeys(Keys.chord(Keys.CONTROL), Keys.ENTER);
		} else {
			if (!captureText(locator).isEmpty())
				element.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);

			// element.sendKeys(Keys.HOME);

			value = SeleniumUtils.HandleMaskedValues(element, value);

			for (int i = 0; i < value.length(); i++) {
				element.sendKeys(value.substring(i, i + 1));
				Thread.sleep(3);
			}

			if (element.toString().contains("CEP")) {
				Thread.sleep(7000);
			}

			if (element.toString().contains("Modelo")) {
			} else if (element.toString().contains("veículo")) {
			} else if (element.toString().contains("Placa")) {
			} else {
				element.sendKeys(Keys.TAB);
			}
		}
	}

	@Override
	public void selectValue(String locator, String tipo, String value) throws Exception {
		value = value.replace("\"", "");
		WebElement element = findObject(locator);
		switch (tipo.toLowerCase()) {
		case "text":
			try {

				if (locator.contains("HDI One click")) {
					System.out.println("Click no grid com Xpath substituido por valor da massa");
					System.out.println(value);
					System.out.println(locator);
					locator = locator.replace("HDI One click", value);

					element = findObject(locator);
					element.click();
				} else {
					new Select(element).selectByVisibleText(value);
				}
			} catch (StaleElementReferenceException e) {
				element = findObject(locator);
				new Select(element).selectByVisibleText(value);
			} catch (WebDriverException e) {
				element = findObject(locator);
				new Select(element).selectByVisibleText(value);
			}
			break;
		case "value":
			try {
				new Select(element).selectByValue(value);
			} catch (StaleElementReferenceException e) {
				element = findObject(locator);
				new Select(element).selectByValue(value);
			} catch (WebDriverException e) {
				element = findObject(locator);
				new Select(element).selectByVisibleText(value);
			}
			break;
		case "index":
			int index = Integer.parseInt(value);
			try {

				new Select(element).selectByIndex(index);
			} catch (StaleElementReferenceException e) {
				element = findObject(locator);
				new Select(element).selectByIndex(index);
			} catch (WebDriverException e) {
				SeleniumUtils.waitLoadHdiSeguros(_driver, TIME_OUT);
				element = findObject(locator);
				new Select(element).selectByIndex(index);
			}
			break;
		default:
			try {
				try {
					new Select(element).selectByValue(value);
				} catch (Exception e) {
					new Select(element).selectByVisibleText(value);
				}
			} catch (StaleElementReferenceException e) {
				element = findObject(locator);
				new Select(element).selectByVisibleText(value);
			} catch (WebDriverException e) {
				element = findObject(locator);
				new Select(element).selectByVisibleText(value);
			}
			break;
		}
	}

	@Override
	public void checkBox(String locator, Boolean checkedAction) throws Exception {
		WebElement element = findObject(locator);

		try {
			if (checkedAction != element.isSelected())
				element.click();
		} catch (StaleElementReferenceException e) {
			element = findObjectClickable(locator);
			if (checkedAction != element.isSelected())
				element.click();
		} catch (WebDriverException e) {
			SeleniumUtils.waitLoadHdiSeguros(_driver, TIME_OUT);
			element = findObject(locator);
			if (checkedAction != element.isSelected())
				element.click();
		}
	}

	@Override
	public void setFocus(String locator) throws Exception {
		WebElement element = findObject(locator);

		Actions actions = new Actions(_driver);
		actions.moveToElement(element).perform();
	}

	@Override
	public void maximazedWindow(String attach) throws Exception {
		_driver.manage().window().maximize();
	}

	@Override
	public void mouseMove(String locator, int x, int y) throws Exception {

		if (locator.isEmpty()) {
			Robot robot = new Robot();
			robot.mouseMove(x, y);

			return;
		}

		WebElement element = findObject(locator);

		Actions actions = new Actions(_driver);
		actions.moveToElement(element).build().perform();
	}

	@Override
	public void closeWindow() throws Exception {
		// throw new Exception("[CloseWindow]: Not implemented");
		String base = _driver.getWindowHandle();
		Set<String> aba = _driver.getWindowHandles();
		aba.remove(base);
		int abas = aba.size();
		int i = 1;
		for (String handle : aba) {
			if (i == abas) {
				_driver.switchTo().window(handle);
				_driver.close();
				break;
			}
			i++;
		}
		_driver.switchTo().window(base);
	}

	@Override
	public void closeWindow(String attach) throws Exception {
		// throw new Exception("[CloseWindow]: Not implemented");
		String base = _driver.getWindowHandle();
		Set<String> aba = _driver.getWindowHandles();
		aba.remove(base);
		int abas = aba.size();
		int i = 1;

		for (String handle : aba) {
			if (i == abas) {
				_driver.switchTo().window(handle);
				_driver.close();
				break;
			}
			i++;
		}
		_driver.switchTo().window(base);
	}

	@Override
	public void textSelect(String locator) throws Exception {
		WebElement element = findObject(locator);
		element.getText();
	}

	@Override
	public void selectTdTable(String attach, String Valor, String event) throws Exception {
		throw new Exception("[SelectTdTable]: Not implemented");

	}

	@Override
	public void capturePrint() throws Exception {
		// throw new Exception("[CapturePrint]: Not implemented");
		JavascriptExecutor jse = (JavascriptExecutor) _driver;
		jse.executeScript("document.title = 'act_documento_pdf_digital.htm'");

	}

	@Override
	public void capturePrintThread() throws Exception {
		throw new Exception("[CapturePrintThread]: Not implemented");
	}

	@Override
	public void waitForPageLoaded() throws Exception {
		_driver.manage().timeouts().pageLoadTimeout(TIME_OUT, TimeUnit.SECONDS);
	}

	@Override
	public boolean waitObject(String attach, String sec) throws Exception {
		return false;
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
	public void scroll(String locator, int offSet, int time) throws Exception {
		WebElement element = findObject(locator);

		new Actions(_driver).moveToElement(element).build().perform();
	}

	@Override
	public void scroll(String locator, String direction, int offSet, int time) throws Exception {
		WebElement element = findObject(locator);
		new Actions(_driver).moveToElement(element).build().perform();
	}

	@Override
	public String getAttribute(String locator, String attribute) throws Exception {
		WebElement element = findObject(locator);
		return element.getAttribute(attribute);
	}

	@Override
	public void sendKeys(String locator, String keys) throws Exception {
		WebElement element = findObject(locator);
		element.clear();
		element.sendKeys(keys);
	}

	public void sendKeys(String locator, Keys keys) throws Exception {
		WebElement element = findObject(locator);
		element.sendKeys(keys);
	}

	@Override
	public void closeBrowser() throws Exception {
		Runtime.getRuntime().exec("taskkill /f /im chromedriver.exe /t").waitFor();
	}

	@Override
	public void dragAndDrop() throws Exception {
		throw new Exception("[DragAndDrop]: Not implemented");
	}

	@Override
	public void scrollToObject(String locator) throws Exception {
		WebElement element = findObject(locator);
		new Actions(_driver).moveToElement(element).build().perform();
	}

	@Override
	public String captureCss(String locator, String attribute) throws Exception {
		WebElement element = findObject(locator);

		if (element.isDisplayed()) {
			if (!(element.getAttribute("background-color") == null || element.getAttribute("background-color") == ""))
				return element.getAttribute("background-color");
			else
				return element.getCssValue("background-color");
		} else
			return "";
	}

	@Override
	public String captureCssColor(String locator, String attribute) throws Exception {
		WebElement element = findObject(locator);

		if (element.isDisplayed()) {
			if (!(element.getAttribute("color") == null || element.getAttribute("color") == ""))
				return element.getAttribute("color");
			else
				return element.getCssValue("color");
		} else
			return "";
	}

	@Override
	public Object getDataGrid(String starcAttach) throws Exception {
		throw new Exception("[GetDataGrid]: Not implemented");
	}

	@Override
	public WebElement ReturnElement(String locator) throws Exception {
		return findObject(locator);
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
		WebElement element = findObject(locator);

		return new Select(element).getOptions();
	}

	@Override
	public void switchTab() throws Exception {
		for (String handle : _driver.getWindowHandles()) {
			_driver.switchTo().window(handle);
		}
	}

	@Override
	public String getLocation(String locator) throws Exception {
		WebElement element = findObject(locator);
		return element.getLocation().x + ";" + element.getLocation().y;
	}

	@Override
	public void toURL(String url) throws Exception {
		_driver.navigate().to(url);
	}

	@Override
	public String captureSelectedOption(String locator) throws Exception {
		WebElement element = findObject(locator);
		return new Select(element).getFirstSelectedOption().getText();
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
	public boolean setIframe(String locator) throws Exception {
		return false;
	}

	@Override
	public void switchTo(String locator) throws Exception {

		if (locator.isEmpty()) {
			_driver.switchTo().defaultContent();
			return;
		}

		try {
			Integer locatorInt = Ints.tryParse(locator);

			if (locatorInt == null) {
				WebElement element = findObject(locator);
				_driver.switchTo().frame(element);

			} else
				_driver.switchTo().frame(locatorInt);

		} catch (Exception e) {
			// throw new Exception(e.getMessage());
		}

	}

	@Override
	public void switchTo() throws Exception {
		switchTo("");
	}

	@Override
	public void waitVisibilityOfObject(String attach, boolean visibility) throws Exception {
		throw new Exception("[WaitVisibilityOfObject]: Not implemented");
	}

	@Override
	public void switchTab(Integer tabIndex) throws Exception {

		if (tabIndex == null)
			switchTab();
		else
			_driver.switchTo().window(_driver.getWindowHandles().toArray()[tabIndex].toString());

	}

	@Override
	public void highlight(String locator) throws Exception {
		WebElement element = findObject(locator);

		JavascriptExecutor jse = (JavascriptExecutor) _driver;
		jse.executeScript("arguments[0].style.border='3px solid #FF0000'", element);

		Actions actions = new Actions(_driver);
		actions.moveToElement(element).perform();
	}

	public BufferedImage takeScreenshot(String printPath) throws Exception {
		if (_driver == null) {
			BufferedImage image = new Robot()
					.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));

			return image;
		}

		if (isAlertPresent()) {
			BufferedImage image2 = new Robot()
					.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
			_driver.switchTo().alert().accept();

			return image2;
		} else {
			try {
				_driver.switchTo().defaultContent();
			} catch (Exception e) {
			}

			ConfigFileReader reader = new ConfigFileReader("configs/config.properties");
			if (reader.getPropertyByKey("debugMode").equalsIgnoreCase("true")) {
				try {
					Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(10))
							.takeScreenshot(_driver);
					return screenshot.getImage();
				} catch (ScriptTimeoutException e) {
					throw e;
				}
			} else {
				try {
					return Shutterbug.shootPage(_driver, ScrollStrategy.WHOLE_PAGE_CHROME, 100, true).getImage();
				} catch (Exception e) {
					BufferedImage image = new Robot()
							.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));

					return image;
				}
			}
		}
	}

	@Override
	public boolean isAlertPresent() throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(_driver, 2);
			Alert alert = wait.until(ExpectedConditions.alertIsPresent());

			return alert != null;

		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void closeDriver() {
		if (_driver != null)
			_driver.quit();
		// throw new Exception("[CloseDriver]: Not implemented");

	}

	@Override
	public void checkAlert(String value) throws Exception {
		Alert alert = _driver.switchTo().alert();
		String alertMessage = alert.getText();

		System.out.println(">> MASSA: " + value.trim());
		System.out.println(">>  TELA: " + alertMessage);
	}

	@Override
	public void acessaMenu(String locator) throws Exception {
		try {
			JavascriptExecutor jse = (JavascriptExecutor) _driver;
			jse.executeScript("arguments[0].click();", findObject(locator));
		} catch (Exception e) {
			_driver.switchTo().window(_windowHandled);
			_driver.manage().window().maximize();

			JavascriptExecutor jse = (JavascriptExecutor) _driver;
			jse.executeScript("arguments[0].click();", findObject(locator));
		}
	}

	@Override
	public String getAllTextFromPage() throws Exception {
		try {
			WebElement element = _driver.findElement(By.tagName("body"));

			return element.getText();
		} catch (Exception e) {
			return "";
		}
	}

	@Override
	public void waitObjectToVanish(String locator, int timeOutInSeconds) throws Exception {
		WebDriverWait wait = new WebDriverWait(_driver, timeOutInSeconds);
		String[] locatorArray = locator.split(":=");

		if (locatorArray.length <= 1) {
			new WebDriverWait(_driver, timeOutInSeconds)
					.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(locator)));
		} else {

			switch (locatorArray[0]) {
			case "id":
				new WebDriverWait(_driver, timeOutInSeconds)
						.until(ExpectedConditions.invisibilityOfElementLocated(By.id(locatorArray[1])));
			case "name":
				new WebDriverWait(_driver, timeOutInSeconds)
						.until(ExpectedConditions.invisibilityOfElementLocated(By.name(locatorArray[1])));
			case "className":
				new WebDriverWait(_driver, timeOutInSeconds)
						.until(ExpectedConditions.invisibilityOfElementLocated(By.className(locatorArray[1])));
			case "tagName":
				new WebDriverWait(_driver, timeOutInSeconds)
						.until(ExpectedConditions.invisibilityOfElementLocated(By.tagName(locatorArray[1])));
			case "linkText":
				new WebDriverWait(_driver, timeOutInSeconds)
						.until(ExpectedConditions.invisibilityOfElementLocated(By.linkText(locatorArray[1])));
			case "cssLocator":
				new WebDriverWait(_driver, timeOutInSeconds)
						.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(locatorArray[1])));
			case "xpath":
				new WebDriverWait(_driver, timeOutInSeconds)
						.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(locatorArray[1])));
			default:
				new WebDriverWait(_driver, timeOutInSeconds)
						.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(locatorArray[1])));
			}
		}
	}

	@Override
	public void WaitObjectVisibility(String locator, int timeOutInSeconds) throws Exception {
		WebDriverWait wait = new WebDriverWait(_driver, timeOutInSeconds);
		String[] locatorArray = locator.split(":=");

		if (locatorArray.length <= 1) {
			new WebDriverWait(_driver, timeOutInSeconds)
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
		} else {

			switch (locatorArray[0]) {
			case "id":
				new WebDriverWait(_driver, timeOutInSeconds)
						.until(ExpectedConditions.visibilityOfElementLocated(By.id(locatorArray[1])));
			case "name":
				new WebDriverWait(_driver, timeOutInSeconds)
						.until(ExpectedConditions.visibilityOfElementLocated(By.name(locatorArray[1])));
			case "className":
				new WebDriverWait(_driver, timeOutInSeconds)
						.until(ExpectedConditions.visibilityOfElementLocated(By.className(locatorArray[1])));
			case "tagName":
				new WebDriverWait(_driver, timeOutInSeconds)
						.until(ExpectedConditions.visibilityOfElementLocated(By.tagName(locatorArray[1])));
			case "linkText":
				new WebDriverWait(_driver, timeOutInSeconds)
						.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(locatorArray[1])));
			case "cssLocator":
				new WebDriverWait(_driver, timeOutInSeconds)
						.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(locatorArray[1])));
			case "xpath":
				new WebDriverWait(_driver, timeOutInSeconds)
						.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locatorArray[1])));
			default:
				new WebDriverWait(_driver, timeOutInSeconds)
						.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locatorArray[1])));
			}
		}
	}

	@Override
	public List<WebElement> getElements(String locator) throws Exception {
		List<WebElement> list = SeleniumUtils.findObjects(_driver, locator, _shadowRootElement, TIME_OUT);
		if (list == null)
			throw new Exception("Element " + locator + " not found");

		return list;
	}

	@Override
	public void saveFile() throws Exception {
		File downloadFolder = new File(System.getProperty("user.home") + "/Downloads");
		File[] listOfFiles = downloadFolder.listFiles();

		Arrays.sort(listOfFiles, new Comparator<File>() {
			public int compare(File f1, File f2) {
				return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
			}
		});

		String defaultDir = _executionConfigs.get("Custom.dirDefaultPrints");

		Integer index = listOfFiles.length - 1;

		File dest = new File(defaultDir + listOfFiles[index].getName());

		FileUtils.copyFile(listOfFiles[index], dest);
	}

	@Override
	public void switchWindow() throws Exception {
		for (String handle : _driver.getWindowHandles()) {
			_driver.switchTo().window(handle);
		}
	}

	@Override
	public void switchWindowSinistro() throws Exception {
		Set<String> aba = _driver.getWindowHandles();
		int abas = aba.size();
		int i = 1;
		for (String handle : aba) {
			if (i == abas) {
				_driver.switchTo().window(handle);
				break;
			}
			i++;
		}
	}

	@Override
	public boolean switchWindowEfetivacao() throws Exception {
		boolean a = false;

		Set<String> aba = _driver.getWindowHandles();
		int abas = aba.size();
		int i = 1;
		for (String handle : aba) {
			if (i == abas) {
				Robot robot = new Robot();
				robot.mouseMove(591, 162);
				robot.delay(1500);
				robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // press left click
				robot.delay(1500);
				robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.delay(1500);
				_driver.switchTo().window(handle);
				a = exists("(//*[@id='dados_segurado']//table[@id='1'])[1]");
				if (!a) {
					_driver.close();
					switchWindow();
				}
				break;
			}
			i++;
		}
		return a;
	}

	public void switchWindowSinistro2() throws Exception {
		String wHandle2 = _driver.getWindowHandle();

		for (String winHandle2 : _driver.getWindowHandles()) {
			_driver.switchTo().window(winHandle2);

			if (isAlertPresent()) {
				_driver.switchTo().alert().accept();
				_driver.switchTo().window(wHandle2);
			}
		}

		for (String winHandle2 : _driver.getWindowHandles()) {
			WebElement element = SeleniumUtils.findObject(_driver, "//*[@id=\"NaoTemIndicacaoBateProntoTexto\"]",
					_shadowRootElement, 1);
			if (element != null && element.getSize().getWidth() > 0)
				break;

			_driver.switchTo().window(winHandle2);
		}
	}

	@Override
	public void acceptAlert() throws Exception {
		if (isAlertPresent()) {
			_driver.switchTo().alert().accept();
			Set<String> aba = _driver.getWindowHandles();
			int abas = aba.size();
			int i = 1;
			for (String handle : aba) {
				if (i == abas) {
					_driver.switchTo().window(handle);
					break;
				}
				i++;
			}
		}
	}

	@Override
	public void acceptAlert(String printPath) throws Exception {
		if (isAlertPresent()) {
			takeScreenshot(printPath);
			_driver.switchTo().alert().accept();
			_driver.switchTo().defaultContent();
		}
	}

	@Override
	public byte[] takeScreenshotForCucumberReport() {
		try {
			return ((TakesScreenshot) _driver).getScreenshotAs(OutputType.BYTES);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void submit(String locator) throws Exception {
		WebElement element = findObject(locator);
		element.submit();
	}

}
