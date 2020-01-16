package sqs.core.actions.mobile;

import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.log4j.Logger;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import sqs.core.utils.Utilities;

import static sqs.core.constants.Constants.EXCEPTION_ON;

public class MobileAndroidActions extends MobileActions {
	private static Logger logger = Logger.getLogger(MobileAndroidActions.class);

	@Override
	public void swipeDown() {
		swipeDown(150);
	}

	@Override
	public void swipeUp() {
		swipeUp(150);
	}

	protected void swipeDown(int length) {
		try {
			logger.debug("Swipe Down Process started");
			switchToNative();
			TouchAction touch = new TouchAction((PerformsTouchActions) driver);
			touch.press(PointOption.point(50, 400));
			touch.moveTo(PointOption.point(0, -length));
			touch.release();
			touch.perform();
			Utilities.waitFor(2000);
			logger.debug("Swipe Down Process Completed");
		} catch (Exception exception) {
			logger.error(EXCEPTION_ON+Utilities.getCallerMethodName()+" :",exception);
		}
		switchToWebView();
	}

	protected void swipeUp(int length) {
		try {
			logger.debug("SwipeUp Process started");
			switchToNative();
			TouchAction touch = new TouchAction((PerformsTouchActions) driver);
			touch.press(PointOption.point(250, 600));
			touch.moveTo(PointOption.point(0, length));
			touch.release();
			touch.perform();
			logger.debug("SwipeUp Process Completed");
			Utilities.waitFor(2000);
		} catch (Exception exception) {
			logger.error(EXCEPTION_ON+Utilities.getCallerMethodName()+" :",exception);
		}
		switchToWebView();
	}

	@Override
	public void scrollToElement(String logicalName) {
		logger.debug("scrollToElement Process started");
		Point elementLocation;
		boolean isElementVisible = false;
		WebElement element = action.getElement(logicalName);
		elementLocation = element.getLocation();
		int elementYPosition = elementLocation.y + (element.getSize().getHeight() / 2);
		int swipeCount = 0;
		while (!isElementVisible && swipeCount < 10) {
			if (elementYPosition < 5) {
				swipeUp();
			} else if (elementYPosition > deviceHeight) {
				swipeDown();
			}
			element = action.getElement(logicalName);
			elementLocation = element.getLocation();
			elementYPosition = elementLocation.y;
			if (elementYPosition > 5 && elementYPosition < deviceHeight) {
				isElementVisible = true;
			}
			swipeCount++;
		}
		logger.debug("scrollToElement Process completed");
	}
}
