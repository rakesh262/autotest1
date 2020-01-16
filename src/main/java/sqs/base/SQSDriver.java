package sqs.base;

import org.apache.log4j.Logger;
import sqs.core.actions.driver.utils.AppiumServerUtilities;
import sqs.core.constants.ApplicationTypes;
import sqs.core.constants.ApplicationTypes.ApplicationType;
import sqs.core.constants.Browsers;
import sqs.core.constants.Constants;
import sqs.core.constants.PropertyConstants;
import sqs.core.utils.*;
import sqs.framework.FrameworkBase;
import sqs.framework.FrameworkData;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import static sqs.core.constants.Constants.API_TEST_URL;
import static sqs.core.constants.Constants.REPORTS_PATH;


public class SQSDriver extends FrameworkData {
    private static final boolean IS_MANDATORY = true;
    private static final boolean IS_NOT_MANDATORY = false;
    protected static String featureFilePath;
    private static Logger logger = Logger.getLogger(SQSDriver.class);
    private static boolean isIssueFound = false;
    private static String tagsGivenByUser = "";
    private static String reportFolderName = "/Result/Reports/";
    private static List<String> resourcesInCoverage;

    public static void main(String[] args) {

        FileUtils.createFolderIfNotExist(PROJECT_PATH + reportFolderName);
        try {
            if (args.length >= 1) {
                setUserDefinedProjectNameAndTags(args);
            }
            config = new Properties();
            tagsChoosenByTestRunner = false;
            manualTestRunner = false;
            updateStatusInExcel = false;//for skipping excelTags,AutoGeneratingTestrunner,StatusUpdationInExcel
            loadProperties();
            resourcesInCoverage = ResultUtils.getDevicesInCoverage(config.getProperty(PropertyConstants.REPORT_FILE));
            validateConfigurationFile(projectName);
            apiRunFlag = Boolean.parseBoolean(config.getProperty("api.RUN.flag"));
            validateAndRemoveDeviceLockfile();
            if (isIssueFound) {
                logger.fatal("There is an error in Properties file. Please validate and correct the file and restart the execution.");
            } else {
                if (!apiRunFlag)
                    startExecution();
                else
                    startApiTest();
            }

        } catch (Exception exception) {
            logger.error(Constants.EXCEPTION_ON + Utilities.getCallerMethodName() + " :", exception);
        }

        try {
            FileUtils.removeLineContainsText(devicesAllottedForExecutionFile, testSuiteIDForCurrentJVM);
        } catch (Exception exception) {

            logger.error(Constants.EXCEPTION_ON + Utilities.getCallerMethodName() + " :", exception);
        }
        logger.debug("<<<<<<<<<< Execution Completed >>>>>>>>>>");
    }


    public static void loadAllValues() {
        try {
            config = new Properties();
            isWindows();
            FileUtils.createFolderIfNotExist(reportDirPath);
            validateAndRemoveDeviceLockfile();
            loadProperties();
            apiRunFlag = Boolean.parseBoolean(config.getProperty("api.RUN.flag"));
            resourcesInCoverage = ResultUtils.getDevicesInCoverage(config.getProperty(PropertyConstants.REPORT_FILE));
            if(!apiRunFlag) {
                validateConfigurationFile(projectName);
            }

            if (isIssueFound) {
                logger.fatal("There is an error in Properties file. Please validate and correct the file and restart the execution.");
            } else {
                if (!apiRunFlag)
                    startExecution();
                else
                    startApiTest();
            }
        } catch (Exception exception) {
            logger.error(Constants.EXCEPTION_ON + Utilities.getCallerMethodName() + " :", exception);
        }
        try {
            FileUtils.removeLineContainsText(devicesAllottedForExecutionFile, testSuiteIDForCurrentJVM);
        } catch (Exception exception) {
            logger.error("Error on load SQS Driver values ", exception);
        }
        logger.debug("<<<<<<<<<< Execution Completed >>>>>>>>>>");
    }



    private static void loadProperties() {
        config = PropertyUtils.loadProperties(PROJECT_PATH + File.separator + Constants.CONFIG + File.separator + "Main.properties");
        if (projectName.isEmpty()) {
            projectName = config.getProperty(PropertyConstants.PROJECT_NAME);
        }
        Properties projectConfig = PropertyUtils.loadProperties(PROJECT_PATH + Constants.CONFIG + File.separator + projectName + ".properties");
        config.putAll(projectConfig);
    }


    private static void setUserDefinedProjectNameAndTags(String[] arguments) {
        for (String currentArgument : arguments) {
            if (!currentArgument.contains("=")) {
                projectName = currentArgument;
            } else {
                if (currentArgument.toLowerCase().contains("tags=")) {
                    tagsGivenByUser = currentArgument.split("=")[1];
                }
            }
        }
    }

    private static void startExecution() {
        consolidatedTagsToExecute = "TagsNotPicked";
        while (!consolidatedTagsToExecute.isEmpty()) {
            createDeviceLockingFile();
            executeOnAvailableResource();
            resetTagsGivenByUser();
            if (consolidatedTagsToExecute.isEmpty()) {
                Utilities.waitFor(5000, "Wait for devices to complete other execution.");
            }
            logger.info("Tags and Devices to Execute:" + consolidatedTagsToExecute);
            Utilities.waitFor(10000, "Next Cycle");
        }
    }

    private static void validateConfigurationFile(String projectName) {
        String fileToVerify;
        try {
            logger.info("Project Path " + PROJECT_PATH);
            //fileToVerify = TEST_RESORUCES_PATH + "Features/" + projectName;
            //FileUtils.checkFileExists(fileToVerify, "Feature file directory");
            fileToVerify = PROJECT_PATH + "Result/Reports";
            FileUtils.checkFileExists(fileToVerify, "Output Folder");
            fileToVerify = PROJECT_PATH + "Config/" + projectName + ".Properties";
            FileUtils.checkFileExists(fileToVerify, "Project config file");
            int value = Utilities.getInteger(config.getProperty(PropertyConstants.OBJECTLOAD_WAITTIME));
            if (value != 0) {
                implicitWaitTime = value;

            }
            value = Utilities.getInteger(config.getProperty(PropertyConstants.PAGELOAD_WAITTIME));
            if (value != 0) {
                FrameworkBase.pageLoadWaitTime = value;
            }
            if (!isIssueFound) {
                validateProjectConfigFile(projectName);
            }

        } catch (Exception exception) {
            logger.error(Constants.EXCEPTION_ON + Utilities.getCallerMethodName() + " :", exception);
        }
    }

    private static boolean checkPropertyContainsValue(String key, boolean isMandatory) {
        if (config.containsKey(key)) {
            String value = Utilities.trimString(config.getProperty(key));
            if (value.equals("")) {
                if (isMandatory) {
                    logger.error("The Property '" + key + "' is mandatory. Please fill the value.");
                    isIssueFound = true;
                }
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    private static void validateProjectConfigFile(String projectName) {

        String projectConfigFile;
        try {
            projectConfigFile = PROJECT_PATH + "Config/" + projectName + ".Properties";
            Properties projectConfig;
            projectConfig = PropertyUtils.loadProperties(projectConfigFile);
            config.putAll(projectConfig);
            setFeaturePath();
            validateORConfig();
            FileUtils.checkFileExists(config.getProperty(PropertyConstants.REPORT_FILE), "Report file");
            validateBrowserAndURL();
            checkPropertyContainsValue(PropertyConstants.APPLICATION_TYPE, IS_MANDATORY);
            applicationType = config.getProperty(PropertyConstants.APPLICATION_TYPE, Constants.DESKTOP_WEB);
            FrameworkData.takeScreenshotForAssetionStepsAlone = getBooleanValue(PropertyConstants.TAKE_SCREENSHOT_FOR_ASSETION_ALONE);

        } catch (Exception exception) {
            logger.error(Constants.EXCEPTION_ON + Utilities.getCallerMethodName() + " :", exception);
        }

    }

    private static void validateBrowserAndURL() {

        if (!checkPropertyContainsValue(PropertyConstants.APPLICATION_TYPE, IS_NOT_MANDATORY)) {
            config.setProperty(PropertyConstants.APPLICATION_TYPE, Constants.DESKTOP_WEB);
        }

        if (config.getProperty(PropertyConstants.APPLICATION_TYPE).equalsIgnoreCase(Constants.DESKTOP_WEB)) {
            checkPropertyContainsValue(PropertyConstants.DESKTOP_BROWSER_NAME, IS_MANDATORY);
            checkPropertyContainsValue(PropertyConstants.APPLICATION_URL, IS_MANDATORY);
        } else if (config.getProperty(PropertyConstants.APPLICATION_TYPE).equalsIgnoreCase(Constants.MOBILE_WEB)) {
            checkPropertyContainsValue(PropertyConstants.ANDROID_BROWSER_NAME, IS_MANDATORY);
            checkPropertyContainsValue(PropertyConstants.IOS_BROWSER_NAME, IS_MANDATORY);
            checkPropertyContainsValue(PropertyConstants.APPLICATION_URL, IS_MANDATORY);
        } else if (applicationType.equalsIgnoreCase(Constants.NATIVE_APP) || applicationType.equalsIgnoreCase(Constants.HYBRID_APP)) {
            if (!checkPropertyContainsValue(PropertyConstants.APK_FILE_PATH, IS_NOT_MANDATORY)) {
                checkPropertyContainsValue(PropertyConstants.APP_PACKAGE, IS_MANDATORY);
                checkPropertyContainsValue(PropertyConstants.APP_ACTIVITY, IS_MANDATORY);
            }
            if (!checkPropertyContainsValue(PropertyConstants.IPA_FILE_PATH, IS_NOT_MANDATORY)) {
                checkPropertyContainsValue(PropertyConstants.APP_PACKAGE, IS_MANDATORY);
                checkPropertyContainsValue(PropertyConstants.APP_ACTIVITY, IS_MANDATORY);
            }
        }
    }

    @SuppressWarnings("unused")
    private static void validateORFile() {
        String applicationType = config.getProperty(PropertyConstants.APPLICATION_TYPE);
        if (applicationType.toLowerCase().contains(Constants.HYBRID_APP.toLowerCase()) || applicationType.toLowerCase().contains(Constants.NATIVE_APP.toLowerCase())) {
            validateMobileOR();
        } else {
            validateDesktopOR();
        }
        if (isIssueFound) {
            logger.error("PageObjects file name is not configured properly.");
        }
    }


    private static void validateORConfig() {
        checkPropertyContainsValue(PropertyConstants.PAGE_OBJECTS_PATH, IS_MANDATORY);
        if (!isIssueFound) {
            String objectRepositoryFileName = config.getProperty(PropertyConstants.PAGE_OBJECTS_PATH);
            String[] objectRepositoryFiles = {objectRepositoryFileName};
            if (objectRepositoryFileName.contains(",")) {
                objectRepositoryFiles = objectRepositoryFileName.split(",");
            }
            for (String currentORFile : objectRepositoryFiles) {
                FileUtils.checkFileOrFolderExists(currentORFile, "Page Objects");
            }
        }


    }

    private static void validateMobileOR() {
        checkPropertyContainsValue(PropertyConstants.IOS_OR, IS_MANDATORY);
        checkPropertyContainsValue(PropertyConstants.ANDROID_OR, IS_MANDATORY);
        if (!isIssueFound) {
            checkObjectRepositoryFileNames(config.getProperty(PropertyConstants.IOS_OR));
            checkObjectRepositoryFileNames(config.getProperty(PropertyConstants.ANDROID_OR));
        }
    }

    private static void checkObjectRepositoryFileNames(String objectRepositoryFileName) {
        objectRepositoryFileName = PROJECT_PATH + objectRepositoryFileName;
        FileUtils.checkFileExists(objectRepositoryFileName, "PageObjects file");
    }

    private static void validateDesktopOR() {
        checkPropertyContainsValue(PropertyConstants.DESKTOP_OR, IS_MANDATORY);
        if (!isIssueFound) {
            String objectRepositoryFileName = config.getProperty(PropertyConstants.DESKTOP_OR);
            String[] objectRepositoryFiles = {objectRepositoryFileName};
            if (objectRepositoryFileName.contains(",")) {
                objectRepositoryFiles = objectRepositoryFileName.split(",");
            }
            for (String currentORFile : objectRepositoryFiles) {
                objectRepositoryFileName = PROJECT_PATH + "PageObjects\\";// + currentORFile
                FileUtils.checkFileExists(objectRepositoryFileName, "PageObjects file");
            }
        }

    }


    private static boolean isResourceAlreadyInExecution(String resourceID) {
        boolean isBlocked = false;
        resourceID = Utilities.trimString(resourceID);
        try {
            if (!resourceID.equals("")) {
                FileUtils.createFileIfNotExists(devicesAllottedForExecutionFile);
                if (FileUtils.isFileContainsText(devicesAllottedForExecutionFile, resourceID)) {
                    isBlocked = true;
                }
            }
        } catch (Exception exception) {
            logger.error(Constants.EXCEPTION_ON + Utilities.getCallerMethodName() + " :", exception);
        }
        return isBlocked;
    }


    private static void releaseResource() {
        if (config.getProperty(PropertyConstants.DEVICE_NAME) == null) {
            logger.debug("--------------------------------");
        } else if (resourceIdentifiedForExecution.equals("")) {
            logger.error("New devices are not connected with this system.");
            try {
                FileUtils.printBlockedDeviceDetails(devicesAllottedForExecutionFile);
            } catch (Exception exception) {
                logger.error(Constants.EXCEPTION_ON + Utilities.getCallerMethodName() + " :", exception);
            }
        } else {
            try {
                FileUtils.removeLineContainsText(devicesAllottedForExecutionFile, resourceIdentifiedForExecution);
                resourceIdentifiedForExecution = "";
            } catch (Exception exception) {
                logger.error(Constants.EXCEPTION_ON + Utilities.getCallerMethodName() + " :", exception);
            }
            AppiumServerUtilities.stopAppiumServer(appiumServerService);
        }
    }

    public static void executeOnAvailableResource() {
        try {
            consolidatedTagsToExecute = "";
            List<String> resourcesConnectedToThisPC;
            resourcesConnectedToThisPC = Utilities.getConnectedDevices();
            if (applicationType.contains(Constants.DESKTOP_WEB)) {
                addDesktopBrowser(resourcesConnectedToThisPC);
            }
            logger.info("Resource Connected to this PC:" + resourcesConnectedToThisPC.toString());
            if (resourcesConnectedToThisPC.isEmpty()) {
                logger.error("There is no devices connected with this system..");
            } else {
                for (String resourceName : resourcesConnectedToThisPC) {
                    if (isResourceAlreadyInExecution(resourceName)) {
                        consolidatedTagsToExecute = Utilities.getAppendString(resourceName, consolidatedTagsToExecute, "|");
                        logger.info("Following resource is Already in Execution");
                        CSVUtilities.printResourceName(resourceName);
                        logger.info("If Not,Kindly remove the resource '" + devicesAllottedForExecutionFile + "' from Lock file in report dir.");
                    } else {
                        validateAndExecuteOnResource(resourceName);
                    }
                }
            }
        } catch (Exception exception) {
            logger.error(Constants.EXCEPTION_ON + Utilities.getCallerMethodName() + " :", exception);
        }

    }

    public static void validateAndExecuteOnResource(String resourceName) {
        try {
            resourceIdentifiedForExecution = resourceName;
            Map<String, String> deviceInfo = CSVUtilities.getResourceInfo(resourceName);
            if (deviceInfo == null) {
                String errorMsg = "Connected Resources(" + resourceName + ") details are not available in " + DEVICE_DETAILS_CSV + "file";
                logger.error(errorMsg);
                throw new RuntimeException(errorMsg);
            } else {
                config.putAll(deviceInfo);
                FrameworkData.getConfig().get(PropertyConstants.DEVICE_NAME);
                if (resourcesInCoverage.contains(config.getProperty(PropertyConstants.DEVICE_NAME).toLowerCase())) {
                    logger.info("Execution Planned to Start in " + config.getProperty(PropertyConstants.DEVICE_NAME));
                    String tagsToExecute = gettingTags(resourceName);
                    if (tagsToExecute.equals("")) {
                        logger.info("All the scripts might be executed for '" + config.getProperty(PropertyConstants.DEVICE_NAME) + "' or Users might not available Kindly check the report Excel and ApplicationSpecificStatus.tmp file.");
                    } else {
                        executOnResource(resourceName, deviceInfo, tagsToExecute);
                    }
                } else {
                    logger.error("Connected device '" + config.getProperty(PropertyConstants.DEVICE_NAME) + "' is not in " + config.getProperty(PropertyConstants.PROJECT_NAME) + " Coverage, Kindly update the report Excel if it is in coverage.");
                }
            }
            releaseResource();
        } catch (Exception exception) {
            logger.error(Constants.EXCEPTION_ON + Utilities.getCallerMethodName() + " :", exception);
        }
    }

    public static String gettingTags(String resourceName) {
        String tagsToExeucte = "";
        try {
            if (tagsChoosenByTestRunner) {
                tagsToExeucte = TagHandler.getScenarioIDFromTag(currentScenario);
            } else {
                tagsToExeucte = getTagsToExeucte(resourceName);
            }
        } catch (Exception exception) {
            logger.error(Constants.EXCEPTION_ON + Utilities.getCallerMethodName() + " :", exception);
        }
        return tagsToExeucte;
    }


    private static void addDesktopBrowser(List<String> resourcesConnectedToThisPC) {
        String browser = config.getProperty(PropertyConstants.DESKTOP_BROWSER_NAME, "");
        if (browser.contains(",")) {
            String[] browsers = browser.split(",");
            for (String currentBrowser : browsers) {
                resourcesConnectedToThisPC.add(currentBrowser);
            }
        } else {
            resourcesConnectedToThisPC.add(browser);
        }
    }

    private static void createDeviceLockingFile() {
        if (devicesAllottedForExecutionFile.isEmpty()) {
            String reportDir = config.getProperty(PropertyConstants.REPORT_FILE);
            reportDir = FileUtils.getParrentFolderPath(reportDir);
            if (reportDir.isEmpty()) {
                logger.error("Report file path is not configured in '" + PropertyConstants.REPORT_FILE + "'");
            } else {
                devicesAllottedForExecutionFile = reportDir + File.separator + "DevicesAllottedForExecution.tmp";
                try {
                    FileUtils.createFileIfNotExists(devicesAllottedForExecutionFile);
                } catch (Exception exception) {
                    logger.error(Constants.EXCEPTION_ON + Utilities.getCallerMethodName() + " :", exception);
                }
            }
        }
    }

    private static String getTagsToExeucte(String resourceName) {
        String tagsToExecute = "";
        try {
            if (tagsGivenByUser.equals("") && !isResourceAlreadyInExecution(resourceIdentifiedForExecution)) {
                FileUtils.waitAndLockTheFile(devicesAllottedForExecutionFile, Constants.TO_ADD_DEVICE + config.getProperty(PropertyConstants.DEVICE_NAME), false);
                if (isResourceAlreadyInExecution(resourceIdentifiedForExecution)) {
                    FileUtils.releaseFileLock(devicesAllottedForExecutionFile, Constants.TO_ADD_DEVICE + config.getProperty(PropertyConstants.DEVICE_NAME));
                    logger.info("The device '" + config.getProperty(PropertyConstants.DEVICE_NAME) + "' is already in Execution state");
                    resourceIdentifiedForExecution = "";
                } else {
                    tagsToExecute = TagHandler.getTagsToExecute(resourceName, config);
                    FileUtils.releaseFileLock(devicesAllottedForExecutionFile, Constants.TO_ADD_DEVICE + config.getProperty(PropertyConstants.DEVICE_NAME));
                }
            } else {
                consolidatedTagsToExecute = tagsGivenByUser;
                tagsToExecute = tagsGivenByUser;
            }
        } catch (Exception exception) {
            logger.error(Constants.EXCEPTION_ON + Utilities.getCallerMethodName() + " :", exception);
        }
        return tagsToExecute;
    }


    private static void resetTagsGivenByUser() {
        if (!Objects.equals(tagsGivenByUser, "")) {
            consolidatedTagsToExecute = "";
        }
    }

    private static void executOnResource(String resourceID, Map<String, String> deviceInfo, String tagsToExecute) {
        boolean errorOccured = false;
        try {
            setApplicationTypeToExecute(resourceID);
            if (applicationTypeToExecute != ApplicationType.DESKTOPWEB) {
                // AppiumServerUtilities.startAppiumServer();
            }
            logger.info("List of Tags to execute: " + tagsToExecute);
            reportFolderName = reportFolderName + config.getProperty(PropertyConstants.PROJECT_NAME) + "_" + deviceInfo.get(PropertyConstants.DEVICE_SHORT_NAME) + "_" + Utilities.getDateAndTimeForFile();
            config.put(PropertyConstants.DEVICE_UDID, resourceID);
            config.put(PropertyConstants.REPORT_FOLDER_PATH, PROJECT_PATH + reportFolderName);

            if (!manualTestRunner) {
                String _reportFolderName = REPORTS_PATH+ File.separator+config.getProperty(PropertyConstants.PROJECT_NAME) + "_" + deviceInfo.get(PropertyConstants.DEVICE_SHORT_NAME) + "_" + Utilities.getDateAndTimeForFile();;
                 if(_reportFolderName.length()!=reportFolderName.length()){
                     System.out.println("In Infinite Loop Skip Execution " );
                     System.exit(1);
                 }else{
                     FrameworkBase.beforeTestSuite();
                     errorOccured = RunnerClassGenerator.generateRunnerClass(getFeatureFilePath(), tagsToExecute, reportFolderName);
                 }
                if(errorOccured){
                    throw new Exception("Error occured in RunnerClass Generator");
                }
            }
        } catch (Exception exception) {
            logger.error(Constants.EXCEPTION_ON + Utilities.getCallerMethodName() + " :", exception);
        }
    }

    private static void setApplicationTypeToExecute(String resourceID) {
        if (Browsers.isBrowser(resourceID)) {
            applicationTypeToExecute = ApplicationType.DESKTOPWEB;
        } else if (applicationType.toLowerCase().contains(Constants.MOBILE_WEB.toLowerCase())) {
            applicationTypeToExecute = ApplicationType.MOBILEWEB;
        } else {
            applicationTypeToExecute = ApplicationTypes.getApplicationType(applicationType);
        }
    }

    private static void setFeaturePath() {
        String featurePath = config.getProperty(PropertyConstants.FEATURE_PATH);
        if (featurePath.isEmpty()) {
            setFeatureFilePath("Features/" + projectName);

        } else {
            setFeatureFilePath(featurePath);
        }
    }

    private static boolean getBooleanValue(String key) {
        boolean booleanStatus = false;
        checkPropertyContainsValue(key, IS_MANDATORY);
        if (!isIssueFound) {
            booleanStatus = PropertyUtils.getBooleanStatus(config, key);
        }
        return booleanStatus;
    }


    private static void startApiTest() {
        logger.info("yet to create API test");
        apiTestUrl = config.getProperty(API_TEST_URL);
        try {
            reportFolderName = reportFolderName + config.getProperty(PropertyConstants.PROJECT_NAME) + "_" + Utilities.getDateAndTimeForFile();
            FrameworkBase.beforeTestSuite();
           // RunnerClassGenerator.generateRunnerClass("Features/Demo_User_Actions --temp", "", reportFolderName);
        } catch (Exception exception) {
            logger.error(exception);
        }
    }

    private static void validateAndRemoveDeviceLockfile() {
        boolean fileExists;
        String reportDir = FileUtils.getParrentFolderPath(reportDirPath);
        String fileName=reportDir+"\\DevicesAllottedForExecution";
        String[] fileNames={fileName+".tmp" , fileName+".lck"};
        for(String lockfile:fileNames){
            fileExists=FileUtils.isFileExists(lockfile);
            if(fileExists) {
                new File(lockfile).delete();
                logger.info("Delete Device locking files" + lockfile);
            }
        }
    }

    public static String getFeatureFilePath() {
        return featureFilePath;
    }

    public static void setFeatureFilePath(String featurefilepath) {
        featureFilePath = featurefilepath;
    }

    public String getReportFolderName() {
        return reportFolderName;
    }

    public static void setReportFolderName(String reportfoldername) {
        reportFolderName = reportfoldername;
    }
}
