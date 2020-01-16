package sqs.core.utils;

import org.apache.log4j.Logger;
import sqs.core.constants.Browsers;
import sqs.core.constants.PropertyConstants;
import sqs.framework.FrameworkData;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static sqs.core.constants.Constants.EXCEPTION_ON;
import static sqs.core.utils.Utilities.getCallerMethodName;


public class CSVUtilities {
	private CSVUtilities(){

	}
	private static Logger logger = Logger.getLogger(CSVUtilities.class);

	public static List<String> getAllLineFromCSV(String fileFullName)  {
		File file = null;
		List<String> fileInfo = new ArrayList<>();
		FileInputStream fis = null;
		 try {
			 file = new File(fileFullName);
			 fileInfo = new ArrayList<>();
			 fis = new FileInputStream(file);
			 getFileInfo(fileInfo, fis);
		 }catch(Exception exception){
			 logger.error(EXCEPTION_ON+ getCallerMethodName()+" :",exception);
		 }
		return fileInfo;
	}

	private static List<String> getFileInfo(List<String> fileInfo, FileInputStream fis) {
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8))) {
			while (bufferedReader.ready()) {
				String currentLine = bufferedReader.readLine();
				currentLine = Utilities.trimString(currentLine);
				fileInfo.add(currentLine);
			}
			fis.close();
		} catch (IOException e1) {
			logger.error(EXCEPTION_ON+ getCallerMethodName()+" :"+ e1.getMessage());
		}
		return fileInfo;
	}

	public static Map<String, String> getResourceInfo(String resourceId)   {
		Map<String, String> dataFromCSV = null;
		String[] deviceInfo;
		try {
			deviceInfo = getDeviceInfoFromCSV(resourceId, FrameworkData.DEVICE_DETAILS_CSV);
			if (Browsers.isBrowser(resourceId)) {
				FrameworkData.setCurrentBrowser( Browsers.getBrowser(resourceId));
				dataFromCSV = loadInfoInHashMap(deviceInfo[0]);
				dataFromCSV.put(PropertyConstants.DEVICE_NAME, resourceId);
				dataFromCSV.put(PropertyConstants.DEVICE_SHORT_NAME, resourceId);
				dataFromCSV.put(PropertyConstants.DEVICE_OS, OSUtilities.getOperatingSystemType().toString());
			}
			if (!deviceInfo[1].equals("")) {
				dataFromCSV = loadInfoInHashMap(deviceInfo[0], deviceInfo[1]);
			}
		} catch (Exception exception) {
			logger.error(EXCEPTION_ON+ getCallerMethodName()+" :",exception);
		}
		return dataFromCSV;
	}

	public static void printResourceName(String resourceID)  {
		Map<String, String> deviceInfo ;
		try{
			if (Browsers.isBrowser(resourceID)) {
				logger.info("Identified Resource to execute: " + resourceID);
			} else {
				if (!resourceID.isEmpty()) {
					deviceInfo = getResourceInfo(resourceID);
					if(deviceInfo!=null) logger.info("Device>" + deviceInfo.get(PropertyConstants.DEVICE_SHORT_NAME) + " and UDID is >" + resourceID);
				}
			}
		}catch ( NullPointerException exception){
			logger.error(EXCEPTION_ON+ getCallerMethodName()+" :",exception);
		}

	}

	public static String getDeviceFullName(String udid)  {
		Map<String, String> deviceInfo = getResourceInfo(udid);
		if (deviceInfo == null) {
			logger.error("Given UDID '" + udid + "' is not available in Device.csv");
			return "";
		} else {
			return deviceInfo.get(PropertyConstants.DEVICE_NAME);
		}
	}

	private static String[] getDeviceInfoFromCSV(String resourceId, String deviceDetailFile) {
		String[] deviceInfo = {"", ""};
		File deviceListFile = null;
		boolean isDeviceIdentified = false;
		BufferedReader bufferedReader = null;
		String currentLine;
		deviceListFile = new File(deviceDetailFile);
		try (FileInputStream fis = new FileInputStream(deviceListFile)) {
			bufferedReader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8)); //To support German Special charcter.
			deviceInfo[0] = bufferedReader.readLine();
			if (!Browsers.isBrowser(resourceId)) {
				while (bufferedReader.ready() && !isDeviceIdentified) {
					currentLine = bufferedReader.readLine();
					if (currentLine.contains(resourceId)) {
						isDeviceIdentified = true;
						deviceInfo[1] = currentLine;
					}
				}
			}
		} catch (Exception e) {
			logger.error(EXCEPTION_ON + getCallerMethodName() + e.getMessage());
		}
		return deviceInfo;
	}

	public static String getDeviceNumberFromCSV(String udid, String deviceDetailFile) throws IOException {
		File deviceListFile = null;
		String currentLine;
		String deviceIdentifiedIn = "00";
		int lineNumber = 0;
		deviceListFile = new File(deviceDetailFile);
		try (FileInputStream fis = new FileInputStream(deviceListFile)) {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
			while (bufferedReader.ready()) {
				currentLine = bufferedReader.readLine();
				if (currentLine.contains(udid)) {
					deviceIdentifiedIn = String.format("%02d", lineNumber);
					break;
				}
				lineNumber++;
			}
		}
		return deviceIdentifiedIn;
	}

	private static HashMap<String, String> loadInfoInHashMap(String header, String data) {

		HashMap<String, String> dataFromCSV = new HashMap< >();
		String[] headerAsArray;
		String[] dataAsArray;
		headerAsArray = header.split(",");
		dataAsArray = data.split(",");
		String value;
		for (int i = 0; i < headerAsArray.length; i++) {
			value = "";
			try {
				value = dataAsArray[i];
			} catch (ArrayIndexOutOfBoundsException exception) {
				logger.error(EXCEPTION_ON+ getCallerMethodName()+" :",exception);
			}
			dataFromCSV.put(headerAsArray[i].toUpperCase().trim(), value);
		}
		return dataFromCSV;
	}

	private static HashMap<String, String> loadInfoInHashMap(String header) {

		HashMap<String, String> dataAsHasMap = new HashMap< >();
		String[] headerAsArray;
		headerAsArray = header.split(",");
		String value;
		for (int i = 0; i < headerAsArray.length; i++) {
			value = "";
			dataAsHasMap.put(headerAsArray[i].toUpperCase().trim(), value);
		}
		return dataAsHasMap;
	}

	public static void loadDeviceInfoInProperties(String udid, Properties properties){
		Map<String, String> deviceInfo = getResourceInfo(udid);
		properties.putAll(deviceInfo);
	}
}
