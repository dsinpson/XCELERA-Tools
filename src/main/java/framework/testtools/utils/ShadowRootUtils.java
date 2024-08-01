package framework.testtools.utils;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

public class ShadowRootUtils {
	
	public static WebElement findShadowElement(WebDriver driver, WebElement shadowRootElement, String cssLocator, int timeoutInSeconds) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(Duration.ofMillis(1000L * timeoutInSeconds))
				.pollingEvery(Duration.ofMillis(1000))
				.ignoring(NoSuchElementException.class);
		
		return wait.until(new Function<WebDriver, WebElement>(){
		
			public WebElement apply(WebDriver driver ) {
				return shadowRootElement.findElement(By.cssSelector(cssLocator));
			}
		});
	}
	
	public static List<WebElement> findShadowElements(WebDriver driver, WebElement shadowRootElement, String cssLocator, int timeoutInSeconds) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(Duration.ofMillis(1000L * timeoutInSeconds))
				.pollingEvery(Duration.ofMillis(1000))
				.ignoring(NoSuchElementException.class);
		
		return wait.until(new Function<WebDriver, List<WebElement>>(){
		
			public List<WebElement> apply(WebDriver driver ) {
				return shadowRootElement.findElements(By.cssSelector(cssLocator));
			}
		});
	}
}
