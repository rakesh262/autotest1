package sqs.core.actions.mobile;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import sqs.core.utils.LoggerUtils;
import sqs.core.utils.Utilities;

import java.util.HashMap;
import java.util.Map;

import static sqs.core.constants.Constants.EXCEPTION_ON;
import static sqs.core.utils.Utilities.getCallerMethodName;

public class MobileIOSActions extends MobileActions {
    private static Logger logger = Logger.getLogger(MobileIOSActions.class);

	@Override
	public void swipeDown() {
        swipe("down");
	}

	@Override
	public void swipeUp() {
        swipe("up");
    }

    private void swipe(String direction) {
        try {
            LoggerUtils.actionStartedLog(getCallerMethodName() + direction);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            Map<String, Object> params = new HashMap<>();
            params.put("direction", direction.toLowerCase());
            js.executeScript("mobile: scroll", params);
            Utilities.waitFor(4000);
            LoggerUtils.actionCompletedLog(getCallerMethodName() + direction);
        } catch (Exception exception) {
            logger.error(EXCEPTION_ON + getCallerMethodName() + direction + " :", exception);
        }
    }

    @Override
    public void scrollToElement(String logicalName) {
        LoggerUtils.actionStartedLog(getCallerMethodName());
        WebElement element = action.getElement(logicalName);
        HashMap<String, String> scrollObject = new HashMap<>();
        scrollObject.put("element", ((RemoteWebElement) element).getId());
        ((JavascriptExecutor) driver).executeScript("mobile: scrollTo", scrollObject);
        LoggerUtils.actionCompletedLog(getCallerMethodName());

    }

}
