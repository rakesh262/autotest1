package sqs.core.actions.web;

import cucumber.runtime.CucumberException;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import sqs.core.actions.interfaces.SeleniumActionsInterface;
import sqs.core.constants.ApplicationTypes.ApplicationType;
import sqs.core.constants.PropertyConstants;
import sqs.framework.FrameworkData;
import sqs.core.utils.ObjectRepository;
import sqs.core.utils.Utilities;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static sqs.core.constants.Constants.*;
import static sqs.core.utils.LoggerUtils.actionCompletedLog;
import static sqs.core.utils.LoggerUtils.actionStartedLog;

public class SeleniumActions extends FrameworkData implements SeleniumActionsInterface {

	private static Logger logger = Logger.getLogger(SeleniumActions.class);

	public static void setDriver(WebDriver driver) {
		SeleniumActions.driver = driver;
	}

	protected WebElement getElement(By locator) {
		return getElement(locator, true, true);
	}

	protected boolean clickAndSwitchWindowIfExists(By locator) {
		boolean isElementClicked;
		takeScreenshot();
		Set<String> windowsBeforeClick = getWindowHandles();
		WebElement element = getElement(locator);
		element.click();
		logger.debug(CLICKED_ON_ELEMENT	+ locator.toString());

		isElementClicked = true;
		Utilities.waitFor(2000, "After Clicking:" + locator);
		switchWindowIfExists(windowsBeforeClick);
		waitForPageToLoad();
		switchWindowIfExists(windowsBeforeClick);
		return isElementClicked;
	}

	protected void switchWindowIfExists(Set<String> windowsBeforeClick) {

		Set<String> windowsAfterClick = getWindowHandles();
		if (windowsBeforeClick.size() < windowsAfterClick.size()) {
			logger.debug("New window opened");
			switchToNewWindow();
		} else if (windowsBeforeClick.size() > windowsAfterClick.size()) {
			logger.debug("working window get closed");
			switchToWindow(getMainWindowHandle());
		} else {
			logger.debug("No window was opened or closed after Click action");
		}
		takeScreenshot();

	}

	protected boolean click(By locator, int retryCount) {
		boolean isElementClicked = false;
		try {
			isElementClicked = clickAndSwitchWindowIfExists(locator);
		} catch (WebDriverException e) {
			if (e.getMessage().contains("OTHER element would receive the click")) {
				if (retryCount < 3) {
					JavascriptExecutor js = (JavascriptExecutor) driver;
					js.executeScript("scroll(0,250);");
					Utilities.waitFor(3000, "Screen Scrolling");

				} else {
					logger.error("The field is Overlapped by another field Not able to perform click action");
					throw new CucumberException(
							"The field is Overlapped by another field Not able to perform click action");
				}

			} else {
				throw e;
			}
		} catch (Exception e) {
			throw new CucumberException(e);
		}

		setActionPerformed(CLICK);
		return isElementClicked;
	}

	protected boolean clickElement(WebElement element) {
		boolean isElementClicked;
		try {
			takeScreenshot();
			Set<String> windowsBeforeClick = getWindowHandles();
			element.click();
			logger.debug(CLICKED_ON_ELEMENT + element);

			isElementClicked = true;
			Utilities.waitFor(1000, "After Clicking:" + element);
			waitForPageToLoad();
			switchWindowIfExists(windowsBeforeClick);
		}

		catch (Exception e) {
			throw new CucumberException(e);
		}
		setActionPerformed(CLICK);
		return isElementClicked;
	}

	public boolean waitForPageToLoad() {
		double waitedSeconds = 0;
		boolean isPageLoaded = false;
		double secondsToWait = 60;
		Utilities.waitFor(500, "Page Load wait process started");
		actionStartedLog(Utilities.getCallerMethodName());

		while (!isPageLoaded && waitedSeconds <= secondsToWait) {
			try {
				isPageLoaded = (new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver dr) {
						return ((JavascriptExecutor) dr).executeScript("return document.readyState=='complete'")
								.equals("true");
					}
				}!=(null));
				isPageLoaded = applicationSpecificPageLoadCheck(isPageLoaded);
				if (!isPageLoaded) {
					Utilities.waitFor(500, "Page Load");
				}
			} catch (Exception e) {
				logger.error("Waiting for Page Load" + e.getMessage());
			}
			waitedSeconds = waitedSeconds + 0.5;
			logger.debug(waitedSeconds + " Seconds Waited as of now");
		}
		actionCompletedLog(Utilities.getCallerMethodName());
		return isPageLoaded;
	}

	protected boolean waitForPageLoadFACE(boolean isPageLoaded) {
		By faceLodingObject = By.xpath("//div[@class='waiting-container']/span[@class='waiting']");
		actionStartedLog(Utilities.getCallerMethodName());

		if (isElementExistsCheckWithoutWait(faceLodingObject)) {
			isPageLoaded = false;
			Utilities.waitFor(1000, "To load Face screen");
		}

		if (isPageLoaded) {
			String pageSource = driver.getPageSource();
			pageSource = pageSource.replaceAll("[^A-z0-9]", "");
			logger.debug("Face screen Size " + pageSource.length());
			if (pageSource.length() < 7000) {
				Utilities.waitFor(1000, "To load Face screen fully " + pageSource.length());
				isPageLoaded = false;
			}
		}

		actionCompletedLog(Utilities.getCallerMethodName());
		return isPageLoaded;
	}

	protected boolean waitForPageLoadRC(boolean isPageLoaded) {
		By faceLodingObject = By.id("wait-indicator");
		actionStartedLog(Utilities.getCallerMethodName());

		if (isElementExistsCheckWithoutWait(faceLodingObject)) {
			isPageLoaded = false;
			Utilities.waitFor(1000, "To load RC screen");
		}

		actionCompletedLog(Utilities.getCallerMethodName());
		return isPageLoaded;
	}

	protected boolean waitForPageLoadKoko(boolean isPageLoaded) {
		By kokoLodingObject = By.xpath("//h3/strong[contains(.,'Klick-Kredit')] | //h1[contains(.,'Login')]");

		actionStartedLog(Utilities.getCallerMethodName());

		if (isElementExistsCheckWithoutWait(kokoLodingObject)) {

			logger.debug("Koko Application is loaded");
		} else {
			logger.debug("Koko Application is not loaded");
		}

		actionCompletedLog(Utilities.getCallerMethodName());
		return isPageLoaded;
	}

	protected boolean waitForPageLoadDepot(boolean isPageLoaded) {
		By depotLoadingObject = By.xpath(
				"//h3/strong[contains(.,'DirektDepot')] | //h1[contains(.,'Login')] | //h1[contains(.,'Fondssparen')]");

		actionStartedLog(Utilities.getCallerMethodName());

		if (isElementExistsCheckWithoutWait(depotLoadingObject)) {
			Utilities.waitFor(1000, "To load Depot application screen");
			logger.debug("Depot Application is loaded");
		} else {
			logger.debug("Depot Application is not loaded");
		}

		actionCompletedLog(Utilities.getCallerMethodName());

		return isPageLoaded;
	}

	protected boolean applicationSpecificPageLoadCheck(boolean isPageLoaded) {

		if (config.getProperty(PropertyConstants.PROJECT_NAME).equalsIgnoreCase("RC")) {
			isPageLoaded = waitForPageLoadRC(isPageLoaded);
		} else if (config.getProperty(PropertyConstants.PROJECT_NAME).equalsIgnoreCase("FACE")) {
			isPageLoaded = waitForPageLoadFACE(isPageLoaded);
		}

		else if (config.getProperty(PropertyConstants.PROJECT_NAME).equalsIgnoreCase("Depot")) {
			isPageLoaded = waitForPageLoadDepot(isPageLoaded);
		}
		return isPageLoaded;
	}

	public void takeScreenshot() {
		takeScreenshot(false);
	}

	public void takeScreenshotForAssertion() {
		takeScreenshot(true);
	}

	protected void takeScreenshot(boolean isAssertionStep) {
		try {
			if (actionPerformed.toLowerCase().contains("assert")) {
				logger.debug("Screenshot taken in previous action itself");
			} else {
				if ((!takeScreenshotForAssetionStepsAlone || isAssertionStep) &&(driver != null)) {
						byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
						currentScenario.embed(screenshot, "image/png");
						logger.debug("Screenshot taken");

				}
			}
		} catch (NoSuchSessionException e) {
			logger.error("ScreenShot Not Taken due to the screen got closed.");
		} catch (Exception e) {
			logger.error("ScreenShot Not Taken - Generic Exceptin"+ e.getMessage());
		}
	}

	protected boolean clickByJavaScript(By locator) {
		actionStartedLog(Utilities.getCallerMethodName());
		boolean isClicked;
		WebElement webElement = getElement(locator);
		try {
			Set<String> windowsBeforeClick = getWindowHandles();
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript(JAVASCRIPT_ARGUMENTS_0_CLICK, webElement);

			logger.debug(CLICKED_ON_ELEMENT + locator.toString());
			setActionPerformed(CLICK);
			logger.debug("Click by JavaScript on " + locator);
			switchWindowIfExists(windowsBeforeClick);
			isClicked = true;
		} catch (Exception exception) {
			throw new CucumberException(exception);
		}
		waitForPageToLoad();
		action.takeScreenshot();
		actionCompletedLog(Utilities.getCallerMethodName());
		return isClicked;
	}

	protected WebElement getElement(By locator, boolean throwExceptionOnFail, boolean waitForElementPrecence)
	{
		return getElement(locator, throwExceptionOnFail, waitForElementPrecence, 0);
	}

	protected WebElement getElement(By locator, boolean throwExceptionOnFail, boolean waitForElementPrecence,
			int reSearchCount) {
		actionStartedLog(Utilities.getCallerMethodName());

		WebElement identifiedElement = null;
		boolean isElementPresentOnPage = false;
		int retryCount = 0;
		List<WebElement> elements;
		logger.debug("Last Action performed is " + actionPerformed + " and started to Search " + locator);
		elements = getElements(locator);

		if (elements.isEmpty() && waitForElementPrecence) {
			while (!isElementPresentOnPage && (actionPerformed.contains(CLICK) || actionPerformed.contains("Switch"))
					&& retryCount < 3) {
				isElementPresentOnPage = waitForElementPrecence(locator);
				retryCount++;
			}
		}
		elements = getElements(locator);

		if (elements.isEmpty() &&throwExceptionOnFail) {
			CucumberException exception= new CucumberException("Not able to identify Object using :" + locator.toString());
		//	logger.error(exception);
		//	throw exception;
		}

		for (WebElement currentElement : elements) {
			if (currentElement.isDisplayed()) {
				identifiedElement = currentElement;
				logger.debug("Object Identified:" + locator);
				break;
			}
		}
		if (identifiedElement == null && !elements.isEmpty()) {
			logger.error("Identified is in Hidden Mode:" + locator);
			if (throwExceptionOnFail && waitForElementPrecence && reSearchCount < 3) {
				reSearchCount++;
				logger.debug("Retry.." + reSearchCount);
				identifiedElement = getElement(locator, throwExceptionOnFail, waitForElementPrecence, reSearchCount);
			} else {
				identifiedElement = elements.get(0);
			}
		}
		actionCompletedLog(Utilities.getCallerMethodName());
		return identifiedElement;
	}

	private boolean waitForElementPrecence(By locator) {
		boolean isObjectPresent = false;
		actionStartedLog(Utilities.getCallerMethodName());

		if (actionPerformed.toLowerCase().contains("click") || actionPerformed.toLowerCase().contains("switch")) {
			Utilities.waitFor(2000);
			double waitedTime = 0.0;
			setImplicitWait(0);
			while (waitedTime <= pageLoadWaitTime && !isObjectPresent) {
				Utilities.waitFor(500, "Load Object:" + locator);
				waitedTime = waitedTime + 0.5;
					driver.findElement(locator);
					isObjectPresent = true;
					if(isObjectPresent) break;
			}
			setImplicitWait(implicitWaitTime);
			setActionPerformed("Waited for Page Load");
		}

		actionCompletedLog(Utilities.getCallerMethodName());
		return isObjectPresent;
	}

	protected void waitForElementToDisappear(By locator) {
		boolean isObjectPresent = true;
		double waitedTime = 0.0;

		actionStartedLog(Utilities.getCallerMethodName());

		setImplicitWait(0);
		while (isObjectPresent && waitedTime <= pageLoadWaitTime) {
			Utilities.waitFor(500, "Object:" + locator + " to Disappear");
			waitedTime = waitedTime + 0.5;
			try {
				driver.findElement(locator);

			} catch (ElementNotVisibleException e) {
				isObjectPresent = false;
				logger.info("Element disappeared = true");

			} catch (Exception e) {
				logger.error(e);
			}
		}
		setImplicitWait(implicitWaitTime);
		setActionPerformed( "Waited for element to disappear");
		actionCompletedLog(Utilities.getCallerMethodName());
	}

	protected void waitForVisibleText(String visibleTextInPage) {

		actionStartedLog(Utilities.getCallerMethodName());
		logger.debug("Last action performed is " + actionPerformed);

		if (actionPerformed.toLowerCase().contains("click") || actionPerformed.toLowerCase().contains("switch")) {
			Utilities.waitFor(2000);
			double waitedTime = 0.0;

			while (waitedTime <= pageLoadWaitTime) {
				Utilities.waitFor(500, "text " + visibleTextInPage + " precent on Page");
				waitedTime = waitedTime + 0.5;
				String pageSource = driver.getPageSource();

				if (pageSource.contains(visibleTextInPage)) {
					break;
				}
			}
			logger.debug("Waited for " + currentBrowser.getName() + " browser Page Load:" + waitedTime);
			setActionPerformed("Waited for Page Load");
		}

		actionCompletedLog(Utilities.getCallerMethodName());
	}

	protected List<WebElement> getElements(String logicalName)  {
		return getElements(ObjectRepository.get(logicalName));
	}

	protected List<WebElement> getElements(By locator) {
		return driver.findElements(locator);
	}

	public void switchToNewWindow() {
		try {

			Set<String> openWindows = getWindowHandles();
			String newWindowHandle = "";

			if (!openWindows.isEmpty() && openWindows.size() > 1) {

				for (String currentWindow : openWindows) {
					if (!(currentWindow.equals(getMainWindowHandle())
							|| currentWindow.equals(getWorkingWindowHandle()))) {
						newWindowHandle = currentWindow;
					}
				}
			}

			if (!newWindowHandle.equals("")) {
				switchToWindow(newWindowHandle);
				logger.debug("Switched to new Window '" + newWindowHandle + "'");
				waitForPageToLoad();
				takeScreenshot();
			}

		} catch (Exception exception) {
			logger.error(EXCEPTION_ON+Utilities.getCallerMethodName()+" :",exception);


		}
	}

	public boolean switchToWindow(String nameOrHandle) {
		boolean isGivenWindowExists = false;
		try {
			driver.switchTo().window(nameOrHandle);
			logger.debug("Switched to window :" + nameOrHandle);
			setActionPerformed("SwitchedToWindow");
			setWorkingWindowHandle(nameOrHandle);
			isGivenWindowExists = true;
			waitForPageToLoad();

		} catch (Exception exception) {
			logger.error(EXCEPTION_ON+Utilities.getCallerMethodName()+" :",exception);

		} finally {
			action.takeScreenshot();
		}
		return isGivenWindowExists;
	}

	protected boolean isElementExists(By locator, boolean throwExceptionOnFail, boolean waitForElementPrecence) {
		actionStartedLog(Utilities.getCallerMethodName());
		boolean isElementFound = false;
		WebElement element;

		try {
			element = getElement(locator, throwExceptionOnFail, waitForElementPrecence);
			if (element != null &&element.isDisplayed()) {
				isElementFound = true;
				logger.info("Element " + locator.toString() + " exists = true");
			}
		} catch (Exception e) {
			logger.error("Exception while finding :" + locator);
		}
		if (isElementFound) {
			logger.debug("Object found :" + locator);
		} else {
			logger.debug("Object Not found :" + locator);
		}
		actionCompletedLog(Utilities.getCallerMethodName());
		return isElementFound;
	}

	protected String getXpathForVisibleTest(String visibleText){
		return ("//*[text()='" + visibleText + "' or normalize-space(text())='"+visibleText+"' or @content-desc='"+visibleText+"']");
	}



	protected By getlocatorForVisibleText(String visibleText) {
		return By.xpath(getXpathForVisibleTest(visibleText));

	}



	protected void closeAllWindow(Set<String> windowHandles) {
		try {
			logger.debug(windowHandles.size() + " Window to Close.");
			Iterator<String> i = windowHandles.iterator();
			String currentWindowHandle;
			while (i.hasNext()) {
				currentWindowHandle = i.next();
				driver.switchTo().window(currentWindowHandle);
				driver.close();
			}
			windowHandles = getWindowHandles();
			if (windowHandles.size() == 1) {
				logger.info("Closed all windows..");
				driver.quit();
			}
		} catch (Exception exception) {
			if (driver != null) {
				driver.quit();
			}
			logger.error(EXCEPTION_ON+Utilities.getCallerMethodName()+" :",exception);
		}
	}

	protected static void deleteCookies() {
		try {
			if (applicationTypeToExecute == ApplicationType.MOBILEWEB) {
				driver.manage().deleteAllCookies();
				logger.info("Cookies deleted..");
			}
		} catch (Exception exception) {
			logger.error(EXCEPTION_ON+Utilities.getCallerMethodName()+" :",exception);
		}

	}

	protected boolean isAlertPresent() {
		boolean isPresent = false;
		try {
			driver.switchTo().alert();
			isPresent = true;
			logger.info("Alert is present..");
		} catch (Exception e) {
			logger.debug(DO_NOTHING);
		}
		return isPresent;
	}

	protected boolean acceptAlertIfPresent() {
		boolean isAlertPresentAndAccepted = false;

		if (!FrameworkData.isWindows()) {
			Utilities.waitFor(3000);
		}
		try {
			driver.switchTo().alert().accept();
			isAlertPresentAndAccepted = true;
			Utilities.waitFor(2000, "Alert Accepted.");
			waitForPageToLoad();
			setActionPerformed("AcceptAlert");
			logger.info("Alert accepted..");
		} catch (Exception e) {
			logger.debug(DO_NOTHING);
		}
		return isAlertPresentAndAccepted;
	}

	protected boolean isElementExistsCheckWithoutWait(By locator) {
		actionStartedLog(Utilities.getCallerMethodName());
		boolean isElementFound;
		setImplicitWait(5000);
		isElementFound = isElementExists(locator, false, false);
		setImplicitWait(implicitWaitTime);
		actionCompletedLog(Utilities.getCallerMethodName());
		return isElementFound;
	}

	protected boolean isDropDownHaveOption(Select sltDropDown, String option) {
		boolean isOptionAvailable = false;
		List<WebElement> options = sltDropDown.getOptions();
		for (WebElement weOptions : options) {
			if (weOptions.getText().trim().equals(option)) {
				isOptionAvailable = true;
				logger.info("Dropdown have the option " + option);
				break;
			}
		}
		return isOptionAvailable;
	}

	public Set<String> getWindowHandles() {
		Set<String> windowHandles = new HashSet<>();
		try {
			windowHandles = ((RemoteWebDriver) driver).getWindowHandles();
			logger.debug("Windows found= " + windowHandles.size());
		} catch (Exception e) {
			logger.debug(DO_NOTHING);
		}
		return windowHandles;
	}

	public String getWindowHandle() {
		String windowHandle = "";
		try {
			windowHandle = ((RemoteWebDriver) driver).getWindowHandle();
			logger.debug("Active window= " + windowHandle);
		} catch (Exception exception) {
			logger.error(EXCEPTION_ON+Utilities.getCallerMethodName()+" :",exception);
		}
		return windowHandle;
	}

	/***
	 * To switch to the base window
	 *
	 * @param windowHandle - Main window name
	 */
	@Override
	public void setMainWindowHandle(String windowHandle)
	{
		setMainWindowHandler(windowHandle);
		setWorkingWindowHandle(windowHandle);
	}


	public static String getWorkingWindowHandle() {
		return workingWindowHandle;
	}

	public static void setWorkingWindowHandle(String workingWindowHandle) {
		SeleniumActions.workingWindowHandle = workingWindowHandle;
	}

	/***
	 * To get the parent window name
	 *
	 * @return - Parent window name
	 */
	@Override
	public String getMainWindowHandle() {
		return mainWindowHandle;
	}

	public void setImplicitWait(int waitInSeconds) {
		driver.manage().timeouts().implicitlyWait(waitInSeconds, TimeUnit.SECONDS);
		logger.debug("Implicit Wait set to " + waitInSeconds + " Seconds");
	}
}
