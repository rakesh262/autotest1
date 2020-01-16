package sqs.core.actions.driver.utils;



import io.appium.java_client.service.local.*;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

import java.io.*;

import org.apache.log4j.Logger;
import org.openqa.selenium.net.UrlChecker;
import org.openqa.selenium.remote.DesiredCapabilities;


import sqs.core.constants.PropertyConstants;
import sqs.core.utils.FileUtils;

import sqs.framework.FrameworkData;

import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static sqs.core.constants.Constants.EXCEPTION_ON;
import static sqs.core.utils.Utilities.getCallerMethodName;

public class AppiumServerUtilities {
    public static final String APPIUM_URL_FILE_NAME = "AppiumServerURL.tmp";
    static final String APPIUM_PROPERTIES = FrameworkData.MAIN_RESOURCES_PATH + "appiumServer.properties";
    static Properties appiumServerProperties = new Properties();
    private static String ipAddress;
    private static Logger logger = Logger.getLogger(AppiumServerUtilities.class);

    private AppiumServerUtilities() {
        readAppiumProperties();
    }

    public static void startAppiumServer() {

        try {
            readAppiumProperties();
            DesiredCapabilities serverCapabilities = new DesiredCapabilities();
            AppiumServiceBuilder builder = new AppiumServiceBuilder().withCapabilities(serverCapabilities);
            builder.usingAnyFreePort();
            builder.withIPAddress(ipAddress);
            builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
            builder.withArgument(GeneralServerFlag.LOG_LEVEL, "error");
            FrameworkData.setAppiumServerService(builder.build());
            FrameworkData.getAppiumServerService().start();
            logger.debug("Appium server Started In URL: " + FrameworkData.getAppiumServerService().getUrl());

        } catch (InvalidServerInstanceException e) {
            if (e.getMessage().contains("There is no installed nodes")) {
                logger.error("Kindly install Appium using npm (Node Package manager) 'npm install appium' from command Prompt");
                try {
                    throw new Exception(e);
                } catch (Exception e1) {
                }
            }
        } catch (Exception e) {
            logger.error("Exception while starting Appium Server.", e);

        }


    }

    public static void readAppiumProperties() {
        try (InputStream inputStream = new FileInputStream(APPIUM_PROPERTIES)) {
            appiumServerProperties.load(inputStream);
            String property= PropertyConstants.APPIUM_SERVER_IP;
            ipAddress = appiumServerProperties.getProperty(property);
            FrameworkData.getConfig().put(property,ipAddress);
        } catch (Exception io) {
            logger.error(io);
            try {
                throw io;
            } catch (IOException e) {
                logger.error(e);
            }
        }

    }


    public static void stopAppiumServer(AppiumDriverLocalService appiumService) {
        try {
            if (appiumService != null) {
                appiumService.stop();
            }
        } catch (Exception exception) {
            logger.error(EXCEPTION_ON + getCallerMethodName() + " :", exception);
        }
    }

    @SuppressWarnings("unused")
    private static boolean isAppiumServerAlreadyRunning(String appiumServerURL) {

        boolean isAppiumServerRunning = false;
        try {
            final URL appiumURL = new URL(appiumServerURL + "/sessions");
            new UrlChecker().waitUntilAvailable(2000, TimeUnit.MILLISECONDS, appiumURL);
            logger.debug("Appium Server is Already Running in URL:" + appiumServerURL);
            isAppiumServerRunning = true;
        } catch (Exception exception) {
            logger.error(EXCEPTION_ON + getCallerMethodName() + " :", exception);
        }
        return isAppiumServerRunning;
    }

    public static String getAppiumURL() {
        String appiumURL = "";
        try {
            appiumURL = FileUtils.getTextFromFile(APPIUM_URL_FILE_NAME);
        } catch (Exception exception) {
            logger.error(EXCEPTION_ON + getCallerMethodName() + " :", exception);
        }
        return appiumURL;
    }
}
