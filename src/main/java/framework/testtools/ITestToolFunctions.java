package framework.testtools;

import java.awt.image.BufferedImage;
import java.util.List;

import org.openqa.selenium.WebElement;

public interface ITestToolFunctions {

	/**
	 * The method finds the an object in the current screen and returns the object
	 * on screen as a current object knows by Test Tool
	 * 
	 * @param attach
	 *            - the object's identify
	 * @return the attach as a Object
	 */
    Object guiObject(String attach) throws Exception;

	/**
	 * The method returns the text that contains in specify object
	 * 
	 * @param attach
	 *            - the object's identify
	 * @return captured text
	 * @throws Exception
	 */
    void attach() throws Exception;

	void acceptAlert() throws Exception;

	void acceptAlert(String printPath) throws Exception;

	String captureCss(String attach) throws Exception;

	/**
	 * The method verifies is the object exists in screen
	 * 
	 * @param Attach
	 *            - the object's identify
	 * @return if the attach exists or not
	 * @throws Exception
	 */

    String captureCssColor(String attach) throws Exception;

	/**
	 * The method verifies is the object exists in screen
	 * 
	 * @param Attach
	 *            - the object's identify
	 * @return if the attach exists or not
	 * @throws Exception
	 */

    String captureText(String attach) throws Exception;

	/**
	 * The method verifies is the object exists in screen
	 * 
	 * @param Attach
	 *            - the object's identify
	 * @return if the attach exists or not
	 * @throws Exception
	 */

    String verificaIndice(List<WebElement> objeto) throws Exception;

	String VerificaIndice(String Attach) throws Exception;

	boolean visible(String Attach) throws Exception;

	/**
	 * The method verifies if the object is enabled to test
	 * 
	 * @param attach
	 *            - the object's identify
	 * @return if the object is visible or not
	 */
    boolean exists(String Attach) throws Exception;

	boolean exists(String Attach, int timeout) throws Exception;

	/**
	 * The method verifies if the object is enabled to test
	 * 
	 * @param attach
	 *            - the object's identify
	 * @return if the object is enabled or not
	 */
    boolean enabled(String attach) throws Exception;

	/**
	 * The method verifies if the object is disabled
	 * 
	 * @param attach
	 * @return if the object is disabled or not
	 * @throws Exception
	 */
    boolean disabled(String attach) throws Exception;

	/**
	 * The method launches an application
	 * 
	 * @param app
	 *            - application's name
	 */
    boolean checked(String attach) throws Exception;

	/**
	 * The method launches an application
	 * 
	 * @param app
	 *            - application's name
	 */
    void openApp(String app) throws Exception;

	/**
	 * The method launches an bowser application in a mobile device
	 * 
	 * @param url
	 *            - Web site URL
	 */
    void openDeviceBrowser(String url) throws Exception;

	/**
	 * The method clicks in a specific coordinates on attach
	 * 
	 * @param objectAttach
	 * @param x
	 *            - coordinate x
	 * @param y
	 *            - coordinate y
	 */
    void click(String objectAttach, int x, int y) throws Exception;

	/**
	 * The method clicks in a specific object by attach
	 * 
	 * @param objectAttach
	 */
    void click(String objectAttach) throws Exception;

	/**
	 * The method clicks two times in specific object by attach
	 * 
	 * @param objectAttach
	 *            - the object's identify
	 */
    void doubleClick(String objectAttach) throws Exception;

	/**
	 * The method clicks two times in specific coordinate in object
	 * 
	 * @param objectAttach
	 *            - the object's identify
	 * @param x
	 *            - coordinate x
	 * @param y
	 *            - coordinate y
	 */
    void doubleClick(String objectAttach, int x, int y) throws Exception;

	/**
	 * The method clears a field
	 * 
	 * @throws Exception
	 */
    void clearField(String attach) throws Exception;

	/**
	 * The method sets a text in specific object
	 * 
	 * @param objectAttach
	 *            - the object's identify
	 * @param value
	 *            - the text that you want to insert in the field
	 * @throws Exception
	 */
    void setValue(String objectAttach, String value) throws Exception;

	/**
	 * This method is responsible for select a value from ComboBox. The methods has
	 * been elaborated for attends all of devices, for this it uses the native
	 * methods from class Client.
	 * 
	 * @param attach
	 *            - the object's identify
	 * @param value
	 *            - the text that you want to select
	 * @throws Exception
	 */
    void selectValue(String attach, String tipo, String value) throws Exception;

	/**
	 * The method checks the field by the current status
	 * 
	 * @param Attach
	 *            - the object's identify
	 * @param checkedAction
	 * @throws Exception
	 */
    void checkBox(String Attach, Boolean checkedAction) throws Exception;

	/**
	 * The method focus in a specific object
	 * 
	 * @param attach
	 * @throws Exception
	 */
    void setFocus(String attach) throws Exception;

	/**
	 * The method maximizes the window
	 * 
	 * @param attach
	 * @throws Exception
	 */
    void maximazedWindow(String attach) throws Exception;

	/**
	 * The method moves the cursor in a specific coordinate on attach
	 * 
	 * @param attach
	 * @param x
	 * @param y
	 * @throws Exception
	 */
    void mouseMove(String attach, int x, int y) throws Exception;

	/**
	 * The method closes the current window
	 * 
	 * @param attach
	 * @throws Exception
	 */
    void closeWindow(String attach) throws Exception;

	void closeWindow() throws Exception;

	/**
	 * The method selects a text in a specific attach
	 * 
	 * @param attach
	 */
    void textSelect(String attach) throws Exception;

	/**
	 * The method selects a specific id in a table
	 * 
	 * @param attach
	 * @param Valor
	 * @param event
	 * @throws Exception
	 */
    void selectTdTable(String attach, String Valor, String event) throws Exception;

	/**
	 * The method captures a print of current screen
	 */
    void capturePrint() throws Exception;

	/**
	 * The method captures a print of current screen using thread
	 */
    void capturePrintThread() throws Exception;

	void waitForPageLoaded() throws Exception;

	void hover(String locator) throws Exception;

	/**
	 * This method waits an object until the timeout
	 * 
	 * @param obj
	 *            - the object's identify
	 * @param sec
	 *            - the timeout
	 * @return true if the object exists or false if the wait failed
	 * @throws Exception
	 */
    boolean waitObject(String attach, String sec) throws Exception;

	/**
	 * This method closes applications.
	 * 
	 * @param application
	 */
    void closeExe(String application) throws Exception;

	/**
	 * This method drags objects to another position
	 * 
	 * @param attach
	 *            - Object to move
	 * @param x
	 * @param y
	 */
    void drag(String attach, int x, int y) throws Exception;

	/**
	 * This method scroll pages.
	 * 
	 * @param direction
	 *            - Direction to scroll the page
	 * @param offSet
	 *            - How much the screen should be moved
	 * @time how long the action will take to execute
	 */
    void scroll(String direction, int offSet, int time) throws Exception;

	/**
	 * This method scroll pages.
	 * 
	 * @param attach
	 *            - Object to move
	 * @param direction
	 *            - Direction to scroll the page
	 * @param offSet
	 *            - How much the screen should be moved
	 * @time how long the action will take to execute
	 */
    void scroll(String attach, String direction, int offSet, int time) throws Exception;

	/**
	 * This method gets an attribute from a object
	 * 
	 * @param attibute
	 *            The attribute that you want verify
	 * @return The object's attribute
	 */
    String getAttribute(String attach, String attribute) throws Exception;

	void sendKeys(String attach, String keys) throws Exception;

	void closeBrowser() throws Exception;

	void dragAndDrop() throws Exception;

	void scrollToObject(String attachStr) throws Exception;

	String captureCss(String attach, String attribute) throws Exception;

	String captureCssColor(String attach, String attribute) throws Exception;

	Object getDataGrid(String starcAttach) throws Exception;

	WebElement ReturnElement(String attach) throws Exception;

	WebElement returnElement(WebElement obj, String attach, String type) throws Exception;

	List<WebElement> returnList(String attach) throws Exception;

	void switchTab() throws Exception;

	String getLocation(String attach) throws Exception;

	void toURL(String string) throws Exception;

	String captureSelectedOption(String attach) throws Exception;

	List<WebElement> returnSelectOptions(String attach) throws Exception;

	int returnSize(String attach) throws Exception;

	void pinchZoom() throws Exception;

	boolean setIframe(String attach) throws Exception;

	void switchTo(String id) throws Exception;

	void switchTo() throws Exception;

	void waitVisibilityOfObject(String attach, boolean visibility) throws Exception;

	void switchTab(Integer tabIndex) throws Exception;

	void highlight(String attach) throws Exception;

	BufferedImage takeScreenshot(String printPath) throws Exception;

	byte[] takeScreenshotForCucumberReport();

	boolean isAlertPresent() throws Exception;

	void closeDriver();

	void checkAlert(String value) throws Exception;

	void acessaMenu(String locator) throws Exception;

	String getAllTextFromPage() throws Exception;

	void setWindowHandled() throws Exception;

	void waitObjectToVanish(String locator, int timeOutInSeconds) throws Exception;

	void WaitObjectVisibility(String locator, int timeOutInSeconds) throws Exception;

	List<WebElement> getElements(String locator) throws Exception;

	void saveFile() throws Exception;

	void switchWindow() throws Exception;

	void switchWindowSinistro() throws Exception;

	void switchWindowSinistro2() throws Exception;

	boolean switchWindowEfetivacao() throws Exception;

	void getShadowRootElement(String locator) throws Exception;

	void releaseShadowRootElement() throws Exception;

	void submit(String locator) throws Exception;

}
