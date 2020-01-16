package sqs.core.actions.interfaces;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public interface DesktopActionsInterface {
	void openApplication();

	/***
	 * To Click on Element
	 * 
	 * @param logicalName The field name mentioned in the ObjectRepository file
	 * @return <b>True</b> if clicked without any issue <b>False</b> if any
	 *         error.<br>
	 *         eg. <code> action.click('Login')</code>
	 */
	boolean click(String logicalName);

	/***
	 * To Click on an Element if it's present in the screen
	 * 
	 * @param logicalName The field name mentioned in the ObjectRepository file
	 * @return <b>True</b> if the objects exists and gets clicked without any
	 *         issues. <br>
	 *         <b>False</b> if the object doesn't exist <br>
	 *         Eg: <code>action.clickIfExists('Login')</code>
	 */
	boolean clickIfExists(String logicalName);

	/***
	 * To Click on an Element using JavaScript
	 * 
	 * @param logicalName - The field name mentioned in the ObjectRepository file
	 * @return <b>True</b> if clicked without any issues <br>
	 *         <b>False</b> if any error.<br>
	 *         Eg: <code> action.clickByJavaScript('Login')</code>
	 */
	boolean clickByJavaScript(String logicalName);

	/***
	 * To Right click on an element
	 * 
	 * @param logicalName - The field name mentioned in the ObjectRepository file
	 * @return <b>True</b> if clicked without any issues <br>
	 *         <b>False</b> if any error.<br>
	 *         Eg: <code> action.clickByContextClick('Login')</code>
	 */
	boolean clickByContextClick(String logicalName);

	/***
	 * To click on an element using Mouse actions
	 * 
	 * @param logicalName - The field name mentioned in the ObjectRepository file
	 * @return <b>True</b> if clicked without any issues <br>
	 *         <b>False</b> if any error.<br>
	 *         Eg: <code> action.clickByMouse('Login')</code>
	 */
	boolean clickByMouse(String logicalName);

	/***
	 * To press the Enter key from keyboard, on an element
	 * 
	 * @param logicalName - The field name mentioned in the ObjectRepository file
	 * @return <b>True</b> if pressed enter key without any issues <br>
	 *         <b>False</b> if any error.<br>
	 *         Eg: <code> action.clickEnter('Login')</code>
	 */
	boolean clickEnter(String logicalName);

	/***
	 * To click on a text in the pages
	 * 
	 * @param visibleText - The text to be clicked
	 * @return <b>True</b> if clicked on visible text without any issues <br>
	 *         <b>False</b> if any error.<br>
	 *         Eg: <code> action.clickOnVisibleText('Login')</code>
	 */
	boolean clickOnVisibleText(String visibleText);

	/***
	 * To click on a text if displayed else ignore the action.
	 * 
	 * @param visibleText - The text to click
	 * @return <b>True</b> if the text present in the screen and clicked without any
	 *         issues <br>
	 *         <b>False</b> if the text is not present in the screen.<br>
	 *         Eg: <code> action.clickOnVisibleTextIfExists('Login')</code>
	 */
	boolean clickOnVisibleTextIfExists(String visibleText);

	/***
	 * To click on an element which has the given text from the group of element.
	 * 
	 * @param logicalName  - The PageObjects which return multiple elements.
	 * @param expectedText - Expected text that the element should have.
	 * @return <b>True</b> if the 'expected text' found within the 'logicalName'
	 *         (list if elements)<br>
	 *         <b>False</b> if the 'expected text' doesn't found within the
	 *         'logicalName' (list if elements)<br>
	 *         Eg:
	 *         <code> action.clickOnVisibleTextWithinElement('ProductTypesList','PersonalLoan')</code>
	 */
	boolean clickOnVisibleTextWithinElement(String logicalName, String expectedText);

	/***
	 * To click and hold the TextBox based on it's Index
	 * 
	 * @param index - The index of the TextBox to be clicked
	 * @return <b>True</b> if click and holded on TextBox without any issues <br>
	 *         <b>False</b> if any error.<br>
	 *         Eg: <code> action.clickAndHoldTextboxByIndex(1)</code>
	 */
	boolean clickAndHoldTextboxByIndex(int index);

	/***
	 * To click and hold the Button based on it's Index
	 * 
	 * @param index - The index of the button to be clicked
	 * @return <b>True</b> if click and holded on Button without any issues <br>
	 *         <b>False</b> if any error.<br>
	 *         Eg: <code> action.clickAndHoldButtonByIndex(1)</code>
	 */
	boolean clickAndHoldButtonByIndex(int index);

	/***
	 * To click Button based on it's index
	 * 
	 * @param index - The index of the button to be clicked
	 * @return <b>True</b> if clicked on Button without any issues <br>
	 *         <b>False</b> if any error.<br>
	 *         Eg: <code> action.clickButtonByIndex(1)</code>
	 */
	boolean clickButtonByIndex(int index);

	/***
	 * To double click on an element
	 * 
	 * @param locator
	 * @return <b>True</b> if double clicked on element without any issues <br>
	 *         <b>False</b> if any error.<br>
	 *         Eg: <code> action.doubleClick('Login')</code>
	 */
	boolean doubleClick(String locator);

	/***
	 * To enter the value in a TextBox
	 * 
	 * @param value       - The value to be entered in the TextBox
	 * @param logicalName - The field name mentioned in the ObjectRepository file
	 */
	void enterText(String value, String logicalName);

	/***
	 * To enter data from the 'Saved value'
	 * 
	 * @param logicalName - The field name mentioned in the ObjectRepository file
	 */
	void enterSavedValue(String logicalName);

	/***
	 * To enter the 'Current date' into a TextBox (dd.mm.yyyy)
	 * 
	 * @param logicalName - The field name mentioned in the ObjectRepository file
	 */
	void enterCurrentDate(String logicalName);

	/***
	 * To clear text in an Edit field
	 * 
	 * @param logicalName - The field name mentioned in the ObjectRepository file
	 */
	void clearText(String logicalName);

	/***
	 * To select an option from the Drop down box using 'Visible test'
	 * 
	 * @param logicalName - The field name mentioned in the ObjectRepository file (
	 *                    Dropdown box)
	 * @param visibleText - The text to be selected from the DropDown
	 */
	void selectDropDownOptionByText(String logicalName, String visibleText);

	/***
	 * To select an option from the Drop down box using 'Value' attribute
	 * 
	 * @param logicalName - The field name mentioned in the ObjectRepository file (
	 *                    Dropdown box)
	 * @param value       - The 'value' (@value attribute's Value) of option to be
	 *                    selected from the DropDown
	 */
	void selectDropDownOptionByValue(String logicalName, String value);

	/***
	 * To select an option by it's index from the DropDown
	 * 
	 * @param logicalName - The field name mentioned in the ObjectRepository file (
	 *                    DropDown box)
	 * @param index       - The index of the DropDown values to be selected <br>
	 *                    Eg:
	 *                    <code> action.selectDropDownOptionByIndex('ProductTypes', 1)</code>
	 * 
	 */
	void selectDropDownOptionByIndex(String logicalName, String index);

	/***
	 * To select an option from the Drop down box
	 * 
	 * @param logicalName - The field name mentioned in the ObjectRepository file
	 * @param selectionType  - select drop down element by Type-> Text/Value/Index
	 * @param option      - The text/value/index Value, based on the selectType
	 */
	void selectDropDownOption(String logicalName, String option, String selectionType);

	/***
	 * Assert the element <b>is displayed</b> in the screen. <br>
	 * 
	 * @param logicalName - The field name mentioned in the ObjectRepository file
	 */
	void assertElementDisplayed(String logicalName);

	/***
	 * Assert the element <b>is not displayed</b> in the screen. <br>
	 * 
	 * @param logicalName
	 */
	void assertElementNotDisplayed(String logicalName);

	/***
	 * Assert the element <b>is displayed</b> in the screen. It waits for a certain
	 * amount of time (@waitTimeInSeconds) if the element is not immediately available. <br>
	 * 
	 * @param logicalName - The field name mentioned in the ObjectRepository file
	 */
	void assertElementDisplayed(String logicalName, int waitTimeInSeconds);

	/***
	 * Assert the 'Expected text' <b> is displayed </b> in the screen. <br>
	 * 
	 * @param expectedText - The 'Expected string value' to verify
	 */
	void assertTextDisplayed(String expectedText);

	/***
	 * Assert the element is having @expectedText value <br>
	 * 
	 * @param logicalName  - The field name mentioned in the ObjectRepository file
	 * @param expectedText - The 'Expected string value' to verify
	 */
	void assertElementValue(String logicalName, String expectedText);

	/***
	 * Assert any of the 'List of elements' are having the 'Expected text' <br>
	 * 
	 * @param logicalName  - The field name mentioned in the ObjectRepository file
	 * @param expectedText - The 'Expected string value' to verify
	 */
	void assertAnyElementHaveText(String logicalName, String expectedText);

	/***
	 * Assert any of the 'List of elements' as specified by @locator are having
	 * <b> either </b> @expectedTest1 <b> or </b> @expectedText2 <br>
	 * 
	 * @param locator       - selenium.By type value
	 * @param expectedText1 - The 'Expected string value 1' to verify
	 * @param expectedText2 - The 'Expected string value 2' to verify
	 */
	void assertAnyElementHaveOneOfTheText(By locator, String expectedText1, String expectedText2);

	/***
	 * Assert the Text matches with the given regular expression <br>
	 * 
	 * @param regx        - Regular expression value
	 * @param logicalName - The field name mentioned in the ObjectRepository file
	 */
	void assertElementValueWithRegularExpression(String regx, String logicalName);

	/***
	 * Asserts the Element value got increased compared to the previously stored
	 * value <br>
	 * 
	 * @param logicalName - The field name mentioned in the ObjectRepository file
	 */
	void assertElementValueIncreased(String logicalName);

	/***
	 * Asserts the Element value got decreased compared to the previously stored
	 * value. <br>
	 * 
	 * @param logicalName - The field name mentioned in the ObjectRepository file
	 */
	void assertElementValueDecreased(String logicalName);

	/***
	 * Asserts the element(@logicalName) contains the given text (@expectedText)
	 * 
	 * @param logicalName  - The field name mentioned in the ObjectRepository file
	 * @param expectedText - The 'Expected string value' to verify
	 * @return <b>True</b> if the element contains the @expectedText <br>
	 *         <b>False</b> if the element doesn't contain the @expectedText.<br>
	 *         Eg:
	 *         <code> action.assertElementContainsText('homepageHeader','Welcome')</code>
	 */
	boolean assertElementContainsText(String logicalName, String expectedText);

	/***
	 * Assert the element is disabled in the screen <br>
	 * 
	 * @param logicalName - The field name mentioned in the ObjectRepository file
	 */
	void assertElementDisabled(String logicalName);

	/***
	 * To verify the @expectedText in a new window
	 * 
	 * @param expectedText - The 'Expected string value' to verify
	 * @param logicalName  - The field name mentioned in the ObjectRepository file
	 */
	void assertTextInNewWindow(String expectedText, String logicalName);

	/***
	 * Assert the value of the CheckBox is as expected. <br>
	 * 
	 * @param locator        - The field name mentioned in the ObjectRepository file
	 * @param expectedStatus - Checked/ Unchecked
	 */
	void assertCheckbox(String locator, String expectedStatus);

	/***
	 * To Save the data to a variable
	 * 
	 * @param logicalName - The field name mentioned in the ObjectRepository file
	 */
	void saveElementText(String logicalName);

	/***
	 * To Select an option by value
	 * 
	 * @param option
	 * @param logicalName - The field name mentioned in the ObjectRepository file
	 */
	void selectOptionByValue(String option, String logicalName);

	/***
	 * To scroll to an element
	 * 
	 * @param scrolltype
	 * @param logicalName - The field name mentioned in the ObjectRepository file
	 */
	void scrollForElement(String scrolltype, String logicalName);

	/***
	 * To scroll the slider bar to an expected position
	 * 
	 * @param slider
	 * @param position
	 * @param slideBar
	 */
	void scrollSlider(String slider, String position, String slideBar);

	/***
	 * Switch to new window and click on an element
	 * 
	 * @param logicalName - The field name mentioned in the ObjectRepository file
	 */
	void switchToNewWindowAndClickElement(String logicalName);

	/***
	 * Switch to new window opened
	 */
	void switchToNewWindow();

	/***
	 * To wait until the element to be present in the screen. It waits for a certain
	 * amount of time (@waitTimeInSeconds) if the element is not immediately available.
	 * 
	 * @param logicalName - The field name mentioned in the ObjectRepository file
	 * @return <b>True</b> if the element is present after the wait <br>
	 *         <b>False</b> if the element is not present even sfter the wait.<br>
	 *         Eg: <code> action.waitForElementToDisplay('Welcome')</code>
	 */
	boolean waitForElementToDisplay(String logicalName);

	/***
	 * To dismiss/hide the keyboard from the screen
	 */
	void dismissKeyBoard();

	/***
	 * To wait until the element to be present with the customized wait time. It waits for a certain
	 * amount of time (@waitTimeInSeconds) if the element is not immediately available.
	 * 
	 * @param logicalName   - The field name mentioned in the ObjectRepository file
	 * @param secondsToWait - Wait time in milliseconds
	 * @return <b>True</b> if the element is present after the wait <br>
	 *         <b>False</b> if the element is not present even sfter the wait.<br>
	 *         Eg: <code> action.waitForElementToDisplay('Welcome',1000)</code>
	 */
	boolean waitForElementToDisplay(String logicalName, int secondsToWait);

	/***
	 * To select the CheckBox
	 * 
	 * @param locator - The field name mentioned in the ObjectRepository file
	 * @param status  - Select/ Deselect
	 * @return
	 */
	boolean selectCheckBox(String locator, boolean status);

	/***
	 * To Mousehover on an element
	 * 
	 * @param locator - The field name mentioned in the ObjectRepository file
	 * @return <b>True</b> if the mousehover on an element is successful<br>
	 *         <b>False</b> if any error <br>
	 *         Eg: <code> action.mouseHover('Login')</code>
	 */
	boolean mouseHover(String locator);

	/***
	 * To check whether the 'Text' is present in the screen or not
	 * 
	 * @param visibleText - The 'Expected Text' to verify
	 * @return <b>True</b> if the 'Text' <b>is present</b><br>
	 *         <b>False</b> if the 'Text' <b>is not present</b> <br>
	 *         Eg:
	 *         <code> action.isVisibleTextExistsCheckWithoutWait('Login')</code>
	 */
	boolean isVisibleTextExistsCheckWithoutWait(String visibleText);

	/***
	 * To take the screenshot
	 */
	void takeScreenshot();

	/***
	 * To take screenshot for assertion This method needs to be added in Assertion
	 * steps alone.
	 */
	void takeScreenshotForAssertion();

	/***
	 * Switch to the window with @windowTitle
	 * 
	 * @param windowTitle - The title of the expected Window to be navigated
	 * @return <b>True</b> if switched to the new window<br>
	 *         <b>False</b> if any error</b> <br>
	 *         Eg: <code> action.switchToWindow('Login_popup')</code>
	 */
	boolean switchToWindow(String windowTitle);

	/***
	 * To get the 'Element'
	 * 
	 * @param logicalName - The field name mentioned in the ObjectRepository file
	 * @return <b>True</b> if able to retrieve the WebElement<br>
	 *         <b>False</b> if any error</b> <br>
	 *         Eg: <code> action.getElement('Login')</code>
	 */
	WebElement getElement(String logicalName);

	/***
	 * To get the Element's text
	 * 
	 * @param logicalName - The field name mentioned in the ObjectRepository file
	 * @return <b> Element text </b> in String format
	 */
	String getElementText(String logicalName);

	/***
	 * Asserts the element is having the stored value <br>
	 * 
	 * @param logicalName        - The field name mentioned in the ObjectRepository
	 *                           file
	 * @param storedVariableName - The Expected text value to validate
	 */
	void assertElementValueWithStoredValue(String logicalName, String storedVariableName);

	/***
	 * To Check whether the element present in the screen or not
	 * 
	 * @param logicalName - The field name mentioned in the ObjectRepository file
	 * @return <b>True</b> if present<br>
	 *         <b>False</b> if not present</b> <br>
	 *         Eg: <code> action.isElementExists('Login')</code>
	 */
	boolean isElementExists(String logicalName);

	/***
	 * To Check whether the element present in the screen without wait time
	 * 
	 * @param logicalName - The field name mentioned in the ObjectRepository file
	 * @return <b>True</b> if present<br>
	 *         <b>False</b> if not present</b> <br>
	 *         Eg: <code> action.isElementExistsCheckWithoutWait('Login')</code>
	 */
	boolean isElementExistsCheckWithoutWait(String logicalName);

	/***
	 * To swipe/scroll down the screen
	 */
	void swipeDown();

	/**
	 * To click and accept the alert if present
	 * 
	 * @param logicalName - The field name mentioned in the ObjectRepository file
	 * @return <b>True</b> if the alert handled<br>
	 *         <b>False</b> if the alert not handled</b> <br>
	 *         Eg: <code> action.clickAndAcceptAlertIfExists('Login')</code>
	 */
	boolean clickAndAcceptAlertIfExists(String logicalName);

	/***
	 * To quit the webdriver instance
	 */
	void quiteDriver();

	/***
	 * To close the active browser window
	 */
	void closeWindow();

	/***
	 * To wait for the pages to complete the loading
	 * 
	 * @return <b>True</b> if the pages load completed<br>
	 *         <b>False</b> if any error</b> <br>
	 *         Eg: <code> action.waitForPageToLoad()</code>
	 */
	boolean waitForPageToLoad();	

	/***
	 * Assert the element has any value
	 * 
	 * @param logicalName - The field name mentioned in the ObjectRepository file
	 */
	void assertElementHasAnyValue(String logicalName);

	/***
	 * To check whether the CheckBox is checked or not
	 * 
	 * @return <b>True</b> if the CheckBox is checked<br>
	 *         <b>False</b> if not checked</b> <br>
	 *         Eg: <code> action.checkbox_Status_checked()</code>
	 */
	boolean isCheckboxChecked();

	/***
	 * To check whether the CheckBox is checked or not
	 * 
	 * @return <b>True</b> if the CheckBox is checked<br>
	 *         <b>False</b> if not checked</b> <br>
	 *         Eg: <code> action.checkbox_Status_checked()</code>
	 */
	boolean isCheckboxUnChecked();

	/***
	 * To open the Mobile application
	 */
	void openApp();

	/***
	 * To click the Tab key from an element
	 * 
	 * @param logicalName - The field name mentioned in the ObjectRepository file
	 * @return
	 */
	boolean clickTabKey(String logicalName);

	/***
	 * To click the Down key from an element
	 * 
	 * @param logicalName - The field name mentioned in the ObjectRepository file
	 * @return
	 */
	boolean clickDownKey(String logicalName);

	/***
	 * To get the saved data value
	 * 
	 * @param variablName - The variable used on actions.saveData('VariableName')
	 * @return The String variable. <br>
	 *         Eg: <code>action.getSavedData('VariableName')</code>
	 */
	String getSavedData(String variablName);

	/***
	 * To Save the data in a variable
	 * 
	 * @param variableName - Custom variable name
	 * @param value        - The Variable value to be stored
	 */
	void saveData(String variableName, String value);
}
