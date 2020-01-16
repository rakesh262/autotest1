package sqs.core.actions.driver.utils;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import sqs.core.utils.Utilities;


import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static sqs.core.constants.Constants.EXCEPTION_ON;
import static sqs.core.utils.Utilities.getCallerMethodName;

public class SeleniumUtils {

	private SeleniumUtils(){

	}
	private static Logger logger=Logger.getLogger(SeleniumUtils.class);

	public static By getLocator(String locatorType,String locator) {
		By by = null;
		try {
			switch (locatorType.toLowerCase()) {
				case "id":
					by = By.id(locator);
					break;
				case "classname":
					by = By.className(locator);
					break;
				case "name":
					by = By.name(locator);
					break;
				case "linktext":
					by = By.linkText(locator);
					break;
				case "partiallinktext":
					by = By.partialLinkText(locator);
					break;
				case "cssselector":
					by = By.cssSelector(locator);
					break;
				case "xpath":
					by = By.xpath(locator);
					break;
				case "tagname":
					by = By.tagName(locator);
					break;
				default:
					break;
			}
		} catch (Exception exception) {
			logger.error(EXCEPTION_ON + getCallerMethodName() ,exception);
		}
		return by;
	}
	
	public static void loadObjectsFromCSV(List<String> orLists, Map<String,By> objectRepository)
	{
		int lineNumber=1;
		try
		{			
		String locatorName ;
		String locatorType;
		String locatorValue;
		while(lineNumber<orLists.size()){
			String currentORDetails = orLists.get(lineNumber);
			if(!currentORDetails.isEmpty())
			{
				Pattern patern= Pattern.compile("(.*),(.*),(.*)");
				Matcher match=patern.matcher(new StringBuilder(currentORDetails).reverse());
				match.find();			
				locatorName=new StringBuilder(match.group(3)).reverse().toString();
				locatorType=new StringBuilder(match.group(2)).reverse().toString();
				locatorValue=new StringBuilder(match.group(1)).reverse().toString();
				By locator=SeleniumUtils.getLocator(locatorType, locatorValue);
				if(locator==null)
				{
					logger.error("Given Locator type '"+locatorType+"' is not valid in line "+lineNumber);
				}
				else
				{
					objectRepository.put(locatorName,locator );
				}
			}
			lineNumber++;
		}
		}catch(Exception e)
		{
			lineNumber++;
			logger.error("At line '"+lineNumber+"' while converting to object.");
			logger.error(e.getMessage());
			throw e;
		}
		
		
		logger.debug("Page Objects load process Completed.");
	}
	public static int getYpointFromMesssage(String message) {
		logger.debug(message);
		int y=0;
		try
		{
		String yValue = message.substring(message.indexOf("is not clickable at point ("), message.indexOf("). OTHER"));
		yValue = yValue.substring(yValue.indexOf(',')+2);
		y = 0;
		y = Utilities.getInteger(yValue);
		}
		catch(Exception exception) {
			logger.error("Not able to find y position from this message" + message);
			logger.error("<<<End of message>>>");
			logger.error(EXCEPTION_ON +getCallerMethodName()+" :",exception);
		}
		return y;
	}
}
