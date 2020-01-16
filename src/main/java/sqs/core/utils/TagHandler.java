package sqs.core.utils;

import cucumber.api.Scenario;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sqs.core.constants.Browsers;
import sqs.core.constants.PropertyConstants;
import sqs.framework.FrameworkData;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Properties;

import static sqs.core.constants.Constants.EXCEPTION_ON;
import static sqs.framework.FrameworkData.*;

public class TagHandler {
	static final String TAGS_EXECUTED = "TagsAlreadyExecuted";
	static final String TAGS_NOT_EXECUTED = "TagsToExecute";
	static final String NOT_ABLE_TO_READ = "NotAbleToReadTag";
	static final String CONFIG_PATH = System.getProperty("user.dir") + File.separator + "Config" + File.separator;
	private static Logger logger = Logger.getLogger(TagHandler.class);

	private TagHandler(){

	}

	public static void printTagsToExecute(Properties config) throws Exception {
		String tags = "";

		String[] arrtags = tags.split(",");
		if (arrtags.length > 30) {
			StringBuilder tagsList=new StringBuilder();
			tagsList.append(arrtags[0]);
			for (int i = 1; i < 30; i++) {
				tagsList.append("," + arrtags[i]);
			}
			tags=tagsList.toString();
		}
		if (tags.equals("")) {
			tags = "@AllTagsExecuted";
		}
		FileUtils.writeTextNewFile("tags.to.execute=" + tags, CONFIG_PATH + config.getProperty(PropertyConstants.DEVICE_UDID) + ".tags");
		Utilities.waitFor(2);
	}


	public static String getTagsFromSheetToExecute(Properties config, int sheetNumber, String commonTagName) {
		String tagsToExecute = "";

		HashMap<String, String> tagStatus;
		tagStatus = getTagsToExecuteFromSheet(config.getProperty(PropertyConstants.REPORT_FILE), config.getProperty(PropertyConstants.DEVICE_NAME), sheetNumber);
		logger.debug(TAGS_NOT_EXECUTED + " in Sheet " + sheetNumber + " :" + tagStatus.get(TAGS_NOT_EXECUTED));
		logger.debug(TAGS_EXECUTED + " in Sheet " + sheetNumber + " :" + tagStatus.get(TAGS_EXECUTED));
		if (tagStatus.get(TAGS_EXECUTED).equals("")) {
			if (!tagStatus.get(TAGS_NOT_EXECUTED).equals("")) {
				tagsToExecute = commonTagName;
			}
		} else {
			tagsToExecute = tagStatus.get(TAGS_NOT_EXECUTED);
		}

		return tagsToExecute;
	}

	public static String getTagsToExecute(String resourceName, Properties config)  {
		String tags;

		if (Browsers.isBrowser(resourceName)) {
			tags = getTagsToExecuteFromSheet(config.getProperty(PropertyConstants.REPORT_FILE), resourceName, 0).get(TAGS_NOT_EXECUTED);
		} else {
			tags = getTagsToExecuteFromSheet(config.getProperty(PropertyConstants.REPORT_FILE), config.getProperty(PropertyConstants.DEVICE_NAME), 0).get(TAGS_NOT_EXECUTED);

		}
		if (!tags.isEmpty()) {
			FileUtils.writeTextExistingFile(FrameworkData.getDevicesAllottedForExecutionFile(), resourceName + ",," + Utilities.getCurrentDateAndTime());
			setConsolidatedTagsToExecute( Utilities.getAppendString(tags, getConsolidatedTagsToExecute(), "|"));
		}
		return tags;
	}


	private static HashMap<String, String> getTagsToExecuteFromSheet(String fileName, String deviceName, int sheetIndex) {
		HashMap<String, String> tagDetails = new HashMap<>();
		XSSFWorkbook workbook = null;
		fileName = FileUtils.getAbsolutePath(fileName);
		try (FileInputStream reportFileInputStream = new FileInputStream(fileName)) {
			workbook = new XSSFWorkbook(reportFileInputStream);
			XSSFSheet sheet;
			sheet = workbook.getSheetAt(sheetIndex);
			tagDetails = getTagListToExecute(sheet, deviceName);
		} catch (Exception exception) {
			tagDetails.put(TAGS_NOT_EXECUTED, NOT_ABLE_TO_READ);
			logger.error(EXCEPTION_ON + Utilities.getCallerMethodName() + " :" ,exception);
		}
		return tagDetails;
	}


	private static HashMap<String, String> getTagListToExecute(XSSFSheet sheet, String deviceName) {
		HashMap<String, String> tagsToExecute = new HashMap<>();
		tagsToExecute.put(TAGS_NOT_EXECUTED, "@NotAbleToReadExcel");
		tagsToExecute.put(TAGS_EXECUTED, "@NotAbleToReadExcel");
		StringBuilder tags = new StringBuilder();
		String scriptId;
		String executionStatus;
		String alreadyExecutedScript = "";
		int deviceColumn = ResultUtils.getDeviceColumnNumber(sheet, deviceName);
		if (deviceColumn == 0) {
			tagsToExecute.put(TAGS_NOT_EXECUTED, "@DeviceNotFoundInExcel");
			tagsToExecute.put(TAGS_EXECUTED, "@DeviceNotFoundInExcel");
			return tagsToExecute;
		}

		boolean currentRowHasRecord = true;
		int r = ResultUtils.TESTCASE_ID_STARTING_ROW_INDEX;

		while (r < 400 && currentRowHasRecord) {
			scriptId = ExcelUtilities.getCellValue(sheet, r, 0);
			executionStatus = ExcelUtilities.getCellValue(sheet, r, deviceColumn);
			if (scriptId.equals("")) {
				currentRowHasRecord = false;
			} else {
				if (executionStatus.equals("") || executionStatus.equals("Failed")) {
					if (tags.length() != 0) {
						tags.append(",");
					}
					tags.append("@" + scriptId);
				} else {
					StringBuilder executedScripts= new StringBuilder();
					if (!executionStatus.equalsIgnoreCase("Hold")) {
						executedScripts.append(scriptId);
					}
					alreadyExecutedScript=executedScripts.toString();

				}
			}
			r++;
		}

		if (r > ResultUtils.TESTCASE_ID_STARTING_ROW_INDEX + 1) {
			tagsToExecute.put(TAGS_NOT_EXECUTED, tags.toString());
			tagsToExecute.put(TAGS_EXECUTED, alreadyExecutedScript);
		}
		return tagsToExecute;

	}

	public static String getScenarioIDFromTag(Scenario scenario) {
		Object[] tags = scenario.getSourceTagNames().toArray();
		String scenarioID = "";
		if (tags.length == 1) {
			scenarioID = (String) tags[0];
		} else if (tags.length > 1) {
			String tag1;
			String tag2;
			tag1 = (String) tags[0];
			tag2 = (String) tags[1];
			if (tag1.contains("_") && tag1.indexOf('_') != tag1.lastIndexOf('_')) {
				scenarioID = tag1;
			} else if (tag2.contains("_") && tag2.indexOf('_') != tag2.lastIndexOf('_')) {
				scenarioID = tag2;
			} else if (tag1.contains("-")) {
				scenarioID = tag1;
			} else if (tag2.contains("-")) {
				scenarioID = tag2;
			}
		}
		return scenarioID;
	}
}
