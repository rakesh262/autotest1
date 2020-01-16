package sqs.framework;

import cucumber.api.Scenario;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import sqs.base.SQSDriver;
import sqs.core.actions.CommonActions;
import sqs.core.actions.api.APIRequestActions;
import sqs.core.actions.driver.utils.AppiumServerUtilities;
import sqs.core.actions.mobile.MobileAndroidActions;
import sqs.core.actions.mobile.MobileAndroidWebActions;
import sqs.core.actions.mobile.MobileIOSActions;
import sqs.core.actions.mobile.MobileIOSWebActions;
import sqs.core.constants.ApplicationTypes.ApplicationType;
import sqs.core.constants.Browsers.Browser;
import sqs.core.constants.Constants;
import sqs.core.constants.PropertyConstants;
import sqs.core.reports.ExcelReporter;
import sqs.core.reports.ExtentReporter;
import sqs.core.utils.*;
import sqs.pageobjects.PageFactory;


import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import static sqs.core.webdriver.DriverCapabilities.setCapabilitiesDesktop;
import static sqs.core.webdriver.DriverCapabilities.setCapabilitiesMobileDevice;
import static sqs.core.utils.TestDataHandler.getData;

/**
 * This class contains generic functionalities like
 * setup/teardown test environment
 * @author  : Automation Tester (SQS)
 * @since  : 20 July 2015 @Modified Date:
 */
public class FrameworkBase extends FrameworkData {
    public static final Logger logger = Logger.getLogger(FrameworkBase.class);

    public static void beforeTestSuite()  {
        LoggerUtils.reConfigureLogPath();
        if (!config.getProperty("application.type").equalsIgnoreCase("api"))//for api not load page objects{
        {
            loadPageFactory();
        }
        loadTestData();
	    setActionClass();
        PropertyUtils.printAllProperty(config);
        if (applicationTypeToExecute == ApplicationType.DESKTOPWEB) {
            FrameworkData.setCapabilities(setCapabilitiesDesktop());
        } else if(config.getProperty("application.type").equalsIgnoreCase("api")){
            logger.info("Initialize api testing");
        }else {
            FrameworkData.setCapabilities(setCapabilitiesMobileDevice());
        }
    }



    public static  void afterTestSuite()  {
        if (applicationTypeToExecute == ApplicationType.MOBILEWEB || applicationTypeToExecute == ApplicationType.NATIVEAPP || applicationTypeToExecute == ApplicationType.HYBRIDAPP) {
            Utilities.playBeepSound();
        }
        if ((applicationTypeToExecute != ApplicationType.OTHERS)) {
  	        ExtentReporter.generateCustomeReport();
	     // generateAllureReport();
        }else if(applicationTypeToExecute == ApplicationType.OTHERS) {
        	APIRequestActions.resetBaseURI();
        }
    }

    private static void generateAllureReport() {
        try {
            Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"allure serve allure-results"); //For Allure Report run through Maven Test
        } catch (IOException e) {
            logger.error(e);
            try {
                throw e;
            } catch (IOException e1) {
                logger.error(e);
            }
        }

    }


    protected static void beforeScenario(Scenario scenario) {
        currentScenario = scenario;
        scenarioName = currentScenario.getName();
        List<String> tagNames = (List<String>) currentScenario.getSourceTagNames();
        Iterator<String> iterator = tagNames.iterator();
        boolean dataTagProvided=false;
        while (iterator.hasNext() && dataTagProvided==false) {
            String value = iterator.next();
            if (value.contains("@data") || value.contains("@Data")) {
                String tag = value.substring(6);
                scenarioTestDataTag=tag;
                dataTagProvided=true;
                logger.debug("Given Scenario Data Tag " +scenarioTestDataTag);
                break;
            }
        }
        if (applicationTypeToExecute == ApplicationType.DESKTOPWEB) {
            logger.debug(TagHandler.getScenarioIDFromTag(scenario) + " scenario Started in " + currentBrowser.getName());
        } else {
            try {
                if (config.size()> 0) {
                    logger.debug(TagHandler.getScenarioIDFromTag(scenario) + " scenario Started in " + config.getProperty(PropertyConstants.DEVICE_NAME));
                } else {
                    SQSDriver.loadAllValues();
                }
            } catch (Exception e) {
                e.getMessage();
            }
        }
        if (!apiRunFlag)
            initiateDriver();
        else
            APIRequestActions.setBaseURI();
    }


    protected  void afterScenario(Scenario scenario)   {
        try {
            action.quiteDriver();
            if (scenario.isFailed()) {
                action.takeScreenshotForAssertion();
            }
            if ((!updateStatusInExcel)&&(!apiRunFlag)) {
                ExcelReporter.updateStatusInExcelFile(scenario);
            }
        }catch (Exception exception){
            logger.error(Constants.EXCEPTION_ON+Utilities.getCallerMethodName()+" :",exception);
        }
    }

    protected static void setActionClass()   {
        try{
        String platform = config.getProperty(PropertyConstants.DEVICE_OS);
        if (applicationTypeToExecute == ApplicationType.NATIVEAPP || applicationTypeToExecute == ApplicationType.HYBRIDAPP) {
            if (platform.toLowerCase().contains(Constants.ANDROID.toLowerCase())) {
                action = new MobileAndroidActions();
            } else if (platform.toLowerCase().contains(Constants.IOS.toLowerCase())) {
                action = new MobileIOSActions();
            }
        } else if (applicationTypeToExecute == ApplicationType.MOBILEWEB) {
            if (platform.toLowerCase().contains(Constants.ANDROID.toLowerCase())) {
                action = new MobileAndroidWebActions();
            } else if (platform.toLowerCase().contains(Constants.IOS.toLowerCase())) {
                action = new MobileIOSWebActions();
            }
        } else {
            action = new CommonActions();
        }
        logger.debug("Action object Created with Class :" + action.toString());
        }catch (Exception exception){
            logger.error(Constants.EXCEPTION_ON+Utilities.getCallerMethodName()+" :",exception);
        }
    }


    public  static void loadPageFactory() {
        PageFactory screen = new PageFactory();
        screen.init();
        ObjectRepository.loadPageObjects();
        setPageObjectsPath(FrameworkData.config.getProperty(PropertyConstants.PAGE_OBJECTS_PATH));

    }

    private static void loadTestData() {
        TestDataHandler.loadTestData();
    }

    @SuppressWarnings("unused")
    private void loadPropertiesFile()  {
        try{
        String projectName = config.getProperty(PropertyConstants.PROJECT_NAME);
        String udid = config.getProperty(PropertyConstants.DEVICE_UDID);
        Properties projectConfig = PropertyUtils.loadProperties(PROJECT_PATH + "Config/" + projectName + ".properties");
        CSVUtilities.loadDeviceInfoInProperties(udid, config);
        config.putAll(projectConfig);
        }catch (Exception exception){
            logger.error(Constants.EXCEPTION_ON+Utilities.getCallerMethodName()+" :",exception);
        }
    }

    private static void initiateDriver() {
        logger.debug("Capabilities :" + capabilities);
        try {
            if (getApplicationTypeToExecute() == ApplicationType.DESKTOPWEB) {
                initiateWebDriver();
            } else {
                initiateAppiumDriver();
            }
        } catch (WebDriverException e) {
            handleSartDriver(e);
        }
        action.setImplicitWait(implicitWaitTime);
        Utilities.waitFor(1000);
    }

     static void initiateWebDriver() {
        if (currentBrowser == Browser.CHROME) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-notifications");
            driver = new ChromeDriver(options);
            driver.manage().window().maximize();
        } else if (currentBrowser == Browser.FIREFOX) {
            driver = new FirefoxDriver();
        } else if (currentBrowser == Browser.SAFARI) {
            driver = new SafariDriver();
            logger.debug("Maximizing browser");
            driver.manage().window().maximize();
        } else if (currentBrowser == Browser.EDGE) {
            driver = new EdgeDriver();
            logger.debug("Maximizing browser");
            driver.manage().window().maximize();
        }
    }


     static void initiateAppiumDriver(){
        AppiumServerUtilities.startAppiumServer();
            logger.debug(config.getProperty(PropertyConstants.DEVICE_OS));
            try {
                if (config.getProperty(PropertyConstants.DEVICE_OS).equalsIgnoreCase(Constants.ANDROID)) {
                    Thread.yield();
                    Utilities.waitFor(1000, "Start Android webdriver");
                    driver = new AndroidDriver<>(appiumServerService.getUrl(),capabilities);

                } else if (config.getProperty(PropertyConstants.DEVICE_OS).equalsIgnoreCase(Constants.IOS)) {
                    driver = new IOSDriver<>(appiumServerService.getUrl(), capabilities);
                }
                if (driver != null) {
                    mapDriverInstance();
                }
            }catch (Exception e){
                throw new WebDriverException(e);

            }
    }

    private static void mapDriverInstance() {
        action.switchToNative();

        setDeviceHeight(driver.manage().window().getSize().getHeight());
        setDeviceWidth(driver.manage().window().getSize().getWidth());
        if (applicationTypeToExecute == ApplicationType.MOBILEWEB) {
            action.switchToWebView();
        }

    }

    private static void handleSartDriver(WebDriverException e) {
        if (e.getMessage().contains("unknownHostException")) {
            unknownHostException();
        } else if (e.getMessage().contains("httpHostConnectException")) {
            httpHostConnectException();
        } else {
            try {
                throw new DriverRunTimeExecption(e);
            } catch (DriverRunTimeExecption driverRunTimeExecption) {
                logger.error(driverRunTimeExecption);
            }
        }
    }

   static void unknownHostException()   {
        logger.error("Please check, You have updated Appium server IP in Main.properties(" + PropertyConstants.APPIUM_SERVER_IP + ")");
       try {
           throw new UnknownHostException("Please check, You have updated Appium server IP in Main.properties(" + PropertyConstants.APPIUM_SERVER_IP + ")");
       } catch (UnknownHostException e) {
           logger.error(e);
       }
   }

    static void httpHostConnectException() {
       logger.error("Please check, Appium server is Up and running. in '" + config.get(PropertyConstants.APPIUM_SERVER_IP) + "' ip and 4723 port");
    }


    private static class DriverRunTimeExecption extends Throwable {
        public DriverRunTimeExecption(WebDriverException e ) {
            logger.error("Driver run time execption "+ e);

        }
    }
}
