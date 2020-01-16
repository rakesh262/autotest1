package sqs.core.webdriver;

import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import sqs.core.constants.ApplicationTypes;
import sqs.core.constants.Browsers;
import sqs.core.constants.Constants;
import sqs.core.constants.PropertyConstants;
import sqs.core.utils.CSVUtilities;
import sqs.core.utils.Utilities;
import sqs.framework.FrameworkData;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

public class DriverCapabilities {
   static Logger logger = Logger.getLogger(DriverCapabilities.class);

    private DriverCapabilities(){

    }

    private static DesiredCapabilities setCapabilitiesCommon() {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, FrameworkData.getConfig().getProperty(PropertyConstants.DEVICE_OS));
        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, FrameworkData.getConfig().get(PropertyConstants.DEVICE_OS_VERSION));
        desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, FrameworkData.getConfig().get(PropertyConstants.DEVICE_NAME));
        desiredCapabilities.setCapability(MobileCapabilityType.UDID, FrameworkData.getConfig().get(PropertyConstants.DEVICE_UDID));
        desiredCapabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 480);
        return desiredCapabilities;
    }

    public static DesiredCapabilities setCapabilitiesDesktop()  {
        DesiredCapabilities desiredCapabilities=null;
        if (FrameworkData.getApplicationTypeToExecute() == ApplicationTypes.ApplicationType.DESKTOPWEB) {
            Browsers.Browser browser = Browsers.getBrowser(FrameworkData.getResourceIdentifiedForExecution());
            switch (browser) {
                case FIREFOX:
                	WebDriverManager.firefoxdriver().setup();
                    desiredCapabilities =  DesiredCapabilities.firefox();
                    break;
                case CHROME:
                    desiredCapabilities = DesiredCapabilities.chrome();
                    if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                    	WebDriverManager.getInstance(ChromeDriver.class).setup();
                    } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                    	WebDriverManager.getInstance(ChromeDriver.class).setup();
                        desiredCapabilities.setCapability("chrome.binary", "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome");
                    } else {
                    	WebDriverManager.getInstance(ChromeDriver.class).setup();
                    }
                    break;
                case IE:
                	WebDriverManager.getInstance(InternetExplorerDriver.class).setup();
                    desiredCapabilities = DesiredCapabilities.internetExplorer();
                    break;
                case EDGE:
                	WebDriverManager.getInstance(EdgeDriver.class).setup();
                    desiredCapabilities = DesiredCapabilities.edge();
                    break;
                case SAFARI:
                	WebDriverManager.getInstance(SafariDriver.class).setup();
                    desiredCapabilities = DesiredCapabilities.safari();
                    break;
                default:
                    logger.error("Given browser '" + System.getProperty(PropertyConstants.DESKTOP_BROWSER_NAME) + "' is not valid Please provide proper browser - Chrome, Firefox, Safari or Internet Explorer");
                    try {
                        throw new UnknownHostException("Given browser '" + System.getProperty(PropertyConstants.DESKTOP_BROWSER_NAME) + "' is not valid Please provide proper browser - Chrome, Firefox, Safari or Internet Explorer");
                    } catch (Exception e) {
                        logger.error(e);
                    }
            }

        }
        return desiredCapabilities;
    }

    private static DesiredCapabilities setCapabilitiesMobileDeviceOS(DesiredCapabilities capabilities) {
        if (FrameworkData.getConfig().getProperty(PropertyConstants.DEVICE_OS).equalsIgnoreCase(Constants.ANDROID)) {
            setCapabilitiesAndroid(capabilities);
        } else if (FrameworkData.getConfig().getProperty(PropertyConstants.DEVICE_OS).equalsIgnoreCase(Constants.IOS)) {
            setCapabilitiesIOS(capabilities);
        }
        return capabilities;
    }

    private static DesiredCapabilities setCapabilitiesAndroid(DesiredCapabilities capabilities) {
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "Appium");
        capabilities.setCapability(AndroidMobileCapabilityType.RESET_KEYBOARD, true);
        capabilities.setCapability(AndroidMobileCapabilityType.DEVICE_READY_TIMEOUT, 480000);
        return capabilities;

    }

    private static DesiredCapabilities setCapabilitiesIOS(DesiredCapabilities capabilities) {
        if (compareVersionNumbers(FrameworkData.getConfig().getProperty(PropertyConstants.DEVICE_OS_VERSION)) <= 9.35) {
            capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.APPIUM);
        } else {
            capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.IOS_XCUI_TEST);
            try {
                String deviceNumber = CSVUtilities.getDeviceNumberFromCSV(FrameworkData.getConfig().getProperty(PropertyConstants.DEVICE_UDID), FrameworkData.DEVICE_DETAILS_CSV);
                int port = 27700 + Utilities.getInteger(deviceNumber);
                capabilities.setCapability("webkitDebugProxyPort", port);
                logger.debug("Webkit Proxy started in port:" + port);
                port = 8100 + Utilities.getInteger(deviceNumber);
                capabilities.setCapability("wdaLocalPort", port);
                logger.debug("WebDriverAgent port:" + port);
            } catch (IOException exception) {
                logger.error(Constants.EXCEPTION_ON+Utilities.getCallerMethodName()+" :",exception);

            }
        }
        capabilities.setCapability(IOSMobileCapabilityType.SHOW_IOS_LOG, true);
        capabilities.setCapability(IOSMobileCapabilityType.LAUNCH_TIMEOUT, 48000);
        return capabilities;
    }

    public static DesiredCapabilities setCapabilitiesMobileDevice() {
        DesiredCapabilities capabilities=null;
        capabilities=setCapabilitiesCommon();
        setCapabilitiesMobileDeviceOS(capabilities);
        setCapabilitiesApplicationSpecific(capabilities);
        return capabilities;
    }

    private static DesiredCapabilities setCapabilitiesApplicationSpecific(DesiredCapabilities capabilities) {
        if (FrameworkData.getApplicationTypeToExecute() == ApplicationTypes.ApplicationType.NATIVEAPP) {
            setCapababilitiesNativeApp(capabilities);
        } else if (FrameworkData.getApplicationTypeToExecute() == ApplicationTypes.ApplicationType.HYBRIDAPP) {
            setCapababilitiesHybridApp(capabilities);
        } else {
            setCapababilitiesBrowserMobile(capabilities);
        }
        return capabilities;
    }

    private static DesiredCapabilities setCapababilitiesNativeApp(DesiredCapabilities capabilities) {
        if (FrameworkData.getConfig().getProperty(PropertyConstants.DEVICE_OS).equalsIgnoreCase(Constants.IOS)) {
            capabilities.setCapability(IOSMobileCapabilityType.AUTO_DISMISS_ALERTS, true);
            capabilities.setCapability(IOSMobileCapabilityType.BUNDLE_ID, FrameworkData.getConfig().get("test.appBundleID"));
        } else if (FrameworkData.getConfig().getProperty(PropertyConstants.DEVICE_OS).equalsIgnoreCase(Constants.ANDROID)) {
            logger.debug("Set Capabilities for Android Native app");
            capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, FrameworkData.getConfig().getProperty(PropertyConstants.APP_PACKAGE));
            capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, FrameworkData.getConfig().getProperty(PropertyConstants.APP_ACTIVITY));
            capabilities.setCapability(MobileCapabilityType.NO_RESET, FrameworkData.getConfig().getProperty(PropertyConstants.NO_RESET));
            String apkFilePath = FrameworkData.getConfig().getProperty(PropertyConstants.APK_FILE_PATH);
            if (!apkFilePath.isEmpty()) {
                File app = new File(apkFilePath);
                capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
            }

        }
        return capabilities;
    }

    private static void setCapababilitiesHybridApp(DesiredCapabilities capabilities) {
        if (FrameworkData.getConfig().getProperty(PropertyConstants.DEVICE_OS).equalsIgnoreCase(Constants.IOS)) {
            capabilities.setCapability("autoWebview", true);
        }
        setCapababilitiesNativeApp(capabilities);
    }

    private static void setCapababilitiesBrowserMobile(DesiredCapabilities capabilities) {
        if (FrameworkData.getConfig().getProperty(PropertyConstants.DEVICE_OS).equalsIgnoreCase(Constants.IOS)) {
            capabilities.setCapability(IOSMobileCapabilityType.SAFARI_INITIAL_URL, "about:blank");
            FrameworkData.setCurrentBrowser( Browsers.getBrowser(FrameworkData.getConfig().getProperty(PropertyConstants.IOS_BROWSER_NAME)));

            capabilities.setCapability(IOSMobileCapabilityType.BROWSER_NAME, FrameworkData.getCurrentBrowser().getName());
            capabilities.setCapability(IOSMobileCapabilityType.SAFARI_ALLOW_POPUPS, true);
            if (compareVersionNumbers(FrameworkData.getConfig().getProperty(PropertyConstants.DEVICE_OS_VERSION)) >= 9.35) {
                capabilities.setCapability(MobileCapabilityType.AUTO_WEBVIEW, true);
                capabilities.setCapability(IOSMobileCapabilityType.START_IWDP, true);
            }
        } else if (FrameworkData.getConfig().getProperty(PropertyConstants.DEVICE_OS).equalsIgnoreCase(Constants.ANDROID)) {

            FrameworkData.setCurrentBrowser(Browsers.getBrowser(FrameworkData.getConfig().getProperty(PropertyConstants.ANDROID_BROWSER_NAME)));
            capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, FrameworkData.getCurrentBrowser().getName());
        }

    }

    public static double compareVersionNumbers(String value) {

            if (value.contains(".") && ((value.indexOf('.')) != (value.lastIndexOf('.')))) {
                value = value.substring(0, value.indexOf('.')) + "." + value.substring(value.indexOf('.')).replace(".", "");
            }

        return Double.parseDouble(value);
    }


}
