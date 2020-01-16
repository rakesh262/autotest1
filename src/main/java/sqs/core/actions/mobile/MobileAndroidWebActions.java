package sqs.core.actions.mobile;

import cucumber.runtime.CucumberException;
import org.apache.commons.lang.math.NumberUtils;
import org.junit.Assert;

import static sqs.core.utils.LoggerUtils.actionCompletedLog;
import static sqs.core.utils.LoggerUtils.actionStartedLog;
import static sqs.core.utils.Utilities.getCallerMethodName;

public class MobileAndroidWebActions extends MobileAndroidActions {

	@Override
	public void assertElementOnPage(int seconds, String element) {
		try {
			waitForElementToDisplay(element, seconds);
		} catch (CucumberException e) {
			throw new CucumberException(e.getMessage(), e);
		}
	}

	@Override
	public void selectOptionByValue(String option, String logicalName) {
		try {
			Assert.assertTrue("Element " + logicalName + "is not found", waitForElementToDisplay(logicalName));
			selectDropDownOption(logicalName, option, "Value");
		} catch (CucumberException e) {
			throw new CucumberException(e.getMessage(), e);
		}
	}

	@Override
	public void scrollForElement(String scrolltype, String element) {
		try {
			scroll(scrolltype, element);

		} catch (CucumberException e) {
			throw new CucumberException(e.getMessage(), e);
		}
	}

	@Override
	public void assertElementValueWithStoredValue(String logicalName, String variableName) {
		try {
			Assert.assertTrue("Element " + logicalName + "is not found", waitForElementToDisplay(logicalName));
			String actualValue = action.getElementText(logicalName);
			String expectedValue = getSavedData(variableName);
			if (!NumberUtils.isNumber(actualValue)) {
				actualValue = actualValue.split(" ", 2)[0].replace(",", "");
				expectedValue = expectedValue.split(" ", 2)[0].replace(",", "");
			}
			Assert.assertEquals(actualValue, expectedValue);

		} catch (Exception e) {
			throw new CucumberException(e.getMessage(), e);
		}

	}

	@Override
	public boolean click(String logicalName) {
		actionStartedLog(getCallerMethodName());

		boolean isClicked = action.clickByJavaScript(logicalName);
		actionCompletedLog(getCallerMethodName());
		return isClicked;
	}

}
