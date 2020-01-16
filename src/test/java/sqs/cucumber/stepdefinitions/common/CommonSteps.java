package sqs.cucumber.stepdefinitions.common;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.CucumberException;
import org.apache.log4j.Logger;
import sqs.core.constants.Constants;
import sqs.core.constants.PropertyConstants;
import sqs.core.utils.TestDataHandler;
import sqs.framework.FrameworkBase;
import sqs.core.utils.Utilities;

import java.util.Set;

import static sqs.core.utils.TestDataHandler.getData;

/**
 * @scriptName : Utilities
 * @description : This class contains Commonly used Keyword for Mobile/Web application automation using reports base
 * @author uthor : Srirangan
 * @creation Date : 23 May 2016 @Modified Date:
 */
public class CommonSteps extends FrameworkBase {
    private static Logger logger = Logger.getLogger(CommonSteps.class);

    @Given("^My WebApp '(.*)' is open$")
    public void myWebApplicationIsOpen(String applicationName)  {
        logger.debug("openApplication method Started for "+applicationName);
        try {
            String activeURL = driver.getCurrentUrl();
            logger.debug(activeURL);
            launchURL();
            logger.debug("Open URL process completed.");
            action.setMainWindowHandle(action.getWindowHandle());
        } catch (Exception exception) {
            logger.error(Constants.EXCEPTION_ON+Utilities.getCallerMethodName()+" :",exception);
            throw new CucumberException(exception);
        }
        logger.debug("openApplication method Ended");
    }

    private void launchURL() {
        boolean isURLLoaded = false;
        String activeURL;
        int retryCount = 1;
        String applicationURL = config.getProperty(PropertyConstants.APPLICATION_URL);
        while (!isURLLoaded && retryCount < 3) {
            action.openApplication();
            activeURL = driver.getCurrentUrl();
            if (activeURL.contains(applicationURL)) {
                isURLLoaded = true;
            }
            retryCount++;
        }

    }

    @Then("^I click '(.*)'$")
    public void iClick(String logicalName) {
        action.click(logicalName);
    }

    @Then("^I touch '(.*)'$")
    public void iTouch(String element) {
        action.touch(element);
    }

    @Then("^I click on the '(.*)' Link$")
    public void iClickLink(String link) {
        action.click(link);
    }

    @Given("^My NativeApp '(.*)' is open$")
    public void myNativeappIsOpen(String app) {
        logger.debug("Application" + app + " is launched");
        action.openApplication();
    }

    @When("^I enter '(.*)' in field '(.*)'$")
    public void iEnterInField(String value, String element) {
        action.enterText(value, element);
    }

    @When("^I enter '(.*)' in field '(.*)' and wait$")
    public void iEnterInFieldAndWait(String value, String element) {
        action.enterText(value, element);
        Utilities.waitFor(Constants.WAIT_FOR_SHORT_ACTION, "After Entering Text");
    }

    @Then("^I clear '(.*)' field value$")
    public void iClearTheField(String element) {
        action.clearText(element);
    }

    @Then("^I clear and enter '(.*)' in field '(.*)'$")
    public void iClearAndEnterTheField(String value, String element) {
        action.clearText(element);
        action.enterText(value, element);
    }

    @When("^I enter stored value in field '(.*)'$")
    public void iEnterStoredValueInField(String element) {
        action.enterSavedValue(element);
    }

    @When("^I click by JS '(.*)'$")
    public void iClickJS(String element) {
        action.clickByJavaScript(element);
    }

    @When("^I hit Enter key on '(.*)'$")
    public void iHitEnterkey(String element) {
        action.clickEnter(element);
    }

    @When("^I hit Tab key on '(.*)'$")
    public void iHitTabkey(String element) {
        action.clickTabKey(element);
    }

    @When("^I hit Down key on '(.*)'$")
    public void iHitDownkey(String element) {
        action.clickTabKey(element);
    }

    @Then("^I should see '(.*)' field on page$")
    public void iShouldSeeOnPage(String value) {
        action.assertElementDisplayed(value);
    }

    @Then("^I should not see '(.*)' field on page$")
    public void iShouldNotSeeOnPage(String element) {
        action.assertElementNotDisplayed(element);
    }

    @Then("^I should see text '(.*)' present on page$")
    public void iShouldSeeTextPresentOnPage(String expectedText) {
        expectedText = getData(expectedText);
        action.assertTextDisplayed(expectedText);
    }

    @Then("^I should see text matching regx '(.*)' present on page at '(.*)'$")
    public void iShouldSeeTextMatchingRegularExpression(String regx, String logicalName) {
        action.assertElementValueWithRegularExpression(regx, logicalName);
    }

    @Then("^I wait '(.*)' seconds for presence of element '(.*)'$")
    public void iWaitForPresenceOfElement(int seconds, String element) {
        action.waitForElementToDisplay(element, seconds);
    }

    @Then("^I dismiss keyboard$")
    public void iDismissKeyboard() {
        action.dismissKeyBoard();
    }

    @Then("^I store '(.*)' field value$")
    public void iGetTextFrom(String element) {
        action.saveElementText(element);
    }

    @Then("^I verify '(.*)' value should be 'Increased'$")
    public void assertValueIncreased(String value) {
        action.assertElementValueIncreased(value);
    }

    @Then("^I verify '(.*)' value should be 'Decreased'$")
    public void assertValueDecreased(String value) {
        action.assertElementValueDecreased(value);
    }

    @And("^I store the details '(.*)' in text file '(.*)'$")
    public void iSaveDetails(String logicalName, String fileName) {
        throw new PendingException();
    }

    @And("^I get the details from text file '(.*)'$")
    public void iGetDetails(String fileName) {
        throw new PendingException();
    }

    @Then("^I verify '(.*)' field presence in page '(.*)'$")
    public void iVerifyFieldPresenceInPage(int seconds, String element) {
        action.assertElementDisplayed(element, seconds);
    }

    @Then("^I switch to window with title '(.*)'$")
    public void iSwitchToWindowWithTitle(String title) {
        action.switchToWindow(title);
    }

    @Then("^I select option '(.*)' in dropdown '(.*)' by '(.*)'$")
    public void iSelectOptionInDropdownBy(String option, String element, String selectionType) {
        action.selectDropDownOption(element, option, selectionType);
    }

    @Then("^I scroll to '(.*)' - '(.*)'$")
    public void iScrollToElement(String scrolltype, String element) {
        throw new PendingException();
    }

    @Then("^I compare '(.*)' with stored value '(.*)'$")
    public void iCompareWithStoredValue(String elementValue, String variableName) {
        action.assertElementValueWithStoredValue(elementValue, variableName);
    }

    @Then("^I should see '(.*)' text in '(.*)' field$")
    public void iVerifyTextFrom(String expectedValue, String element) {
        action.assertElementValue(element, expectedValue);
    }

    @Then("^I should see '(.*)' text within '(.*)' field$")
    public void iVerifyElementContainsExpectedText(String expectedText, String element) {
        action.assertElementContainsText(element, expectedText);
    }

    @Then("^I should see '(.*)' text in '(.*)' fields$")
    public void iShouldSeeTextInFields(String expectedText, String element) {
        action.assertAnyElementHaveText(element, expectedText);
    }

    @Then("^I verify '(.*)' element is disabled$")
    public void iVerifyElementIsDisabled(String element) {
        action.assertElementDisabled(element);
    }

    @Then("^I switch to new window and click on '(.*)'$")
    public void iSwitchToWindowAndClick(String element) {
        action.switchToNewWindowAndClickElement(element);
    }

    @Then("^I switch to new window$")
    public void iSwitchToWindow() {
        action.switchToNewWindow();
    }

    @Then("^I switch back to main window$")
    public void iSwitchToMainWindow() {
        if (action.getMainWindowHandle() != null) {
            logger.debug("Switching to Main Window :" + action.getMainWindowHandle());
            action.switchToWindow(action.getMainWindowHandle());
        }
        logger.debug("switchToWindow method Ended");
    }

    @Then("^I switch to new window and verify text '(.*)' present at '(.*)'$")
    public void iSwitchToWindowAndVerifyTextPrecent(String expectedText, String logicalName) {
        action.assertTextInNewWindow(expectedText, logicalName);
    }

    @Then("^I navigate back to main window$")
    public void iSwitchBackToMainWindow() {
        action.switchToWindow(action.getMainWindowHandle());
    }

    @And("^I click '(.*)' and accept alert if exists$")
    public void iClickAndHandleAlert(String value) {
        Set<String> windowsBeforeClick = action.getWindowHandles();
        action.clickAndAcceptAlertIfExists(value);
        Set<String> windowsAfterClick = action.getWindowHandles();
        if (windowsBeforeClick.size() == windowsAfterClick.size()) {
            action.takeScreenshot();
        } else {
            action.switchToNewWindow();
        }
    }

    @Then("^I click on the element '(.*)' if exists$")
    public void iClickOnTheElementIfExists(String element){
        action.clickIfExists(element);
    }

    @Given("^I wait for '(.*)' seconds$")
    public void waitForgivenSeconds(String secondsToWait)  {
        int milliSecondsToWait = Utilities.getInteger(secondsToWait) * 1000;
        Utilities.waitFor(milliSecondsToWait, "For Given seconds");
    }

    @Then("^I swipe down$")
    public void swipeDown()  {
        action.swipeDown();
    }

    @Then("^I swipe down '(\\d+)' times$")
    public void swipeDownTimes(int numberOfTimes)  {
        logger.debug("swipedown " + numberOfTimes + " times method Started");
        for (int i = 0; i < numberOfTimes; i++) {
            action.swipeDown();
        }
        logger.debug("swipedown " + numberOfTimes + " times method Ended");
    }

    @Then("^I click on '(.*)' text$")
    public void iClickOnVisibleText(String visibleText) {
        action.clickOnVisibleText(visibleText);
    }

    @Then("^I click on '(.*)' text within '(.*)'$")
    public void iClickOnVisibleTextWithinElement(String expectedText, String element) {
        action.clickOnVisibleTextWithinElement(element, expectedText);
    }

    @Then("^I enter date into '(.*)' field$")
    public void iEnterDateIntoField(String element) {
        action.enterCurrentDate(element);
    }

    @Then("^I select '(.*)' in '(.*)' dropdown$")
    public void iSelectDropdown(String visibleText, String element) {
        action.selectDropDownOptionByText(element, visibleText);
    }

    @Then("^I select '(.*)' in '(.*)' dropdown and wait$")
    public void iSelectDropdownWait(String visibleText, String element) {
        iSelectDropdown(visibleText, element);
        Utilities.waitFor(Constants.WAIT_FOR_SHORT_ACTION, "After selecting Dropdown");
    }

    @Then("^I close child window$")
    public void iCloseWindow() {
        action.closeWindow();
    }

    @Then("^I should see '(.*)' value in '(.*)' field$")
    public void iShouldSeeValueInField(String expectedValue, String element) {
        action.scrollToElement(element);
        action.assertElementValue(element, expectedValue);
    }

    @Then("^I should not see nil value in field '(.*)'$")
    public void iShouldNotSeeNilValueInField(String element) {
        action.assertElementHasAnyValue(element);
    }

    @Given("^My NativeApps '(.*)' is open$")
    public void myNativeapp(String app)  {
        action.openApp();
    }

}


