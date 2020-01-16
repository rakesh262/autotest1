package sqs.core.utils;

import com.jayway.awaitility.Awaitility;
import com.jayway.awaitility.Duration;
import org.apache.log4j.Logger;
import sqs.core.constants.Constants;
import sqs.framework.FrameworkData;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static sqs.core.constants.Constants.DO_NOTHING;
import static sqs.framework.FrameworkData.frameworkData;

/**
 * @ScriptName    : Utilities
 * @Description   : This class contains   
 * @Author        : Automation Tester (SQS)
 * @Creation Date : 20 July 2015   @Modified Date:                       
 */
public class Utilities
{
	private static Logger logger=Logger.getLogger(Utilities.class);

 	public static String getDateAndTimeForFile()
	{
		SimpleDateFormat dateFormatForFiles =new SimpleDateFormat("yyyyMMdd_HHmmss");
		return dateFormatForFiles.format(new Date());
	}
	public static String getDateAndTimeForLogging()
	{
		SimpleDateFormat dateFormatForLogging =new SimpleDateFormat("dd-MM-yyyy HH:mm ss SSS");
		return dateFormatForLogging.format(new Date());
	}	

	public static String getCurrentDateAndTime()
	{
		SimpleDateFormat dateFormatForDateFields =new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return dateFormatForDateFields.format(new Date());
	}

	/**
	 * Method: getRequiredDate
	 * Description: This method will give require date
	 * @param incrementDays Number by which user want increase date
	 * @param expectedDateFormat - User expected date format
	 * 		eg. 9 april 2014 --- dd/MM/yyyy -> 09/04/2015, dd-MM-yyyy -> 09-04-2015
	 * @param timeZoneId - Time Zone
	 * @author Automation Tester (SQS) Creation Date:  20 July 2015  Modified Date:
	 */
	public static String getRequiredDate (int incrementDays, String expectedDateFormat, String timeZoneId)
	{
		try 
		{
			DateFormat dateFormat;
			Calendar calendar = Calendar.getInstance();
			dateFormat = new SimpleDateFormat(expectedDateFormat);
			if(timeZoneId != null && ! timeZoneId.equals(""))
				dateFormat.setTimeZone(TimeZone.getTimeZone(timeZoneId));
			calendar.add(Calendar.DAY_OF_MONTH, incrementDays);
			Date tomorrow = calendar.getTime();
			return dateFormat.format(tomorrow);
		}
		catch (Exception exception)
		{
			logger.error(Constants.EXCEPTION_ON+Utilities.getCallerMethodName()+" :"+ exception.getMessage());
			return null;
		}
	}

	/**
	 * Method: copyFileUsingStream
	 * Description:
	 * @param source Source file location
	 * @param dest Target location
	 * @author Automation Tester (SQS) Creation Date:  20 July 2015 Modified Date:
	 */
	public void copyFileUsingStream(File source, File dest) throws IOException
	{
	  try(InputStream fis  = new FileInputStream(source)){
			try(OutputStream os = new FileOutputStream(dest)) {
				byte[] buffer = new byte[1024];
				int length;
				while ((length = fis.read(buffer)) > 0) {
					os.write(buffer, 0, length);
				}
			}
		}
		catch(Exception exception)
		{
			logger.error(Constants.EXCEPTION_ON+Utilities.getCallerMethodName()+" :"+ exception.getMessage());
		}

	}




		/**
	 * Method: waitFor
	 * Description: timeInSeconds for the specified amount of [timeInSeconds].
	 * @author Automation Tester (SQS) Creation Date:  20 July 2015 Modified Date:
	 */

	public static void waitFor(int timeInMilliSeconds)
	{
		waitFor(timeInMilliSeconds,"");

	}


	public static List<String> getConnectedDevices() {
        String applicationType = "";
        List<String> connectedDevices = new ArrayList<>();
        try {
        	String appType= FrameworkData.getApplicationType();
           // if (appType.toLowerCase().contains(",") ||appType.toLowerCase().contains("nativeapp") || appType.toLowerCase().contains("hybridapp") || appType.toLowerCase().contains("mobileweb")) {
                String[] s = appType.split(",");
                for (String st : s) {
                    if (!st.toLowerCase().contains("desktopweb")) {
                        applicationType = st.toLowerCase();
                    }
					if (applicationType.contains("nativeapp") || applicationType.contains("hybridapp") || applicationType.contains("mobileweb")) {
						getConnectedDevicesMobile(connectedDevices);
					}
                }
           // }

        } catch (Exception exception) {
            logger.error(Constants.EXCEPTION_ON + Utilities.getCallerMethodName() + " :" + exception.getMessage());
        }
        return connectedDevices;
    }

	private static void getConnectedDevicesMobile(List<String> connectedDevices) {
		try {
			Process process;
			if (FrameworkData.isWindows()) {
				process = Runtime.getRuntime().exec("adb devices");
			} else {
				process = Runtime.getRuntime().exec("idevice_id -l");
			}
			process.waitFor();
			readDeviceInfoFromProcessStream(process.getInputStream(), connectedDevices);
		} catch (Exception exception) {
			logger.error(Constants.EXCEPTION_ON + Utilities.getCallerMethodName() + " :" + exception.getMessage());
		}
	}


	private static List<String> readDeviceInfoFromProcessStream(InputStream inputStream, List<String> connectedDevices) {
		String currentLine;
		try(BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))){
			while ((currentLine = br.readLine()) != null) {
				String udid = "";
				currentLine = Utilities.trimString(currentLine);
				if (!"List of devices attached".equals(currentLine) && !currentLine.equals("")) {
					if (currentLine.contains("device")) {
						udid = currentLine.split("	")[0];
					} else if (!FrameworkData.isWindows()) {
						udid = currentLine;
					}
					connectedDevices.add(udid);
				}
			}
		} catch (IOException exception) {
			logger.error(Constants.EXCEPTION_ON+Utilities.getCallerMethodName()+" :", exception);
		}
		return connectedDevices;

	}

	public static String trimString(String value)
	{
		if(value==null)
		{
			value="";
		}
		else
		{
			value=value.trim();
		}
		return value;
	}

	public static boolean isWindows()
	{
		boolean isThisWindow=false;
		String os=System.getProperty("os.name");
		if(os.contains("Windows"))
		{
			isThisWindow=true;
		}
		return isThisWindow;
	}
	public static boolean isNumeric(String s) {
		s=trimString(s);
		if(s.equals(""))
		{
			return false;
		}
		else
		{
			return s.matches("[-+]?\\d*\\.?\\d+");
		}
	}

	public static int getInteger(String s) {
		s=trimString(s);
		if(s.equals(""))
		{
			return 0;
		}
		else if(isNumeric(s))
		{
			return Integer.parseInt(s);
		}
		else
		{
			return 0;
		}
	}

	public static double getDouble(String s) {
		s=trimString(s);
		if(s.equals(""))
		{
			return 0;
		}
		else if(isNumeric(s))
		{
			return Double.parseDouble(s);
		}
		else
		{
			return 0;
		}
	}
	public static String  removePrefixAndSufix(String text)
	{
		text=text.replace("€", "");
		text=text.replace("EUR", "");
		text=text.replace("z.B.", "");
		text=text.replace(",", "");
		text=text.replace(" ", "");
		return text;
	}


	public static double getGermatIntegerAsIndianInteger(String germanformatNumber)
	{
		double indianFormatNumber=0.00;
		if(!germanformatNumber.equals(""))
		{
			germanformatNumber=removePrefixAndSufix(germanformatNumber);
		germanformatNumber=germanformatNumber.replace(".", "");
		germanformatNumber=germanformatNumber.replace(",", ".");

		indianFormatNumber=getDouble(germanformatNumber);
		}
		return indianFormatNumber;

	}


	public static String escapeSpecialCharecters(String text)
	{

		    return text.replace("/", "//");

	}
	public static void printErrorMessage(String message)
	{

		   logger.error(getDateAndTimeForLogging()+" : #Error "+message);

	}

	public static void printInformationMessage(String message) {
		logger.error(getDateAndTimeForLogging()+" : "+message);
	}
	public static int getIntegerValueAlone(String givenValue)
	{
		int availableInteger=0;
		if(givenValue!=null)
		{

			givenValue=givenValue.replaceAll("[^0-9]","");
			if(!givenValue.isEmpty())
			{
				availableInteger=Integer.parseInt(givenValue);
			}
		}
		return availableInteger;
	}

	public static void waitFor(int timeInMilliSeconds, String message) {
		try {
			Awaitility.await().pollDelay(new Duration(timeInMilliSeconds, TimeUnit.MILLISECONDS)).until(() -> true);
			float s=0;
			if(timeInMilliSeconds!=0)
			{
			s=(float)timeInMilliSeconds/1000;
			}
			logger.debug("Waited For :"+s+" seconds "+message);
		} catch (IllegalStateException e){
			logger.debug(DO_NOTHING	);
		} catch (Exception exception) {
			logger.error(Constants.EXCEPTION_ON+Utilities.getCallerMethodName()+" :"+ exception.getMessage());
		}

	}
	
	public static void playBeepSound()
	{
		for(int i=0;i<5;i++)
		{
			java.awt.Toolkit.getDefaultToolkit().beep();
			Utilities.waitFor(500,"Play Beep sound");
		}
	}
	
	public static String getAppendString(String stringToAppend,String consolidatedString,String delimiter)
	{
		if(stringToAppend==null)
		{
			stringToAppend="";
		}
		if(consolidatedString==null)
		{
			consolidatedString="";
		}
		if(delimiter==null)
		{
			delimiter=",";
		}
		String appendedString=stringToAppend;
		
		if(!consolidatedString.equals(""))
		{
			appendedString=consolidatedString+delimiter+stringToAppend;
		}
		return appendedString;		
	}
	public static  void printArray(String[] a)
	{
		for( String s : a)
		{
			logger.debug("value:"+s);
		}
	}

	public static boolean isClassExists(String classFullPath)
	{
		boolean isClassfound=false;
		try {
			 Class.forName(classFullPath);
			 isClassfound=true;
		} catch (ClassNotFoundException e) {
			logger.debug(DO_NOTHING);
		}
		return isClassfound;
		
	}

	public static String getCallerMethodName() {
		return  Thread.currentThread().getStackTrace()[2].getMethodName();
	}


}
