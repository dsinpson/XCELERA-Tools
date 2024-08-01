package framework.testtools.utils;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumUtils {

	public static void waitUntilDocumentIsReady(WebDriver driver, int timeOutInSeconds) {
		try {
			
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);

			wait.until((d) -> {
				Boolean isAjaxFinished = (Boolean) ((JavascriptExecutor) driver)
				.executeScript("return jQuery != undefined && jQuery.active == 0");

				return isAjaxFinished;
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void waitForAjaxControls(WebDriver driver, int timeoutInSeconds) throws Exception {

		if (driver instanceof JavascriptExecutor) {
			JavascriptExecutor jse = (JavascriptExecutor) driver;

			for (int i = 0; i < timeoutInSeconds; i++) {
				Object numberOfAjaxConnections = jse.executeScript("return jQuery.active");
				if (numberOfAjaxConnections instanceof Long) {
					Long n = (Long) numberOfAjaxConnections;

					if (n.longValue() == 0L)
						break;
				}
				Thread.sleep(1000);
			}
		}
	}
	
	private static String getTypeFindFromLocator(String locator) {
		if (locator.contains("//") || locator.contains("body"))
			return "xpath";
		else if (locator.contains("@id"))
			return "id";
		else if (locator.contains("name"))
			return "name";
		else if (locator.contains("iframe"))
			return "tagName";
		else
			return "linkText";
	}
	

	public static WebElement findObject(WebDriver driver, String locator,WebElement shadowRootElement, int timeOutInSeconds) {
		try {
			if(shadowRootElement != null)
				return ShadowRootUtils.findShadowElement(driver, shadowRootElement, locator, timeOutInSeconds);
			
			String[] locatorArray = locator.split(":=");
			
            if(locatorArray.length <= 1) {
                return new WebDriverWait(driver, timeOutInSeconds)
                        .until(ExpectedConditions.presenceOfElementLocated(By.xpath(locator)));
            }

			switch (locatorArray[0]) {
				case "id":
					return new WebDriverWait(driver, timeOutInSeconds)
							.until(ExpectedConditions.presenceOfElementLocated(By.id(locatorArray[1])));
				case "name":
					return new WebDriverWait(driver, timeOutInSeconds)
							.until(ExpectedConditions.presenceOfElementLocated(By.name(locatorArray[1])));
				case "className":
					return new WebDriverWait(driver, timeOutInSeconds)
							.until(ExpectedConditions.presenceOfElementLocated(By.className(locatorArray[1])));
				case "tagName":
					return new WebDriverWait(driver, timeOutInSeconds)
							.until(ExpectedConditions.presenceOfElementLocated(By.tagName(locatorArray[1])));
				case "linkText":
						return new WebDriverWait(driver, timeOutInSeconds)
								.until(ExpectedConditions.presenceOfElementLocated(By.linkText(locatorArray[1])));
				case "cssLocator":
					return new WebDriverWait(driver, timeOutInSeconds)
							.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(locatorArray[1])));
				case "xpath":
					return new WebDriverWait(driver, timeOutInSeconds)
							.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locatorArray[1])));
				default:
					return new WebDriverWait(driver, timeOutInSeconds)
							.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locatorArray[1])));
			}
		} catch (Exception e) {
			return null;
		}
	}

		public static List<WebElement> findObjects(WebDriver driver, String locator,WebElement shadowRootElement, int timeOutInSeconds) {

			try {
				if(shadowRootElement != null)
					return ShadowRootUtils.findShadowElements(driver, shadowRootElement, locator, timeOutInSeconds);
				
				WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);			
				String[] locatorArray = locator.split(":=");

				if(locatorArray.length <= 1) {
                	wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locatorArray[1])));
					return driver.findElements(By.xpath(locatorArray[1]));                }

				switch (locatorArray[0]) {
					case "id":
						wait.until(ExpectedConditions.presenceOfElementLocated(By.id(locatorArray[1])));
						return driver.findElements(By.id(locatorArray[1]));
					case "name":
						wait.until(ExpectedConditions.presenceOfElementLocated(By.name(locatorArray[1])));
						return driver.findElements(By.name(locatorArray[1]));
					case "className":
						wait.until(ExpectedConditions.presenceOfElementLocated(By.className(locatorArray[1])));
						return driver.findElements(By.className(locatorArray[1]));
					case "tagName":
						wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName(locatorArray[1])));
						return driver.findElements(By.tagName(locatorArray[1]));
					case "linkText":
						wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText(locatorArray[1])));
						return driver.findElements(By.linkText(locatorArray[1]));
					case "cssLocator":
						wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(locatorArray[1])));
						return driver.findElements(By.cssSelector(locatorArray[1]));
					case "xpath":
						wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locatorArray[1])));
						return driver.findElements(By.xpath(locatorArray[1]));
					default:
                        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locatorArray[1])));
						return driver.findElements(By.xpath(locatorArray[1]));
				}
			} catch (Exception e) {
				return null;
			}
		}	
	public static WebElement findObjectClickable(WebDriver driver, String locator,WebElement shadowRootElement , int timeOutInSeconds) {
		try {
			if(shadowRootElement != null)
				return ShadowRootUtils.findShadowElement(driver, shadowRootElement, locator, timeOutInSeconds);
			                       
			String[] locatorArray = locator.split(":=");

			if(locatorArray.length <= 1) {
                return new WebDriverWait(driver, timeOutInSeconds)
                        .until(ExpectedConditions.elementToBeClickable(By.xpath(locator)));
            }

			switch (locatorArray[0]) {
				case "id":
					return new WebDriverWait(driver, timeOutInSeconds)
							.until(ExpectedConditions.elementToBeClickable(By.id(locatorArray[1])));
				case "name":
					return new WebDriverWait(driver, timeOutInSeconds)
							.until(ExpectedConditions.elementToBeClickable(By.name(locatorArray[1])));
				case "className":
					return new WebDriverWait(driver, timeOutInSeconds)
							.until(ExpectedConditions.elementToBeClickable(By.className(locatorArray[1])));
				case "tagName":
					return new WebDriverWait(driver, timeOutInSeconds)
							.until(ExpectedConditions.elementToBeClickable(By.tagName(locatorArray[1])));
				case "linkText":
						return new WebDriverWait(driver, timeOutInSeconds)
								.until(ExpectedConditions.elementToBeClickable(By.linkText(locatorArray[1])));
				case "cssLocator":
					return new WebDriverWait(driver, timeOutInSeconds)
							.until(ExpectedConditions.elementToBeClickable(By.cssSelector(locatorArray[1])));
				case "xpath":
					return new WebDriverWait(driver, timeOutInSeconds)
							.until(ExpectedConditions.elementToBeClickable(By.xpath(locatorArray[1])));
				default:
                    return new WebDriverWait(driver, timeOutInSeconds)
                        .until(ExpectedConditions.elementToBeClickable(By.xpath(locatorArray[1])));
			}
		} catch (Exception e) {
			return findObject(driver, locator,shadowRootElement, timeOutInSeconds);
		}
	}
	
	public static void waitLoadHdiSeguros(WebDriver driver, int timeOutInSeconds) {
		new WebDriverWait(driver, 60).until(ExpectedConditions
				.invisibilityOfAllElements(driver.findElements(By.xpath("//img[contains(@src,'aguarde.gif')]"))));

		try {
			Thread.sleep(1000);
		} catch (Exception e) {

		}
	}
	
	public static String HandleMaskedValues(WebElement element, String value) throws Exception {

		// tratar dinheiro
		if (element.getAttribute("class").contains("mask-money")) {
			value = value.replaceAll(",", "");
			for (int i = 0; i < 3; i++) {
				if (value.length() < 3)
					break;
				value = value.substring(0, value.length() - 1);
			}
			return value;
		}
		// tratar CNPJ
		if (element.getAttribute("class").contains("mask-cnpj")) {
			return value.replaceAll("\\.", "").replaceAll("/", "").replaceAll("-", "");
		}
		// tratar Data
		if (element.getAttribute("class").contains("mask-date")) {
			return value.replaceAll("/", "");
		}
		return value;
	}
}
