package sqs.core.actions.mobile;

import cucumber.runtime.CucumberException;
import io.appium.java_client.*;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import sqs.core.actions.CommonActions;
import sqs.core.constants.Browsers.Browser;
import sqs.core.constants.PropertyConstants;
import sqs.core.utils.LoggerUtils;
import sqs.core.utils.ObjectRepository;
import sqs.core.actions.driver.utils.SeleniumUtils;
import sqs.core.utils.TestDataHandler;
import sqs.core.utils.Utilities;

import java.util.Set;

import static io.appium.java_client.touch.TapOptions.tapOptions;
import static io.appium.java_client.touch.offset.ElementOption.element;
import static sqs.core.constants.Constants.*;
import static sqs.core.utils.TestDataHandler.getData;

public class MobileActions extends CommonActions {
    private static Logger logger = Logger.getLogger(MobileActions.class);

    @Override
    public void enterText(String value, String logicalName) {
        LoggerUtils.actionStartedLog(Utilities.getCallerMethodName());
        try {
            WebElement element = getElement(ObjectRepository.get(logicalName), true, true);
            if (element.isEnabled()) {
                value = getData(value);
                element.sendKeys(value);
                logger.info("Value '" + value + "' entered in " + logicalName);
            } else {
                throw new CucumberException(logicalName + " is not visible to enter text.");
            }
        } catch (Exception e) {
            throw new CucumberException(e.getMessage(), e);
        }
        LoggerUtils.actionCompletedLog(Utilities.getCallerMethodName());

    }

    @Override
    public void switchOffGoogleTranslate() {
        Utilities.waitFor(2000);
        if (currentBrowser == Browser.CHROME) {
            try {
                ((AppiumDriver<?>) driver).context(NATIVE_APP);

                By locator = By.xpath("//android.widget.ImageButton[@content-desc='More options']");
                clickIfExists(locator);

                locator = By.xpath("//android.widget.TextView[@text='Settings']");
                clickIfExists(locator);

                locator = By.xpath("//android.widget.TextView[@text='Site settings']");
                clickIfExists(locator);
                swipeDown();

                locator = By.xpath("//android.widget.TextView[@text='Google Translate']");
                clickIfExists(locator);

                locator = By.xpath("//android.widget.Switch[@text='ON']");
                clickIfExists(locator);
                locator = By.xpath(
                        "//android.widget.ImageButton[@content-desc='Navigate up']|//android.widget.ImageView[@resource-id='android:id/up']");
                clickIfExists(locator);
                clickIfExists(locator);
                clickIfExists(locator);

            } catch (Exception exception) {
                logger.error(EXCEPTION_ON + Utilities.getCallerMethodName() + " :" ,exception);
            } finally {
                switchToWebView();
            }

        }
    }

    @Override
    public void dismissKeyBoard() {
        LoggerUtils.actionStartedLog(Utilities.getCallerMethodName());
        try {
            ((AppiumDriver<?>) driver).hideKeyboard();
            Utilities.waitFor(500);
        } catch (Exception e) {
            if (!e.getMessage().contains("Soft keyboard not present, cannot hide keyboard")) {
                throw new CucumberException(e.getMessage(), e);
            }
        }
        LoggerUtils.actionCompletedLog(Utilities.getCallerMethodName());
    }

    @Override
    public boolean touch(String logicalName) {
        LoggerUtils.actionStartedLog(Utilities.getCallerMethodName());
        TouchAction ta = new TouchAction((MobileDriver) driver);
        MobileElement mobileElement = (MobileElement) action.getElement(logicalName);
        ta.tap(tapOptions().withElement(element(mobileElement))).perform();
        LoggerUtils.actionCompletedLog(Utilities.getCallerMethodName());
        return true;
    }

    protected boolean touch(WebElement element) {
        boolean isElementClicked;
        try {
            LoggerUtils.actionStartedLog(Utilities.getCallerMethodName());
            Set<String> windowsBeforeTouch = getWindowHandles();
            takeScreenshot();
            new TouchAction((PerformsTouchActions) driver)
                    .tap(PointOption.point(element.getLocation().getX(), element.getLocation().getY())).perform();
            isElementClicked = true;
            waitForPageToLoad();
            Utilities.waitFor(2000);
            Set<String> windowsAfterTouch = getWindowHandles();
            if (windowsBeforeTouch.size() == windowsAfterTouch.size()) {
                takeScreenshot();
            } else {
                switchToNewWindow();
            }
        } catch (Exception e) {
            takeScreenshot();
            throw new CucumberException(e);
        } finally {
            switchToWebView();
        }

        setActionPerformed(CLICK);
        LoggerUtils.actionCompletedLog(Utilities.getCallerMethodName());
        return isElementClicked;
    }

    @Override
    public boolean click(String logicalName) {
        return touch(logicalName);
    }

    protected boolean click(String logicalName, int retryCount) {
        LoggerUtils.actionStartedLog(Utilities.getCallerMethodName());
        boolean isElementClicked = false;
        try {
            isElementClicked = clickAndSwitchWindowIfExists(ObjectRepository.get(logicalName));
            setActionPerformed(CLICK);
        } catch (ElementNotVisibleException e) {
            clickWhileElementNotVisibleException(e,logicalName,retryCount);
        } catch (WebDriverException e) {
            clickWhileWebDriverException(e,logicalName,retryCount);
        } catch (Exception e) {
            takeScreenshot();
            throw new CucumberException(e);
        }
        setActionPerformed(CLICK);

        LoggerUtils.actionCompletedLog(Utilities.getCallerMethodName());
        return isElementClicked;
    }

    void clickWhileElementNotVisibleException(ElementNotVisibleException e,String logicalName, int retryCount) {
        if ((e.getMessage().contains("element not visible") && (retryCount < 3))) {
            if (retryCount < 3) {
                retryCount++;
                action.swipeDown();
                Utilities.waitFor(2000);
                click(logicalName, retryCount);
            } else {
                throw new CucumberException(e);
            }
        }
    }

    void clickWhileWebDriverException(WebDriverException e,String logicalName, int retryCount){
        if (e.getMessage().contains("is not clickable at point")) {
            if (retryCount < 10) {
                retryCount++;
                int yPoint = SeleniumUtils.getYpointFromMesssage(e.getMessage());
                logger.debug("Object is not found in VisibleScreen it is available in Y Position:" + yPoint);
                logger.debug("Swipe and retry.." + retryCount);
                if (yPoint > 100) {
                    action.swipeDown();
                } else {
                    action.swipeUp();
                }
                click(logicalName, retryCount);
            } else {
                logger.error(e.getMessage());
                throw new CucumberException(e.getMessage());
            }
        } else if (e.getMessage().contains("OTHER element would receive the click")) {
            if (retryCount < 10) {
                logger.debug("Object is Overlapped by another field. swipe down to find bring the element front");
                retryCount++;
                swipeDown();
                click(logicalName, retryCount);
            } else {
                logger.error(e.getMessage());
                throw new CucumberException(e.getMessage());
            }
        } else {
            throw e;
        }
    }

    @Override
    public void openApplication() {
        try {
            String applicationURL = config.getProperty(PropertyConstants.APPLICATION_URL);
            openURL(applicationURL);

        } catch (Exception e) {
            throw new CucumberException(e);
        }
    }

    @Override
    public boolean clickOnVisibleText(String visibleText) {
        LoggerUtils.actionStartedLog(Utilities.getCallerMethodName());
        ObjectRepository.add("VisibleText " + visibleText,"xpath", getXpathForVisibleTest(visibleText));
        boolean isClicked = action.clickByJavaScript("VisibleText " + visibleText);
        LoggerUtils.actionCompletedLog(Utilities.getCallerMethodName());
        return isClicked;
    }

    @Override
    public void switchToNative() {
        LoggerUtils.actionStartedLog(Utilities.getCallerMethodName());
        ((AppiumDriver<?>) driver).context(NATIVE_APP);
        logger.info("Driver Context Set to :"+NATIVE_APP);
        LoggerUtils.actionCompletedLog(Utilities.getCallerMethodName());
    }

    @Override
    public void switchToWebView() {
        LoggerUtils.actionStartedLog(Utilities.getCallerMethodName());
        Set<String> contextNames = ((AppiumDriver<?>) driver).getContextHandles();
        for (String contextName : contextNames) {
            if (contextName.contains("WEBVIEW") || !contextName.toLowerCase().contains(NATIVE_APP .toLowerCase())) {
                logger.info("Driver Context Set to :" + contextName);
                ((AppiumDriver<?>) driver).context(contextName);
            }
        }
        LoggerUtils.actionCompletedLog(Utilities.getCallerMethodName());
    }

}
