package sqs.core.utils;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sqs.core.constants.PropertyConstants;
import sqs.framework.FrameworkData;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static sqs.core.constants.Constants.EXCEPTION_ON;
import static sqs.core.utils.Utilities.getCallerMethodName;
import static sqs.framework.FrameworkData.frameworkData;


public class ResultUtils {
	private static Logger logger=Logger.getLogger(ResultUtils.class);
	static final int TESTCASE_ID_STARTING_ROW_INDEX=1;
	static final int DEVICE_NAME_ROW_INDEX=0;
	static String excelFilePath;
	static Properties config;	
	public static void main (String[] a)
	{	
		
		if(a.length==0)
		{
			logger.error("Arguments 'DeviceUDID ScriptID Status ReportFileFullName' are mandatory.");
		}
		else if(a.length==4)
			{
			logger.debug("Updation Started");
			String deviceUDID;
			String deviceName;
			String scenarioID;
			String status;
			deviceUDID=a[0];
			scenarioID=a[1];
			status=a[2];
			deviceName=CSVUtilities.getDeviceFullName(deviceUDID);
			logger.debug("Excel file to update:"+a[3]);			
			config=new Properties();
			FrameworkData.isWindows();
			config.setProperty(PropertyConstants.REPORT_FILE,a[3]);
			updateResultsInReportFile(config,scenarioID,deviceName,status);
		}		
		else
		{
			Utilities.printArray(a);
logger.error("Insuffeciant Argument It Should be 'DeviceUDID ScriptID [Status] ReportFileFullName'");
		}
	    
	}
	
	public static void updateResultsInReportFile(Properties config,String scenarioId,String deviceName,String status)
	{
		scenarioId=scenarioId.replace("@", "");
		excelFilePath=config.getProperty(PropertyConstants.REPORT_FILE);
		excelFilePath=FileUtils.getAbsolutePath(excelFilePath);		
		FileUtils.waitAndLockTheFile(excelFilePath,"To Update Execution Status for "+scenarioId,true);
		logger.debug("Update status("+status+") for device("+deviceName+") and scenario ("+scenarioId+") in report file,");
		updateResultsIntoReportFile(excelFilePath,scenarioId,deviceName,status);
		FileUtils.releaseFileLock(excelFilePath,"To Update Execution Status for "+scenarioId);
	}
	@SuppressWarnings("UnusedAssignment")
	private static void updateResultsIntoReportFile(String fileName, String scenarioId, String deviceName, String status) {
	try {
			fileName = FileUtils.getAbsolutePath(fileName);
			try (FileInputStream reportFileInputStream = new FileInputStream(fileName)) {
				FileOutputStream reportFileOutputStream = null;
				XSSFWorkbook workbook = new XSSFWorkbook(reportFileInputStream);
				XSSFSheet sheet;
				sheet = workbook.getSheetAt(0);
				int rowNumberToUpdate;
				int columnNumberToUpdate;
				rowNumberToUpdate = getSenarioRowNumber(sheet, scenarioId);
				if (rowNumberToUpdate != 0) {
					columnNumberToUpdate = getDeviceColumnNumber(sheet, deviceName);
					if (columnNumberToUpdate != 0) {
						ExcelUtilities.updateValueInCell(sheet, rowNumberToUpdate, columnNumberToUpdate, status);
						reportFileOutputStream = new FileOutputStream(new File(fileName));
						workbook.write(reportFileOutputStream);
						reportFileOutputStream.close();
					}
				}
				workbook.close();
			}

		} catch (FileNotFoundException exception) {
			handleFileNotFoundException(exception,fileName,scenarioId,deviceName,status);
		} catch (Exception exception) {
			logger.error(EXCEPTION_ON + getCallerMethodName() + " :" ,exception);
		}


	}

	private static void handleFileNotFoundException(FileNotFoundException exception,String fileName, String scenarioId, String deviceName, String status) {
		String message = exception.getMessage();
		if (message.contains("(Resource busy)") || message.contains("The process cannot access the file because it is being used by another process")) {
			logger.debug("The File is already in Open status or It might be used by another process, Kindly close it, If Opened in any PC");
			Utilities.playBeepSound();
			int retryCount = 0;
			Utilities.waitFor(2000);
			if (retryCount < 100) {
				retryCount++;
				updateResultsIntoReportFile(fileName, scenarioId, deviceName, status);
			}
		} else if (exception.getMessage().contains("The system cannot find the file specified")) {
			logger.debug(exception.getMessage());
		} else {
			logger.error(EXCEPTION_ON + getCallerMethodName() + " :" ,exception);
		}

	}

	private static int getSenarioRowNumber(XSSFSheet sheet, String scenarioID) {
		String currentCellValue;
		boolean isEmptyRowFound=false;
		boolean isSenarioRowNumberIdentified=false;
		int identifiedRowNumber=0;
		int row=1;
		while (!isSenarioRowNumberIdentified && !isEmptyRowFound) 
		{
			currentCellValue=ExcelUtilities.getCellValue(sheet, row, 0);			
			if(currentCellValue.equals(""))
			{
				isEmptyRowFound=true;
			}
			else if (currentCellValue.equalsIgnoreCase(scenarioID))
			{
				identifiedRowNumber=row;
				isSenarioRowNumberIdentified=true;
			}	
			row++;
		}
		if(identifiedRowNumber==0)
		{
logger.error("Scenario Id '"+scenarioID+"' is not found in the excel sheet '"+sheet.getSheetName()+"'.");
		}
		return identifiedRowNumber;               
	}
	static int getDeviceColumnNumber(XSSFSheet sheet, String deviceName) {
		String currentCellValue;
		int columnNumber=0;
		Row row = sheet.getRow(DEVICE_NAME_ROW_INDEX);		
		for (int c=2;c<40;c++) 
		{					
			Cell currentCell = row.getCell(c);
			currentCellValue=ExcelUtilities.getCellValue(currentCell);
					if (currentCellValue.equalsIgnoreCase(deviceName))
					{
						columnNumber=currentCell.getColumnIndex();
						break;
					}			
		}		
		if (columnNumber==0)
		{
logger.error("Device Name '"+deviceName+"' is not found in the excel sheet '"+sheet.getSheetName()+"'.");
		}
		
		return columnNumber;               
	}
	
	static List<String> getAllDevices(XSSFSheet sheet) {
		String currentCellValue;
		List<String> devices=new ArrayList<>();
		Row row = sheet.getRow(DEVICE_NAME_ROW_INDEX);		
		for (int c=2;c<40;c++) 
		{					
			Cell currentCell = row.getCell(c);
			currentCellValue=ExcelUtilities.getCellValue(currentCell);
			if(!currentCellValue.isEmpty())
			{
				devices.add(currentCellValue.toLowerCase());
			}
		}		
		if (devices.isEmpty())
		{
			logger.error("There is no devices found in the excel sheet '"+sheet.getSheetName()+"'.");
		}
		else
		{
			logger.info("List of Resources in coverage '"+devices.toString()+"'.");
		}
		return devices;               
	}
	
	public static List<String> getDevicesInCoverage(String fileName) {
		List<String> devices = null;
		XSSFWorkbook workbook = null;
		int sheetIndex = 0;
		try {
			fileName = FileUtils.getAbsolutePath(fileName);
			try (FileInputStream reportFileInputStream = new FileInputStream(fileName)) {
				workbook = new XSSFWorkbook(reportFileInputStream);
				XSSFSheet sheet;
				sheet = workbook.getSheetAt(sheetIndex);
				devices = getAllDevices(sheet);
				workbook.close();
			}
		} catch (Exception exception) {
			logger.error(EXCEPTION_ON+ getCallerMethodName()+" :",exception);
		}
		return devices;
	}
	

	@SuppressWarnings("unused")
	private static String isTagExecuted(Properties config,String scenarioId,String deviceName) {
		String executionStatus = "";
		XSSFWorkbook workbook = null;
			scenarioId = scenarioId.replace("@", "");
			String phase = getPhaseNo(scenarioId);
			excelFilePath = config.getProperty(PropertyConstants.REPORT_FILE);
			excelFilePath = FileUtils.getAbsolutePath(excelFilePath);
			try(FileInputStream reportFileInputStream = new FileInputStream(excelFilePath)) {
				workbook = new XSSFWorkbook(reportFileInputStream);
				XSSFSheet sheet;
				int scenarioRowNumber;
				int deviceColumnNumber;
				sheet = workbook.getSheetAt(getSheetNumberForPhase(phase));
				scenarioRowNumber = getSenarioRowNumber(sheet, scenarioId);
				if (scenarioRowNumber != 0) {
					deviceColumnNumber = getDeviceColumnNumber(sheet, deviceName);
					if (deviceColumnNumber != 0) {
						executionStatus = ExcelUtilities.getCellValue(sheet, scenarioRowNumber, deviceColumnNumber);
					}
				}
			}
		 catch (Exception exception) {
			logger.error(EXCEPTION_ON+ getCallerMethodName()+" :",exception);
		}
		if (executionStatus.isEmpty()) {
			logger.debug("@@NotExecuted@@");
			return "@@NotExecuted@@";
		} else {
			logger.debug("@@AlreadyExecuted@@");
			return "@@AlreadyExecuted@@";
		}

	}

private static String getPhaseNo(String scenarioId)
{
	if(scenarioId.contains("_"))
	{
		return scenarioId.substring(scenarioId.indexOf('_')+1, scenarioId.indexOf('_',scenarioId.indexOf('_')+1));
	}
	else
	{
		return scenarioId.substring(scenarioId.indexOf('-')+1, scenarioId.indexOf('-',scenarioId.indexOf('-')+1));
	}
	
}

private static int getSheetNumberForPhase(String phase)
{
	int sheetNumber=2;
	if(phase.equalsIgnoreCase("P1"))
	{
		sheetNumber=1;
	}	
	else if(phase.equalsIgnoreCase("P3"))
	{
		sheetNumber=3;
	}	
	return sheetNumber;
}

}


