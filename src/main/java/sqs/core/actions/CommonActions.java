package sqs.core.actions;

import com.jayway.awaitility.*;
import cucumber.api.PendingException;
import cucumber.runtime.CucumberException;
import io.appium.java_client.AppiumDriver;
import io.restassured.http.Headers;
import io.restassured.response.*;
import org.apache.log4j.Logger;
import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import sqs.core.actions.api.APIRequestActions;
import sqs.core.actions.interfaces.FrameworkActionsInterface;
import sqs.core.actions.web.SeleniumActions;
import sqs.core.constants.Browsers.Browser;
import sqs.core.constants.Constants;
import sqs.core.constants.PropertyConstants;
import sqs.framework.FrameworkData;
import sqs.core.utils.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static sqs.core.constants.Constants.*;
import static sqs.core.utils.TestDataHandler.getData;
import static sqs.core.utils.Utilities.getCallerMethodName;

public class CommonActions extends SeleniumActions implements FrameworkActionsInterface {

	private static Logger logger = Logger.getLogger(CommonActions.class);

	public boolean clickByJavaScript(String logicalName) {
		return clickByJavaScript(ObjectRepository.get(logicalName));
	}

	public boolean click(String logicalName) {
	    boolean isClicked = click(ObjectRepository.get(logicalName), 0);
	    if(isClicked)
	        logger.info("Element '"+logicalName + "' is clicked");
		return  isClicked;
	}

	public boolean clickOnVisibleTextIfExists(String visibleText) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		if (isVisibleTextExists(visibleText)) {
			LoggerUtils.actionCompletedLog(getCallerMethodName());
			return clickOnVisibleText(visibleText);
		} else {
			return false;
		}

	}

	public boolean clickOnVisibleText(String visibleText) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		boolean isClicked = false;
		try {
			Set<String> windowsBeforeClick = getWindowHandles();
			visibleText= getData(visibleText);
			ObjectRepository.add("VisibleText_" + visibleText, "xpath", getXpathForVisibleTest(visibleText));
			isClicked = action.click("VisibleText_" + visibleText);
			logger.info(CLICKED_ON_ELEMENT + visibleText);
			setActionPerformed(CLICK);
			switchWindowIfExists(windowsBeforeClick);
		} catch (Exception exception) {
			logger.error(Constants.EXCEPTION_ON+ getCallerMethodName()+" :",exception);
			throw exception;
		}
		LoggerUtils.actionCompletedLog(getCallerMethodName());
		return isClicked;
	}

	public boolean clickAndAcceptAlertIfExists(String logicalName) {
		LoggerUtils.actionStartedLog(getCallerMethodName());

		boolean isElementClicked;
		try {
			Set<String> windowsBeforeClick = getWindowHandles();
			isElementClicked = click(logicalName);
			acceptAlertIfPresent();
			switchWindowIfExists(windowsBeforeClick);
		} catch (Exception exception) {
			throw new CucumberException(exception.getMessage(), exception);
		}
		LoggerUtils.actionCompletedLog(getCallerMethodName());
		return isElementClicked;
	}

	/**
	 * @Method: doubleClick
	 * @Description: This is wrapper method used for doubleClick on element
	 * @param logicalName - By identification of element
	 * @return - true if double click successful
	 * @author Automation Tester (SQS) Creation Date: 20 July 2015 Modified Date:
	 * @throws Exception
	 */
	public boolean doubleClick(String logicalName) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		try {
			waitForElementToDisplay(logicalName, 10);
			WebElement webElement = getElement(logicalName);
			Actions actionBuilder = new Actions(driver);
			actionBuilder.doubleClick(webElement).build().perform();
			logger.info("Double clicked on element " + logicalName);
			setActionPerformed("DoubleClick");
			return true;
		} catch (Exception exception) {
			logger.error(Constants.EXCEPTION_ON+ getCallerMethodName()+" :",exception);
			return false;
		}
	}

	public void enterText(String value, String logicalName) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		try {
			WebElement element = getElement(ObjectRepository.get(logicalName));
			if (element.isEnabled()) {
				value= getData(value);
				element.sendKeys(value);
				logger.info("Entered text '" + value + "' in to the textfield " + logicalName);
				Awaitility.await().pollDelay(Duration.ONE_SECOND).until(() -> true);
     			takeScreenshot();
     			setActionPerformed("EnterText");
				takeScreenshot();
			} else {
				logger.info(logicalName + " is not visible to enter text.");
				Assert.fail(logicalName + " is not visible to enter text.");
			}
		} catch (Exception e) {
			throw new CucumberException(e.getMessage(), e);
		}
		LoggerUtils.actionCompletedLog(getCallerMethodName());
	}

	public boolean clearAndEnterText(String logicalName, String fieldValue)  {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		boolean isValueEntered = false;
		waitForElementToDisplay(logicalName, 10);
		WebElement webElement = getElement(logicalName);
		try {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript(JAVASCRIPT_ARGUMENTS_0_CLICK, webElement);
			webElement.sendKeys(Keys.chord(Keys.CONTROL, "a"));
			webElement.sendKeys(Keys.DELETE);
			webElement.clear();
			logger.info("Details cleared from Textbox");
			enterText(fieldValue, logicalName);
			takeScreenshot();
			isValueEntered = true;
		} catch (Exception exception) {
			logger.error(Constants.EXCEPTION_ON+ getCallerMethodName()+" :",exception);
		}

		setActionPerformed("ClearAndEnterValueInField");
		LoggerUtils.actionCompletedLog(getCallerMethodName());
		return isValueEntered;

	}

	/**
	 * @Method: selectCheckBox
	 * @Description: This is wrapper method select/deselect checkbox
	 * @param logicalName - By identification of element
	 * @param status      - select/deselect
	 * @author Automation Tester (SQS) Creation Date: 20 July 2015 Modified Date:
	 * @throws Exception
	 */
	public boolean selectCheckBox(String logicalName, boolean status) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		try {
			waitForElementToDisplay(logicalName, 10);
			WebElement webElement = getElement(logicalName);
			if (webElement.getAttribute("type").equals("checkbox")) {
				if ((webElement.isSelected() && !status) || (!webElement.isSelected() && status))
					webElement.click();
				logger.info(CLICKED_ON_ELEMENT + logicalName);
				setActionPerformed("SelectCheckbox");
				LoggerUtils.actionCompletedLog(getCallerMethodName());
				return true;
			} else return false;
		} catch (Exception exception) {
			logger.error(Constants.EXCEPTION_ON+ getCallerMethodName()+" :",exception);
			return false;
		}

	}

	/**
	 * @Method: isCheckBoxSelected
	 * @Description: This is wrapper checkbox is selected or not
	 * @param logicalName - By identification of element
	 * @author Automation Tester (SQS) Creation Date: 20 July 2015 Modified Date:
	 * @throws Exception
	 */
	public boolean isCheckBoxSelected(String logicalName, boolean status)  {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		waitForElementToDisplay(logicalName, 10);
		WebElement webElement = getElement(logicalName);

		try {
			if (webElement.getAttribute("type").equals("checkbox"))
				status = webElement.isSelected();
			setActionPerformed("AssertCheckboxIsSelected");
			LoggerUtils.actionCompletedLog(getCallerMethodName());
			return status;
		} catch (Exception exception) {
			logger.error(Constants.EXCEPTION_ON+ getCallerMethodName()+" :",exception);
			status=false;
			return status;
		}
	}

	/**
	 * @Method: selectRadio
	 * @Description: This is wrapper method select/deselect radio button
	 * @param logicalName - By identification of element
	 * @param status      - select/deselect
	 * @author Automation Tester (SQS) Creation Date: 20 July 2015 Modified Date:
	 * @throws Exception
	 */
	public boolean selectRadioButton(String logicalName, boolean status)  {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		waitForElementToDisplay(logicalName, 10);
		WebElement webElement = getElement(logicalName);
		try {
			if (webElement.getAttribute("type").equals("radio")) {
				if ((webElement.isSelected() && !status) || (!webElement.isSelected() && status))
					webElement.click();
				logger.info("Selected radio field " + logicalName);
				setActionPerformed("selectRadioButton");

				LoggerUtils.actionCompletedLog(getCallerMethodName());
				return true;
			} else {
				logger.error("selectRadioButton action process failed.");
				return false;
			}
		} catch (Exception exception) {
			logger.error(Constants.EXCEPTION_ON+ getCallerMethodName()+" :",exception);
			return false;
		}
	}

	/**
	 * @Method: mouseHover
	 * @Description: This is wrapper method used for Mouse Hovering to the element
	 * @param logicalName - By identification of element
	 * @author Automation Tester (SQS) Creation Date: 20 July 2015 Modified Date:
	 * @throws Exception
	 */
	public boolean mouseHover(String logicalName) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		try {
			WebElement webElement = getElement(logicalName);
			Actions actionBuilder = new Actions(driver);
			actionBuilder.moveToElement(webElement).build().perform();
			setActionPerformed("MouseOverOnElement");
			LoggerUtils.actionCompletedLog(getCallerMethodName());
			return true;
		} catch (Exception exception) {
			logger.error(Constants.EXCEPTION_ON+ getCallerMethodName()+" :",exception);
			return false;
		}
	}

	/**
	 * @Method: clickByAction
	 * @Description: This is wrapper method used for Mouse Hovering to the element
	 * @param logicalName - By identification of element
	 * @author Automation Tester (SQS) Creation Date: 20 July 2015 Modified Date:
	 * @throws Exception
	 */
	public boolean clickByAction(String logicalName) {
		return clickByAction(ObjectRepository.get(logicalName));

	}

	public boolean clickByAction(By locator) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		WebElement webElement = getElement(locator);
		try {
			Actions actionBuilder = new Actions(driver);
			actionBuilder.moveToElement(webElement).click().build().perform();
			logger.info(CLICKED_ON_ELEMENT + locator.toString());
			setActionPerformed(CLICK);
			LoggerUtils.actionCompletedLog(getCallerMethodName());
			return true;
		} catch (Exception exception) {
			logger.error(Constants.EXCEPTION_ON+ getCallerMethodName()+" :",exception);
			return false;
		}
	}

	public boolean scroll(String scrollType, String logicalName) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		try {
			JavascriptExecutor executor = (JavascriptExecutor) driver;

			switch (scrollType) {
			case "top":
				executor.executeScript("window.scrollTo(0,0)");
				break;
			case "bottom":
				executor.executeScript("window.scrollTo(0, document.body.scrollHeight)");
				break;
			case "element":
				waitForElementToDisplay(logicalName);
				executor.executeScript("arguments[0].scrollIntoView();", getElement(logicalName));
				break;
			case "bycoordinates":
				String[] coordinates = logicalName.split(",");
				String x = coordinates[0];
				String y = coordinates[1];
				executor.executeScript("window.scrollBy(" + x + "," + y + ")");
				break;
			default:
				executor.executeScript("window.scrollTo(0,0)");
				logger.info("Scroll action defaulted to bottom.");
				break;
			}
			setActionPerformed("ScrollAction");
			LoggerUtils.actionCompletedLog(getCallerMethodName());
			return true;
		} catch (Exception exception) {
			logger.error(Constants.EXCEPTION_ON+ getCallerMethodName()+" :",exception);
			return false;
		}
	}

	/**
	 * @Method: switchToWindowUsingTitle
	 * @Description: This is wrapper method used switch to window using the given
	 *               title
	 * @param logicalName - Window title
	 * @author Automation Tester (SQS) Creation Date: 20 July 2015 Modified Date:
	 */

	public void selectDropDownOptionByText(String logicalName, String visibleText) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		try {
			WebElement webElement = getElement(logicalName);
			Select sltDropDown = new Select(webElement);
			sltDropDown.selectByVisibleText(visibleText);
			setActionPerformed("SelectDropdownVisibleText");
			LoggerUtils.actionCompletedLog(getCallerMethodName());
		} catch (Exception exception) {
			logger.error(Constants.EXCEPTION_ON+ getCallerMethodName()+" :",exception);
			throw new CucumberException(exception.getMessage());
		}

	}

	public void selectDropDownOptionByValue(String logicalName, String value) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		try {
			WebElement webElement = getElement(logicalName);
			Select sltDropDown = new Select(webElement);
			sltDropDown.selectByValue(value);
			setActionPerformed("SelectDropdownValue");
			LoggerUtils.actionCompletedLog(getCallerMethodName());
		} catch (Exception exception) {
			logger.error(Constants.EXCEPTION_ON+ getCallerMethodName()+" :",exception);
			throw new CucumberException(exception.getMessage());
		}

	}

	public void selectDropDownOptionByIndex(String logicalName, String index) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		try {
			WebElement webElement = getElement(logicalName);
			Select sltDropDown = new Select(webElement);
			sltDropDown.selectByIndex(Utilities.getInteger(index));
			setActionPerformed("SelectDropdownByIndex");
			LoggerUtils.actionCompletedLog(getCallerMethodName());
		} catch (Exception exception) {
			logger.error(Constants.EXCEPTION_ON+ getCallerMethodName()+" :",exception);
			throw new CucumberException(exception.getMessage());
		}

	}

	/**
	 * @Method: selectCheckBox
	 * @Description: This is wrapper method select drop down element
	 * @param logicalName - By identification of element
	 * @param option      - drop down element (user may specify text/value/index)
	 * @param selectType  - select dorp down element by Text/Value/Index
	 * @author Automation Tester (SQS) Creation Date: 20 July 2015 Modified Date:
	 */
	public void selectDropDownOption(String logicalName, String option, String selectType) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		try {
			WebElement webElement = getElement(logicalName);
			Select sltDropDown = new Select(webElement);
			selectType = Utilities.trimString(selectType);
			if (!selectType.equals("")) {
				selectDropDownValue(sltDropDown,option,selectType);
			} else {
				selectDropDownOption(sltDropDown,option,logicalName);
			}
			LoggerUtils.actionCompletedLog(getCallerMethodName());
		} catch (Exception exception) {
			logger.error(Constants.EXCEPTION_ON+ getCallerMethodName()+" :",exception);
			throw new CucumberException(exception.getMessage());
		}
	}
	void selectDropDownValue(Select sltDropDown, String option,String selectType){
		if (selectType.equalsIgnoreCase("Value")) {
			sltDropDown.selectByValue(option);
		} else if (selectType.equalsIgnoreCase("Index")) {
			sltDropDown.selectByIndex(Integer.parseInt(option));
		} else {
			sltDropDown.selectByVisibleText(option);
		}
		setActionPerformed(SELECT_DROPDOWN_OPTION);

	}
	void selectDropDownOption(Select sltDropDown, String option,String logicalName){
		List<WebElement> options = sltDropDown.getOptions();
		boolean blnOptionAvailable = false;
		int iIndex = 0;
		for (WebElement weOptions : options) {
			if (weOptions.getText().trim().equals(option)) {
				sltDropDown.selectByIndex(iIndex);
				blnOptionAvailable = true;
       			setActionPerformed(SELECT_DROPDOWN_OPTION);

			} else iIndex++;
			if (blnOptionAvailable)
				break;
		}
		if (!blnOptionAvailable) {
			throw new CucumberException("Given option '" + option + "' is not available in '" + logicalName);
		}

	}

	/**
	 * @Method: getSelectedValueFromDropDown
	 * @Description: This is wrapper method select drop down element
	 * @param logicalName - By identification of element
	 * @author Automation Tester (SQS) Creation Date: 20 July 2015 Modified Date:
	 */
	public String getSelectedValueFromDropDown(String logicalName) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		try {
			waitForElementToDisplay(logicalName, 10);
			Select selectDorpDown = new Select(getElement(logicalName));
			String selectedDorpDownValue = selectDorpDown.getFirstSelectedOption().getText();
			setActionPerformed("GetSelectedValueFromDropdown");
			LoggerUtils.actionCompletedLog(getCallerMethodName());
			return selectedDorpDownValue;
		} catch (Exception exception) {
			logger.error(Constants.EXCEPTION_ON+ getCallerMethodName()+" :",exception);
			return null;
		}

	}

	/**
	 * @Method: selectRadioButtonForSpecificColumn
	 * @Description: This is wrapper method to select radio button from table with
	 *               respect to particular column content
	 * @param logicalName   - By identification of element (table with all rows)
	 * @param columnContent - String column content
	 * @param columnNumberForRadio - integer column number for radio button
	 * @author Automation Tester (SQS) Creation Date: 20 July 2015 Modified Date:
	 */
	public boolean selectRadioButtonForSpecificColumn(String logicalName, String columnContent,
			int columnNumberForRadio) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		try {
			waitForElementToDisplay(logicalName, 10);
			List<WebElement> weResultTable = getElements(logicalName);
			for (WebElement weRow : weResultTable) {
				List<WebElement> weColumns = weRow.findElements(By.xpath(TABLE_TD));
				for (WebElement weColumn : weColumns) {
					if (weColumn.getText().trim().equals(columnContent)) {
						WebElement webElement = weRow
								.findElement(By.xpath(".//td['" + columnNumberForRadio + "']/input[@type='radio']"));
						JavascriptExecutor executor = (JavascriptExecutor) driver;
						executor.executeScript(JAVASCRIPT_ARGUMENTS_0_CLICK, webElement);
						webElement.click();
						Utilities.waitFor(1000);
						webElement.click();
						webElement.click();
						setActionPerformed("SelectRadioFromTable");

					}
				}
			}
			LoggerUtils.actionCompletedLog(getCallerMethodName());
			return true;
		} catch (Exception exception) {
			logger.error(Constants.EXCEPTION_ON+ getCallerMethodName()+" :",exception);
			return false;
		}
	}

	/**
	 * @Method: selectCheckBoxForSpecificColumn
	 * @Description: This is wrapper method to select chechbox from table with
	 *               respect to particular column content
	 * @param logicalName   - By identification of element (table with all rows)
	 * @param columnContent - String column content
	 * @columnNumberForRadio - integer column number for radio button
	 * @author Automation Tester (SQS) Creation Date: 20 July 2015 Modified Date:
	 */
	public boolean selectCheckBoxForSpecificColumn(String logicalName, String columnContent, int columnNumberForRadio) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		try {
			waitForElementToDisplay(logicalName, 10);
			List<WebElement> weResultTable =  getElements(logicalName);
			for (WebElement weRow : weResultTable) {
				List<WebElement> weColumns = weRow.findElements(By.xpath(TABLE_TD));
				for (WebElement weColumn : weColumns) {
					if (weColumn.getText().trim().equals(columnContent))
						weRow.findElement(
								By.xpath(".//td['" + columnNumberForRadio + "']/span/input[@type='checkbox']")).click();
					setActionPerformed("SelectCheckBoxInTable");

				}
			}
			LoggerUtils.actionCompletedLog(getCallerMethodName());
			return true;
		} catch (Exception exception) {
			logger.error(Constants.EXCEPTION_ON+ getCallerMethodName()+" :",exception);
			return false;
		}
	}

	/**
	 * @Method: verifyTableContent
	 * @Description:
	 * @param logicalName   - By identification of element (table with all rows)
	 * @param columnHeader  - String column header
	 * @param columnContent - String column content
	 * @author Automation Tester (SQS) Creation Date: 20 July 2015 Modified Date:
	 */
	public boolean verifyTableContent(String logicalName, String columnHeader, String columnContent) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		HashMap<String, String> dataColumnHeader = new HashMap<>();
		int intColumnNumber = 1;
		boolean blnverify = false;
		try {
			waitForElementToDisplay(logicalName, 10);
			WebElement weResultTable = getElement(logicalName);

			List<WebElement> weColumnsHeaders = weResultTable.findElements(By.xpath(TABLE_HEADER));
			for (WebElement weColumnHeader : weColumnsHeaders) {
				String strHeader = weColumnHeader.getText().trim();
				if (!strHeader.equals(""))
					dataColumnHeader.put(strHeader, String.valueOf(intColumnNumber));
				intColumnNumber++;
			}

			List<WebElement> weRows = weResultTable.findElements(By.xpath(TABLE_ROWS));
			for (WebElement weRow : weRows) {
				WebElement weExceptedClm = weRow
						.findElement(By.xpath(TABLE_TD+"[" + dataColumnHeader.get(columnHeader) + "]"));
				if (weExceptedClm.getText().trim().contains(columnContent))
					blnverify = true;
			}
			setActionPerformed("AssertTableData");

			LoggerUtils.actionCompletedLog(getCallerMethodName());
			return blnverify;
		} catch (Exception exception) {
			logger.error(Constants.EXCEPTION_ON+ getCallerMethodName()+" :",exception);
			return false;
		}
	}

	/**
	 * @Method: verifyTableContentAndCheckSelected
	 * @Description:
	 * @param logicalName   - By identification of element (table with all rows)
	 * @param columnHeader  - String column header
	 * @param columnContent - String column content
	 * @author Automation Tester (SQS) Creation Date: 20 July 2015 Modified Date:
	 */
	public boolean verifyTableContentAndCheckSelected(String logicalName, String columnHeader, String columnContent,
			int checkboxColumnNumber) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		HashMap<String, String> dataColumnHeader = new HashMap<>();
		int intColumnNumber = 1;
		boolean blnverify = false;
		try {
			waitForElementToDisplay(logicalName, 10);
			WebElement weResultTable = getElement(logicalName);

			List<WebElement> weColumnsHeaders = weResultTable.findElements(By.xpath(TABLE_HEADER));
			for (WebElement weColumnHeader : weColumnsHeaders) {
				String strHeader = weColumnHeader.getText().trim();
				if (!strHeader.equals(""))
					dataColumnHeader.put(strHeader, String.valueOf(intColumnNumber));
				intColumnNumber++;
			}

			List<WebElement> weRows = weResultTable.findElements(By.xpath(TABLE_ROWS));
			for (WebElement weRow : weRows) {
				WebElement weExceptedClm = weRow
						.findElement(By.xpath(TABLE_TD+"[" + dataColumnHeader.get(columnHeader) + "]"));
				if (weExceptedClm.getText().trim().contains(columnContent)) {
					WebElement weCheckBox = weRow
							.findElement(By.xpath(TABLE_TD+"["+ checkboxColumnNumber + "]/span/input[@type='checkbox']"));
					boolean blnIsSelected = weCheckBox.isSelected();
					if (blnIsSelected) {
						blnverify = true;
					}
				}
			}
			setActionPerformed("AssertTableDataIsSelected");

			LoggerUtils.actionCompletedLog(getCallerMethodName());
			return blnverify;
		} catch (Exception exception) {
			logger.error(Constants.EXCEPTION_ON+ getCallerMethodName()+" :",exception);
			return false;
		}
	}

	/**
	 * @Method: view table details
	 * @Description:
	 * @param logicalName - By identification of table element
	 * @author Automation Tester (SQS) Creation Date: 20 July 2015 Modified Date:
	 */
	public boolean viewTableContent(String logicalName) {
		logger.debug("viewTableContent action process started...");
		try {
			WebElement weResultTable = getElement(logicalName);

			int intHeaderNumber = 1;
			List<WebElement> weColumnsHeaders = weResultTable.findElements(By.xpath(TABLE_HEADER));
			logger.debug("************************* Table Header details *************");
			for (WebElement weColumnHeader : weColumnsHeaders) {
				logger.debug("Table Header * " + intHeaderNumber + " *--->" + weColumnHeader.getText());
				intHeaderNumber++;
			}

			List<WebElement> weRows = weResultTable.findElements(By.xpath(TABLE_ROWS));
			int intRowNum = 1;
			logger.debug("************************* Table content details *************");
			for (WebElement weRow : weRows) {
				logger.debug("**********Row Number  " + intRowNum + " *************");
				int intClmNum = 1;
				List<WebElement> weClmns = weRow.findElements(By.xpath(TABLE_TD));
				for (WebElement weClmn : weClmns) {
					logger.debug("Column Number---->" + intClmNum);
					logger.debug("Column Text-------->" + weClmn.getText());
					intClmNum++;
				}
				intRowNum++;
			}
			setActionPerformed("DisplayTableContent");
			LoggerUtils.actionCompletedLog(getCallerMethodName());
			return true;
		} catch (Exception exception) {
			logger.error(Constants.EXCEPTION_ON+ getCallerMethodName()+" :",exception);
			return false;
		}
	}

	public void dismissKeyBoard() {
		LoggerUtils.methodNotImplemented(getCallerMethodName());
		throw new PendingException("dismissKeyBoard "+METHOD_NOT_IMPLEMENTED);

	}

	public void switchToNewWindowAssertElement(String logicalName) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		try {
			switchToNewWindow();
			boolean isElementFound = waitForElementToDisplay(logicalName);
			if (!isElementFound) {
				logger.error(ELEMENT+ "'" + logicalName + "' is not found in the Page.");
				Assert.fail(ELEMENT+ "'"+ logicalName + "' is not found in the Page.");
			}
			setActionPerformed("SwitchToNewWindowAssertElement");

			LoggerUtils.actionCompletedLog(getCallerMethodName());
		} catch (Exception exception) {
			logger.error(Constants.EXCEPTION_ON+ getCallerMethodName()+" :",exception);
		}

	}

	public boolean closeTheWindow(String windowTitle) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		try {
			Set<String> openWindows = getWindowHandles();
			if (!openWindows.isEmpty()) {
				for (String windows : openWindows) {
					String window = driver.switchTo().window(windows).getTitle();
					if (windowTitle.equals(window)) {
						driver.close();
						logger.info("Driver closed..");
						setActionPerformed("CloseSpecificWindow");

					} else {
						switchToWindow(getMainWindowHandle());
					}
				}
			}
			LoggerUtils.actionCompletedLog(getCallerMethodName());
			return true;
		} catch (Exception exception) {
			logger.error(Constants.EXCEPTION_ON+ getCallerMethodName()+" :",exception);
			return false;
		}
	}

	public boolean waitForElementToDisplay(String logicalName) {
		return waitForElementToDisplay(ObjectRepository.get(logicalName), FrameworkData.pageLoadWaitTime);
	}

	public boolean waitForElementToDisplay(By locator) {
		return waitForElementToDisplay(locator, FrameworkData.pageLoadWaitTime);
	}

	public boolean waitForElementToDisplay(String logicalName, int secondsToWait) {
		return waitForElementToDisplay(ObjectRepository.get(logicalName), secondsToWait);
	}

	public boolean waitForElementToDisplay(By locator, int secondsToWait) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		double waitedSeconds = 0;
		boolean isElementFound = false;
		WebElement element;
		setImplicitWait(0);

		while (waitedSeconds <= secondsToWait) {
			try {
				Utilities.waitFor(500, "Object:" + locator);
				waitedSeconds = waitedSeconds + 0.5;
				element = getElement(locator);
				if (element.isDisplayed()) {
					isElementFound = true;
					logger.debug("Object :" + locator.toString() + " is Displayed");
					break;
				}
			} catch (Exception e) {
//				logger.error("Not able to Found :" + locator);
			}
		}
		setImplicitWait(implicitWaitTime);
		setActionPerformed("WaitedForElementPresence");

		LoggerUtils.actionCompletedLog(getCallerMethodName());
		return isElementFound;
	}

	public boolean waitForElementToBeClickable(String logicalName, int secondsToWait) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		double waitedSeconds = 0;
		boolean isElementClickable = false;
		WebElement element;
		setImplicitWait(0);
		while (!isElementClickable && waitedSeconds <= secondsToWait) {
			try {
				Utilities.waitFor(500);
				waitedSeconds = waitedSeconds + 0.5;
				element = getElement(logicalName);
				if (ExpectedConditions.elementToBeClickable(element)!=(null)) {
					isElementClickable = true;
					logger.info(ELEMENT + "'" + logicalName + "' id clickable..");
				}
			}
			catch (Exception e) {
				logger.error("Exection wile working with" + logicalName + "with error "+e.getMessage());
			}
		}
		setImplicitWait(implicitWaitTime);
		LoggerUtils.actionCompletedLog(getCallerMethodName());
		return isElementClickable;

	}


	public boolean checkElementExistence(String logicalName, int timeInSeconds) {
		return waitForElementToDisplay(logicalName, timeInSeconds);
	}

	public WebElement getElement(String logicalName) {
		return getElement(ObjectRepository.get(logicalName));
	}

	public boolean isVisibleTextExistsCheckWithoutWait(String visibleText) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		By locator = getlocatorForVisibleText(visibleText);
		LoggerUtils.actionCompletedLog(getCallerMethodName());
		return isElementExistsCheckWithoutWait(locator);
	}

	public boolean isVisibleTextExists(String visibleText) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		By locator = getlocatorForVisibleText(visibleText);
		LoggerUtils.actionCompletedLog(getCallerMethodName());
		return isElementExists(locator, true, true);
	}

	public boolean isElementExistsCheckWithoutWait(String logicalName) {
		return isElementExistsCheckWithoutWait(ObjectRepository.get(logicalName));
	}

	public boolean isElementExists(String fieldName, boolean throwExceptionOnFail) {
		return isElementExists(ObjectRepository.get(fieldName), throwExceptionOnFail, true);
	}

	public boolean isElementExists(String fieldName) {
		try {
			return isElementExists(ObjectRepository.get(fieldName), true, true);
		} catch (Exception e) {
			throw new CucumberException(e);
		}
	}
	
	//@Test
	//@Step("Application Open Method called")
	//@Story("Application Story")
	public void openApplication() {
		String applicationURL = config.getProperty(PropertyConstants.APPLICATION_URL);
		openURL(applicationURL);
	}

	protected void openURL(String applicationURL) {
		try {
			logger.debug("url :" + applicationURL);
			Awaitility.await().pollDelay(new Duration(3, TimeUnit.SECONDS)).until(() -> true);
			logger.debug("Driver session "+ driver);
			driver.get(applicationURL);
			Awaitility.await().pollDelay(Duration.ONE_SECOND).until(() -> true);
			driver.navigate().refresh();
			Utilities.waitFor(1000, "After Launchin URL");
			logger.info("URL: '"+applicationURL+"' launched..");
		} catch (Exception e) {
			throw new CucumberException(e);
		}
	}

	public boolean clickAndHoldButtonByIndex(int index) {
		LoggerUtils.methodNotImplemented(getCallerMethodName());
		throw new PendingException("clickAndHoldButtonByIndex "+METHOD_NOT_IMPLEMENTED);
	}

	public boolean clickButtonByIndex(int index) {
		LoggerUtils.methodNotImplemented(getCallerMethodName());
		throw new PendingException("clickButtonByIndex "+METHOD_NOT_IMPLEMENTED);

	}

	public void clearText(String logicalName) {
		clearText(logicalName, 0);
	}

	public void clearText(String logicalName, int retrycount) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		try {
			getElement(ObjectRepository.get(logicalName)).clear();
			if (retrycount < 2) {
				retrycount++;
				clearText(logicalName, retrycount);
			}
			setActionPerformed("ClearValueInField");

			LoggerUtils.actionCompletedLog(getCallerMethodName());
		} catch (InvalidElementStateException e) {
			if (retrycount < 3) {
				retrycount++;
				clearText(logicalName, retrycount);
			} else {

				throw new CucumberException(e.getMessage(), e);
			}
		} catch (Exception e) {

			throw new CucumberException(e.getMessage(), e);
		}
		LoggerUtils.actionCompletedLog(getCallerMethodName());

	}

	public void enterSavedValue(String logicalName) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		String value = getSavedData(logicalName);
		enterText(value, logicalName);
		LoggerUtils.actionCompletedLog(getCallerMethodName());
	}

	public boolean clickEnter(String logicalName) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		boolean isEnterClicked;
		try {
			Set<String> windowsBeforeClick = getWindowHandles();
			WebElement element = getElement(logicalName);
			if (isWindows) {
				element.sendKeys(Keys.ENTER);
			} else {
				element.click();
				logger.info("Clicked element "+logicalName);
			}
			isEnterClicked = true;
			setActionPerformed(CLICK);

			switchWindowIfExists(windowsBeforeClick);
			waitForPageToLoad();
		} catch (Exception e) {
			throw new CucumberException(e.getMessage(), e);
		}
		LoggerUtils.actionCompletedLog(getCallerMethodName());
		return isEnterClicked;
	}

	public void assertElementDisplayed(String logicalName) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		boolean isElementDisplayed = false  ;
		try {
			WebElement element ;
			element = getElement(logicalName);
			if (element.isDisplayed()) isElementDisplayed = true;
			logger.info("isElement '"+logicalName+"' Displayed= "+isElementDisplayed);
		} catch (Exception e) {
			throw new CucumberException(e);
		}
		Assert.assertTrue(logicalName + " is not displayed in pages", isElementDisplayed);
		action.takeScreenshotForAssertion();
		setActionPerformed("AssertFieldDisplayed");

		LoggerUtils.actionCompletedLog(getCallerMethodName());
	}

	public void assertElementNotDisplayed(String logicalName) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		boolean isElementDisplayed = false;
		WebElement element;
		try {

			element = getElement(logicalName);
		} catch (Exception e) {
			throw new CucumberException(e);
		}
		if (element != null) {
			isElementDisplayed = element.isDisplayed();
			logger.info("isElement '"+logicalName+"' Displayed= "+isElementDisplayed);
		}
		Assert.assertFalse(logicalName + " should not displayed in pages but it displayed", isElementDisplayed);
		action.takeScreenshotForAssertion();
		setActionPerformed("AssertFieldNotDisplayed");

		LoggerUtils.actionCompletedLog(getCallerMethodName());
		takeScreenshot();
	}

	public void assertTextDisplayed(String expectedText) {
		LoggerUtils.actionStartedLog(getCallerMethodName());

		try {
			waitForPageToLoad();
			waitForVisibleText(expectedText);
			String pageSource = driver.getPageSource();
			if (!pageSource.contains(expectedText)) {
				Assert.fail("Expected Text '" + expectedText + "' is not displayed in the pages.");
			}else{
				logger.info("Expected Text '"+expectedText + "' is displayed in the page");
			}
			action.takeScreenshotForAssertion();
		} catch (Exception e) {
			throw new CucumberException(e);
		}
		setActionPerformed("AssertTextInPage");
		LoggerUtils.actionCompletedLog(getCallerMethodName());

	}

	public void assertElementValue(String logicalName, String expectedValue) {
		LoggerUtils.actionStartedLog(getCallerMethodName());

		try {
			logger.debug("assert_ElementText action process started...");
			waitForElementToDisplay(logicalName);
			String actualValue = getElementText(getElement(logicalName));
			Assert.assertEquals(actualValue, expectedValue);
			action.takeScreenshotForAssertion();
			setActionPerformed("AssertFieldText");

		} catch (Exception e) {
			throw new CucumberException(e);
		}
		LoggerUtils.actionCompletedLog(getCallerMethodName());
	}

	public void assertElementValueWithRegularExpression(String regx, String logicalName) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		String actualValue = getElementText(getElement(logicalName));

		if (!actualValue.matches(regx)) {
			Assert.fail("Expected RegularExpression '" + regx + "'  but Actual text found '" + actualValue + "'");
		}
		action.takeScreenshotForAssertion();
		setActionPerformed("Assert_ElementText_WithRegularExpression");

		LoggerUtils.actionCompletedLog(getCallerMethodName());
	}

	public void saveElementText(String logicalName) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		try {
			WebElement element = getElement(logicalName);
			String value = getElementText(element);
			logger.debug("Value '" + value + "' Saved in the name " + logicalName);
			saveData(logicalName, value);
		} catch (Exception e) {
			throw new CucumberException(e);
		}
		LoggerUtils.actionCompletedLog(getCallerMethodName());
	}

	public void assertElementOnPage(int seconds, String logicalName) {
		throw new PendingException(METHOD_NOT_IMPLEMENTED);

	}

	public void selectOptionByValue(String option, String logicalName) {
		throw new PendingException(METHOD_NOT_IMPLEMENTED);

	}

	public void scrollForElement(String scrolltype, String logicalName) {
		throw new PendingException(METHOD_NOT_IMPLEMENTED);

	}

	public boolean assertElementContainsText(String logicalName, String expectedText) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		boolean isElementHaveText = false;
		try {
			List<WebElement> myElements = new ArrayList<>();
			myElements.addAll(getElements(logicalName));
			
			for (WebElement e : myElements) {
				if (e.getText().contains(expectedText)) {
					isElementHaveText = true;
					logger.info("is Element '"+logicalName+"' have text: '"+expectedText+"'= "+isElementHaveText);
					action.takeScreenshotForAssertion();
					break;
				}
			}
		} catch (Exception e) {
			throw new CucumberException(e.getMessage());
		}
		if (!isElementHaveText) {
			Assert.fail(ELEMENT+ "'"+logicalName + "' Does not contains the '" + expectedText + "' "+TEXT);
		}

		setActionPerformed("AssertFieldContainsText");

		LoggerUtils.actionCompletedLog(getCallerMethodName());
		return isElementHaveText;
	}

	public void assertElementDisabled(String logicalName) {
		throw new PendingException(METHOD_NOT_IMPLEMENTED);
	}

	public void scrollSlider(String slider, String position, String slideBar) {
		throw new PendingException(METHOD_NOT_IMPLEMENTED);
	}

	public void switchToNewWindowAndClickElement(String locator) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		try {
			switchToNewWindowAssertElement(locator);
			click(locator);
		} catch (Exception e) {
			throw new CucumberException(e.getMessage(), e);
		}
		LoggerUtils.actionCompletedLog(getCallerMethodName());
	}

	public void assertTextInNewWindow(String expectedText, String logicalName) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		try {
			switchToNewWindowAssertElement(logicalName);
			assertTextDisplayed(expectedText);
		} catch (CucumberException e) {
			throw new CucumberException(e);
		}
		LoggerUtils.actionCompletedLog(getCallerMethodName());
	}

	public boolean clickByContextClick(String logicalName) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		boolean isElementClicked;
		try {
			WebElement element = getElement(logicalName);
			Actions actions = new Actions(driver);
			actions.contextClick(element);
			actions.build().perform();
			isElementClicked = true;
			logger.info("Right clicked on element- "+logicalName);
			waitForPageToLoad();
			takeScreenshot();
		} catch (Exception e) {
			throw new CucumberException(e.getMessage(), e);
		}
		LoggerUtils.actionCompletedLog(getCallerMethodName());
		return isElementClicked;
	}

	public boolean clickByMouse(String logicalName) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		boolean isElementClicked;
		try {
			Set<String> windowsBeforeClick = getWindowHandles();
			WebElement element = getElement(logicalName);
			Actions action = new Actions((RemoteWebDriver) driver);
			action.moveToElement(element).click().build().perform();
			logger.info("Right clicked on element- "+logicalName);
			isElementClicked = true;
			waitForPageToLoad();
			switchWindowIfExists(windowsBeforeClick);
		} catch (Exception e) {
			throw new CucumberException(e.getMessage(), e);
		}
		LoggerUtils.actionCompletedLog(getCallerMethodName());
		return isElementClicked;
	}

	public String getElementText(WebElement element) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		String elementText;
		elementText = Utilities.trimString(element.getText());
		if (elementText.equals("") && element.getTagName().equals("input"))  {
				elementText = element.getAttribute("value");
		}
		LoggerUtils.actionCompletedLog(getCallerMethodName());
		return elementText;
	}

	public void assertElementValueIncreased(String logicalName) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		double currentValue;
		double existingValue;
		try {
			WebElement element = getElement(logicalName);
			currentValue = Utilities.getIntegerValueAlone(getElementText(element));
			existingValue = Utilities.getIntegerValueAlone(getSavedData(logicalName));
		} catch (Exception e) {

			throw new CucumberException(e);
		}
		if (currentValue <= existingValue) {
			Assert.fail(logicalName + " Value is NOT increased,  Existing Value:" + existingValue + " Current vlaue:"
					+ currentValue);
		}
		action.takeScreenshotForAssertion();
		setActionPerformed("AssertFieldValueHasIncreased");

		LoggerUtils.actionCompletedLog(getCallerMethodName());
	}

	public void assertElementValueDecreased(String logicalName) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		double currentValue;
		double existingValue;
		try {
			WebElement element = getElement(logicalName);
			String currentText = getElementText(element);
			String existingText = getSavedData(logicalName);
			currentValue = Utilities.getIntegerValueAlone(currentText);
			existingValue = Utilities.getIntegerValueAlone(existingText);
		} catch (Exception e) {

			throw new CucumberException(e);
		}

		if (currentValue >= existingValue) {
			Assert.fail(logicalName + " Value is NOT Decreased,  Existing Value:" + existingValue + " Current vlaue:"
					+ currentValue);
		}
		action.takeScreenshotForAssertion();
		setActionPerformed("AssertFieldValueHasDecreased");

		LoggerUtils.actionCompletedLog(getCallerMethodName());
	}

	public void assertAnyElementHaveText(String logicalName, String expectedText) {
		assertElementsHaveText(ObjectRepository.get(logicalName), expectedText);
	}

	public void assertElementsHaveText(By locator, String expectedText) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		boolean isElementHaveText = false;
		try {
			String currentElementText;
			List<WebElement> elements;
			elements = getElements(locator);
			for (WebElement e : elements) {
				currentElementText = getElementText(e);
				if (currentElementText.equals(expectedText)) {
					isElementHaveText = true;
					logger.info(ELEMENT+ "'"+locator.toString()+"' is having text- "+expectedText);
					break;
				}
			}
		} catch (Exception e) {
			throw new CucumberException(e.getMessage());
		}
		if (!isElementHaveText) {
			Assert.fail(locator + " does not have the '" + expectedText + "' "+TEXT);
		}
		action.takeScreenshotForAssertion();
		setActionPerformed("AssertAnyFieldHasText");

	}

	public void assertAnyElementHaveOneOfTheText(By locator, String expectedText1, String expectedText2) {
		boolean isElementHaveText = false;
		try {
			List<WebElement> myElements = new ArrayList<>();
			myElements.addAll(getElements(locator));
			for (WebElement e : myElements) {
				String elementText = e.getText();
				if (elementText.equals(expectedText1) || elementText.equals(expectedText2)) {
					isElementHaveText = true;
					break;
				}
			}
		} catch (Exception e) {
			throw new CucumberException(e.getMessage());
		}
		if (!isElementHaveText) {
			Assert.fail(locator + " does not have the '" + expectedText1 + "' or '" + expectedText1 +"' "+TEXT);
		}
		action.takeScreenshotForAssertion();
		setActionPerformed("AssertAnyFieldHasBothText");

	}

	@Override
	public void assertCheckbox(String locator, String expectedStatus) {

		throw new PendingException(METHOD_NOT_IMPLEMENTED);
	}

	public boolean clickIfExists(String logicalName) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		boolean isElementClicked = false;
		if (isElementExists(logicalName, false)) {
			isElementClicked = click(logicalName);
		}
		LoggerUtils.actionCompletedLog(getCallerMethodName());
		return isElementClicked;
	}

	public boolean clickIfExists(By locator) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		boolean isElementClicked = false;
		if (isElementExists(locator, false, true)) {
			isElementClicked = click(locator, 0);
		}
		LoggerUtils.actionCompletedLog(getCallerMethodName());
		return isElementClicked;
	}

	@Override
	public void assertElementDisplayed(String logicalName, int waitTimeInSeconds) {

		throw new PendingException(METHOD_NOT_IMPLEMENTED);
	}

	public void switchOffGoogleTranslate() {
		throw new PendingException(METHOD_NOT_IMPLEMENTED);
	}

	@Override
	public String getElementText(String logicalName) {

		return getElementText(getElement(ObjectRepository.get(logicalName)));
	}

	@Override
	public void assertElementValueWithStoredValue(String logicalName, String storedVariableName) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		String expectedValue = getSavedData(storedVariableName);
		action.assertElementValue(logicalName, expectedValue);
		LoggerUtils.actionCompletedLog(getCallerMethodName());
	}

	@Override
	public void swipeDown() {
		throw new PendingException(METHOD_NOT_IMPLEMENTED);

	}

	public boolean clickOnVisibleTextWithinElement(String logicalName, String expectedText) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		boolean isTextFoundwithinElement = false;
		try {
			waitForElementToDisplay(logicalName);
			List<WebElement> myElements = getElements(logicalName);
			logger.debug(myElements + " Element found");
			for (WebElement e : myElements) {
				String actualText = getElementText(e);
				logger.debug("A Field Contains :" + actualText);
				if (actualText.equals(expectedText)) {
					logger.debug("Click Process Started for " + actualText);
					clickElement(e);
					logger.info( CLICKED_ON_ELEMENT+e.toString());
					isTextFoundwithinElement = true;
					break;
				}
			}
		} catch (Exception e) {
			throw new CucumberException(e.getMessage());
		}
		if (!isTextFoundwithinElement) {
			Assert.fail("Expected Text: " + expectedText + " is not found within" + logicalName);
		}
		LoggerUtils.actionCompletedLog(getCallerMethodName());
		return isTextFoundwithinElement;

	}

	@Override
	public boolean clickAndHoldTextboxByIndex(int index) {
		return false;
	}

	public void enterCurrentDate(String logicalName) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		try {
			action.enterText(Utilities.getRequiredDate(0, "dd.MM.yyyy", null), logicalName);
		} catch (Exception e) {
			throw new CucumberException(e.getMessage());
		}
		LoggerUtils.actionCompletedLog(getCallerMethodName());
	}

	public void quiteDriver() {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		try {
			if (driver != null) {
				deleteCookies();
				Set<String> windowHandles = getWindowHandles();
				while (!windowHandles.isEmpty()) {
					closeAllWindow(windowHandles);
					windowHandles = getWindowHandles();
				}
			}
		} catch (Exception exception) {
			logger.error(Constants.EXCEPTION_ON+ getCallerMethodName()+" :",exception);

		}
		LoggerUtils.actionCompletedLog(getCallerMethodName());
	}

	@Override
	public void closeWindow() {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		String activeWindow;
		activeWindow = getWindowHandle();
		action.takeScreenshot();
		if (!activeWindow.equals(getMainWindowHandle())) {
			driver.close();
			Utilities.waitFor(3000, "Child Window(" + activeWindow + ")  Closed.");
			action.switchToWindow(getMainWindowHandle());
			if (isAlertPresent()) {
				acceptAlertIfPresent();
			}
		}
		LoggerUtils.actionCompletedLog(getCallerMethodName());
	}


	public boolean touch(String logicalName) {
		return clickByJavaScript(logicalName);
	}

	@Override
	public void scrollToElement(String logicalName) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		waitForElementToDisplay(logicalName);
		executor.executeScript("arguments[0].scrollIntoView();", getElement(logicalName));
		logger.info("Scrolled to element "+logicalName);
	}

	@Override
	public void swipeUp() {
		throw new PendingException("Not Applical for Web");

	}

	public void getCoordinates() {
		WebElement image = driver.findElement(By.xpath("//input[@id='monthField']"));

		Point classname = image.getLocation();
		int xcordi = classname.getX();
		logger.debug("Element's Position from left side" + xcordi + " pixels.");
 		int ycordi = classname.getY();
		logger.debug("Element's Position from top" + ycordi + " pixels.");

	}

	public void clickCoordinates() {
		WebElement image = driver
				.findElement(By.cssSelector("div.eg_row.iconRow_height.eg_row--middle.store_dashed-row"));
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("window.scroll(" + 280 + ", " + 1689 + ");");
		executor.executeScript(JAVASCRIPT_ARGUMENTS_0_CLICK, image);
	}

	public void downloadPdf() {
		Utilities.waitFor(2000);
		if (currentBrowser == Browser.CHROME) {
			try {
				((AppiumDriver<?>) driver).context("NATIVE_APP");
				By  locator = By.xpath("//android.widget.Button[@text='DOWNLOAD']");
				clickIfExists(locator);

				locator = By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']");
				clickIfExists(locator);
			} catch (Exception exception) {
				logger.error(Constants.EXCEPTION_ON+ getCallerMethodName()+" :",exception);
			} finally {
				switchToWebView();
			}
		}
	}

	public void fileUpload(String fileName) throws IOException {
		Runtime.getRuntime().exec(fileName);

	}

	public void assertElementHasAnyValue(String logicalName) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		double currentValue;
		WebElement element = getElement(logicalName);
		String currentText = getElementText(element);
		currentValue = Utilities.getIntegerValueAlone(currentText);
		
		if (currentValue != 0) {
			logger.debug(currentText);
			Assert.assertTrue(true);
		} else {
			Assert.fail("No Value is present, its a blank field");
		}
		action.takeScreenshotForAssertion();
		setActionPerformed("Null Value check in the field");

		LoggerUtils.actionCompletedLog(getCallerMethodName());
	}

	public boolean isCheckboxChecked() {

		WebElement chkbox = driver.findElement(By.cssSelector("input[type=checkbox]:checked"));
		if (chkbox.isSelected()) {
			logger.debug("Checkbox is Toggled On");
		} else if (!chkbox.isSelected()) {
			logger.debug("Checkbox is Toggled Off");
		}
		return true;
	}

	public boolean isCheckboxUnChecked() {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		WebElement chkbox = driver.findElement(By.cssSelector("input[type=checkbox]"));
		if (chkbox.isSelected()) {
			logger.debug("Checkbox is Toggled On");
		} else if (!chkbox.isSelected()) {
			logger.debug("Checkbox is Toggled Off");
		}
		LoggerUtils.actionCompletedLog(getCallerMethodName());

		return true;
	}

	public void openApp() {
		LoggerUtils.methodNotImplemented(getCallerMethodName());
		throw new PendingException(getCallerMethodName()+METHOD_NOT_IMPLEMENTED);
	}

	public boolean clickTabKey(String logicalName) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		boolean isTabClicked;
		try {
			WebElement element = getElement(logicalName);
			element.sendKeys(Keys.TAB);
			logger.info("Tab key pressed");
			isTabClicked = true;
			waitForPageToLoad();
		} catch (Exception e) {
			throw new CucumberException(e.getMessage(), e);
		}
		LoggerUtils.actionCompletedLog(getCallerMethodName());
		return isTabClicked;
	}

	public boolean clickDownKey(String logicalName) {
		LoggerUtils.actionStartedLog(getCallerMethodName());
		boolean isTabClicked;
		try {
			WebElement element = getElement(logicalName);
			element.sendKeys(Keys.DOWN);
			logger.info("Down key pressed");
			isTabClicked = true;
			waitForPageToLoad();
		} catch (Exception e) {
			throw new CucumberException(e.getMessage(), e);
		}
		LoggerUtils.actionCompletedLog(getCallerMethodName());
		return isTabClicked;
	}

	public void switchToNative() {
		LoggerUtils.methodNotImplemented(getCallerMethodName());
		throw new PendingException("switchToNative "+METHOD_NOT_IMPLEMENTED);

	}

	public void switchToWebView() {
		LoggerUtils.methodNotImplemented(getCallerMethodName());
		throw new PendingException("switchToWebView "+METHOD_NOT_IMPLEMENTED);

	}

	public void saveData(String variableName, String value) {
		runtimeData.put(variableName, Utilities.trimString(value));

	}

	public String getSavedData(String logicalName) {
		String value;
		if (logicalName.indexOf("Store_") == 0) {
			logicalName = logicalName.replace("Store_", "");
		}

		if (runtimeData.containsKey(logicalName)) {
			value = runtimeData.get(logicalName);
		} else {
			throw new CucumberException("Value is not saved previously with this name '" + logicalName + "'");
		}

		return value;
	}

	@Override
	public Response get() {
		return  new APIRequestActions().get();
	}

	@Override
	public Response get(Headers headers, ResponseBody body) {
		return action.get(headers, body);
	}

	@Override
	public Response post() {
		return new APIRequestActions().post();
	}

	@Override
	public Response post(Headers headers, ResponseBody body) {
		return action.post(headers, body);
	}

	@Override
	public Response put() {
		return new APIRequestActions().put();
	}

	@Override
	public Response put(Headers headers, ResponseBody body) {
		return action.put(headers, body);
	}

	@Override
	public Response delete() {
		return new APIRequestActions().delete();
	}

	@Override
	public Response delete(Headers headers, ResponseBody body) {
		return action.delete(headers, body);
	}

	@Override
	public Response patch() {
		return new APIRequestActions().patch();
	}
}