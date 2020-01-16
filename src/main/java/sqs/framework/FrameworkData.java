package sqs.framework;

import cucumber.api.Scenario;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import sqs.core.actions.interfaces.FrameworkActionsInterface;
import sqs.core.constants.ApplicationTypes.ApplicationType;
import sqs.core.constants.Browsers.Browser;
import sqs.core.constants.Constants;
import sqs.core.utils.FileUtils;
import sqs.core.utils.Utilities;

import javax.json.JsonArray;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

public class FrameworkData {

    public static final FrameworkData frameworkData = new FrameworkData();
    public static final String PROJECT_PATH = System.getProperty("user.dir") + File.separator;
    public static final String DEVICE_DETAILS_CSV = PROJECT_PATH + "Config/Devices.csv";
    protected static boolean isWindows;
    protected static Properties config = new Properties();
    protected static Map<String, String> runtimeData = new HashMap<>();
    protected static int implicitWaitTime = 10;
    protected static int pageLoadWaitTime = 360;
    protected static String projectName = "";
    protected static int deviceHeight = 0;
    protected static int deviceWidth = 0;
    protected static boolean tagsChoosenByTestRunner = true;
    protected static boolean manualTestRunner = true;
    protected static boolean updateStatusInExcel = true;
    protected static WebDriver driver = null;
    protected static Process process;
    protected static Process webkitprocess;
    protected static WebDriverWait webDriverWait;
    protected static FrameworkActionsInterface action;
    protected static DesiredCapabilities capabilities;
    protected static AppiumDriverLocalService appiumServerService;
    protected static Scenario currentScenario;
    protected static boolean takeScreenshotForAssetionStepsAlone = true;
    protected static Map<String, String> testData = new HashMap<>();
    protected static String devicesAllottedForExecutionFile = "";
    protected static String consolidatedTagsToExecute = "";
    protected static String testSuiteIDForCurrentJVM = "";
    protected static String applicationType = "";
    protected static ApplicationType applicationTypeToExecute = ApplicationType.OTHERS;
    protected static String resourceIdentifiedForExecution = "";
    protected static Browser currentBrowser = null;
    protected static Map<String, JsonArray> pageObjects = new HashMap<>();
    protected static String mainWindowHandle;
    protected static String workingWindowHandle;
    protected static int applicationCount;
    protected static boolean apiRunFlag;
    protected static String apiTestUrl;
    protected static String scenarioName;
    protected static String extentReportPath;
    protected static String pageObjectsPath;
    static Path testResourceDirectory = Paths.get("src", "test", "resources");
    public static final String TEST_RESORUCES_PATH = testResourceDirectory.toAbsolutePath().toString() + File.separator;
    public static final String TEST_DATA_FOLDER_PATH = TEST_RESORUCES_PATH + "/TestData/";
    static Path mainResourceDirectory = Paths.get("src", "main", "resources");
    public static final String MAIN_RESOURCES_PATH = mainResourceDirectory.toAbsolutePath().toString() + File.separator;
    private static Logger logger = Logger.getLogger(FrameworkData.class);
    protected String actionPerformed = "click";
    protected PageFactory screen;
    protected static String scenarioTestDataTag="";

    protected static String reportDirPath =PROJECT_PATH+Constants.REPORTS_PATH;

    public static String getReportDirPath() {
        return reportDirPath;
    }

    public static void setReportDirPath(String reportDirPath) {
        FrameworkData.reportDirPath = reportDirPath;
    }

    public static String getScenarioTestDataTag() {
        return scenarioTestDataTag;
    }

    public static void setScenarioTestDataTag(String scenariotestDataTag) {
        scenarioTestDataTag = scenariotestDataTag;
    }

    public static boolean isTagsChoosenByTestRunner() {
        return tagsChoosenByTestRunner;
    }

    public static void setTagsChoosenByTestRunner(boolean tagsChoosenByTestRunnerr) {
        tagsChoosenByTestRunner = tagsChoosenByTestRunnerr;
    }

    public static boolean isUpdateStatusInExcel() {
        return updateStatusInExcel;
    }

    public static void setUpdateStatusInExcel(boolean updatestatusInExcell) {
        updateStatusInExcel = updatestatusInExcell;
    }

    public static int getImplicitWaitTime() {
        return implicitWaitTime;
    }

    public static void setImplicitWaitTime(int implicitwaitTime) {
        implicitWaitTime = implicitwaitTime;
    }

    public static int getPageLoadWaitTime() {
        return pageLoadWaitTime;
    }

    public static void setPageLoadWaitTime(int pageLoadWaittime) {

        pageLoadWaitTime = pageLoadWaittime;
    }

    public static String getProjectPath() {
        return PROJECT_PATH;
    }

    public static String getDeviceDetailsCsv() {
        return DEVICE_DETAILS_CSV;
    }

    public static String getProjectName() {
        return projectName;
    }

    public static void setProjectName(String projectname) {
        projectName = projectname;
    }

    public static int getDeviceHeight() {
        return deviceHeight;
    }

    public static void setDeviceHeight(int deviceHeightt) {
        deviceHeight = deviceHeightt;
    }

    public static int getDeviceWidth() {
        return deviceWidth;
    }

    public static void setDeviceWidth(int deviceWidthh) {
        deviceWidth = deviceWidthh;
    }

    public static int getApplicationCount() {
        return applicationCount;
    }

    public static void setApplicationCount(int applicationcount) {
        applicationCount = applicationcount;
    }

    public static boolean isWindows() {
        isWindows = Utilities.isWindows();
        return isWindows;
    }

    public static void setWindows(boolean windows) {
        isWindows = windows;
    }

    public static Properties getConfig() {
        return config;
    }

    public static void setConfig(Properties configg) {
        config = configg;
    }

    public static Map<String, String> getRuntimeData() {
        return runtimeData;
    }

    public static void setRuntimeData(Map<String, String> runtimeDataa) {
        runtimeData = runtimeDataa;
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static void setDriver(WebDriver driverr) {
        driver = driverr;
    }

    public static Process getProcess() {
        return process;
    }

    public static void setProcess(Process proccess) {
        process = proccess;
    }

    public static Process getWebkitprocess() {
        return webkitprocess;
    }

    public static void setWebkitprocess(Process webkitProcess) {
        webkitprocess = webkitProcess;
    }

    public static WebDriverWait getWebDriverWait() {
        return webDriverWait;
    }

    public static void setWebDriverWait(WebDriverWait webDriverwait) {
        webDriverWait = webDriverwait;
    }

    public static FrameworkActionsInterface getAction() {
        return action;
    }

    public static void setAction(FrameworkActionsInterface action1) {
        action = action1;
    }

    public static DesiredCapabilities getCapabilities() {
        return capabilities;
    }

    public static void setCapabilities(DesiredCapabilities capabillities) {
        capabilities = capabillities;
    }

    public static AppiumDriverLocalService getAppiumServerService() {
        return appiumServerService;
    }

    public static void setAppiumServerService(AppiumDriverLocalService appiumServerservice) {
        appiumServerService = appiumServerservice;
    }

    public static Scenario getCurrentScenario() {
        return currentScenario;
    }

    public static void setCurrentScenario(Scenario currentscenario) {
        currentScenario = currentscenario;
    }

    public static boolean isTakeScreenshotForAssetionStepsAlone() {
        return takeScreenshotForAssetionStepsAlone;
    }

    public static void setTakeScreenshotForAssetionStepsAlone(boolean takeScreenshotforAssetionStepsAlone) {
        takeScreenshotForAssetionStepsAlone = takeScreenshotforAssetionStepsAlone;
    }

    public static Map<String, String> getTestData() {
        return testData;
    }

    public static void setTestData(Map<String, String> testdata) {
        testData = testdata;
    }

    public static String getDevicesAllottedForExecutionFile() {
        return devicesAllottedForExecutionFile;
    }

    public static void setDevicesAllottedForExecutionFile(String devicesAllottedforExecutionFile) {
        devicesAllottedForExecutionFile = devicesAllottedforExecutionFile;
    }

    public static String getConsolidatedTagsToExecute() {
        return consolidatedTagsToExecute;
    }

    public static void setConsolidatedTagsToExecute(String consolidatedTagstoExecute) {
        consolidatedTagsToExecute = consolidatedTagstoExecute;
    }

    public static String getTestSuiteIDForCurrentJVM() {
        return testSuiteIDForCurrentJVM;
    }

    public static void setTestSuiteIDForCurrentJvm(String testSuiteIDforCurrentJVM) {
        testSuiteIDForCurrentJVM = testSuiteIDforCurrentJVM;
    }

    public static String getApplicationType() {
        return applicationType;
    }

    public static void setApplicationType(String applicationtype) {
        applicationType = applicationtype;
    }

    public static ApplicationType getApplicationTypeToExecute() {
        return applicationTypeToExecute;
    }

    public static void setApplicationTypeToExecute(ApplicationType applicationTypetoExecute) {
        applicationTypeToExecute = applicationTypetoExecute;
    }

    public static String getResourceIdentifiedForExecution() {
        return resourceIdentifiedForExecution;
    }

    public static void setResourceIdentifiedForExecution(String resourceIdentifiedForExecutionn) {
        resourceIdentifiedForExecution = resourceIdentifiedForExecutionn;
    }

    public static Browser getCurrentBrowser() {
        return currentBrowser;
    }

    public static void setCurrentBrowser(Browser currentbrowser) {
        currentBrowser = currentbrowser;
    }

    public static Map<String, JsonArray> getPageObjects() {
        return pageObjects;
    }

    public static void setPageObjects(Map<String, JsonArray> pageobjects) {
        pageObjects = pageobjects;
    }

    public static void setMainWindowHandler(String windowHandle) {
        mainWindowHandle = windowHandle;
        setWorkingWindowHandle(windowHandle);
    }

    public static String getWorkingWindowHandle() {
        return workingWindowHandle;
    }

    public static void setWorkingWindowHandle(String workingWindowhandle) {
        workingWindowHandle = workingWindowhandle;
    }

    public static boolean isApiRunFlag() {
        return apiRunFlag;
    }

    public static void setApiRunFlag(boolean apiRunflag) {
        apiRunFlag = apiRunflag;
    }

    public static String getApiTestUrl() {
        return apiTestUrl;
    }

    public static void setApiTestUrl(String apiTestURL) {
        apiTestUrl = apiTestURL;
    }

    public static String getScenarioName() {
        return scenarioName;
    }

    public static void setScenarioName(String scenarioname) {
        scenarioName = scenarioname;
    }

    public static String getExtentReportPath() {
        return extentReportPath;
    }

    public static void setExtentReportPath(String extentReportpath) {
        extentReportPath = extentReportpath;
    }

    public static void incrementApplicationCount() {
        applicationCount++;
    }

    public static String getTestDataFolderPath() {
        return TEST_DATA_FOLDER_PATH;
    }

    public void assignTestSuiteIDForCurrentJVM() {
        String randomSessionId;
        Random random;
        try {
            if (testSuiteIDForCurrentJVM.isEmpty()) {
                random = SecureRandom.getInstanceStrong();
                randomSessionId = "$" + random.nextInt(999999999) + "$";
                FileUtils.waitAndLockTheFile(devicesAllottedForExecutionFile, Constants.TO_ADD_TEST_SUITE_ID + "  " + randomSessionId, false);
                if (FileUtils.isFileContainsText(devicesAllottedForExecutionFile, randomSessionId)) {
                    FileUtils.releaseFileLock(devicesAllottedForExecutionFile, Constants.TO_ADD_TEST_SUITE_ID + "   " + randomSessionId);
                    assignTestSuiteIDForCurrentJVM();
                } else {
                    FileUtils.writeTextExistingFile(devicesAllottedForExecutionFile, "TestSuite Id  " + randomSessionId);
                    FileUtils.releaseFileLock(devicesAllottedForExecutionFile, Constants.TO_ADD_TEST_SUITE_ID + "  " + randomSessionId);
                    setTestSuiteIDForCurrentJvm(randomSessionId);
                    logger.debug("TestSuite Id :<<<<" + testSuiteIDForCurrentJVM + ">>>>");

                }
            }
        } catch (Exception exception) {
            logger.error(Constants.EXCEPTION_ON + Utilities.getCallerMethodName() + " :", exception);
        }
    }

    public String getActionPerformed() {
        return actionPerformed;
    }

    public void setActionPerformed(String actionperformed) {
        actionPerformed = actionperformed;
    }

    public String getPageObjectsPath() {
        return pageObjectsPath;
    }

    public static void setPageObjectsPath(String pageobjectspath) {
        pageObjectsPath = pageobjectspath;
    }

    public boolean isManualTestRunner() {
        return manualTestRunner;
    }

    public static void setManualTestRunner(boolean manualTestRunnerr) {
        manualTestRunner = manualTestRunnerr;
    }

}