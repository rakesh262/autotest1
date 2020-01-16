package sqs.core.utils;

import cucumber.runtime.CucumberException;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import sqs.core.actions.driver.utils.SeleniumUtils;
import sqs.core.constants.Constants;
import sqs.core.constants.PropertyConstants;
import sqs.framework.FrameworkData;

import javax.json.*;
import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @ScriptName : Utilities
 * @Description : This class contains HashMap Memory setter and getter
 * @Author : Srirangan
 * @Creation Date : 23 May 2016 @Modified Date:
 */

public class ObjectRepository extends JsonUtils {
    private static Logger logger = Logger.getLogger(ObjectRepository.class);

    public static By get(String page, String name) {
        boolean itemFound = false;
        By by = null;
        for (Map.Entry<String, JsonArray> entry : pageObjects.entrySet()) {
            if (!itemFound) {
                String data = entry.getKey();
                if (data.equalsIgnoreCase(page)) {
                    JsonArray array = entry.getValue();
                    by = getByLocatorFromJsonArray(array, name, by);
                    if (by != null) itemFound = true;
                }
            }
        }
        return by;
    }

    public static By getByLocatorFromJsonArray(JsonArray array, String name, By by) {
            JsonObject obj = array.getJsonObject(0);
            for (Map.Entry<String, JsonValue> item : obj.entrySet()) {
                if (item.getKey().equalsIgnoreCase(name)) {
                    by = getByLocatorFromJsonValue(item.getValue());
                    break;
                }
            }
        return by;
    }

    /***
     *
     * Gets the object locator details from the repository based on given @param
     * @param name: Field name
     * @return By locator for the field
     */

    public static By get(String name) {
        String pageName = getPageClassName();
        boolean elementFound = false;
        By by = null;
        if (pageName != null) {
            by = get(pageName, name);
        } else {
            for (Map.Entry<String, JsonArray> entry : pageObjects.entrySet()) {
                JsonArray array = entry.getValue();
                by = getByLocatorFromJsonArray(array, name, by);
                if (by != null) break;
            }
        }
        if (by != null) {
            elementFound = true;
        }

        if (!elementFound) {
            throw new CucumberException("Given Object '" + name + "' is not found in Object repository.");
        }
        return by;
    }

    private static String getPageClassName() {
        String pageName = null;
        StackTraceElement[] classNames = Thread.currentThread().getStackTrace();
        for (StackTraceElement names : classNames) {
            String name = names.getClassName();
            if (name.endsWith("Page")) {
                pageName = name.substring(name.lastIndexOf('.') + 1, name.length());
                break;
            }

        }
        return pageName;


    }


    private static By getByLocatorFromJsonValue(JsonValue value) {
        String param = value.toString().substring(1, value.toString().length() - 1);
        String locatorType = param.split(";")[0];
        String locatorValue = param.split(";")[1];
        return SeleniumUtils.getLocator(locatorType, locatorValue);
    }


    public static void loadPageObjects() {

        String orFile = getORFileName();
        Path folderPath = Paths.get(orFile);
        List<String> objectRepositoryFolderFiles = new ArrayList<>();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(folderPath)) {
            for (Path path : directoryStream) {
                if (FilenameUtils.getExtension(String.valueOf(path)).contains("json")) {
                    objectRepositoryFolderFiles.add(path.toString());
                }
            }
        } catch (IOException exception) {
            logger.error("Exception on load page objects ", exception);
            try {
                throw exception;
            } catch (IOException e) {
                logger.error("Exception on load page objects ", exception);
            }
        }
        JsonArray dummyArray=null;

        for (String files : objectRepositoryFolderFiles) {
            JsonArray array = null;
            File currentFile = new File(files);
            try (InputStream is = new FileInputStream(currentFile)) {
                try (JsonReader reader = Json.createReader(is)) {
                    JsonObject obj = reader.readObject();
                    String  appType=FrameworkData.getApplicationType().toLowerCase();
                    if(appType.contains("app")) {
                        if (config.getProperty(PropertyConstants.DEVICE_OS).equalsIgnoreCase(Constants.IOS)) {
                            array = obj.getJsonArray("ios");
                        } else if (config.getProperty(PropertyConstants.DEVICE_OS).equalsIgnoreCase(Constants.ANDROID)) {
                            array = obj.getJsonArray("android");
                        }
                    } else {
                        array = obj.getJsonArray("Web");
                    }
                }
            } catch (Exception exception) {
                logger.error("Exception on load page objects", exception);
            }
            pageObjects.put(FilenameUtils.removeExtension(currentFile.getName()), array);
           dummyArray=array;

        }
        pageObjects.put("CommonPage",dummyArray);

    }


    public static void add(String key, String locatorType, String locatorValue) {
        boolean itemInserted = false;
        if (locatorType.equalsIgnoreCase("xpath")) {
            locatorType = "Xpath";
        }
        JsonArray newArray = null;
        String strValue = locatorType + ";" + locatorValue;
        String pageName = getPageClassName();
        if (pageName==null){
            pageName="CommonPage";
        }
        for (Map.Entry<String, JsonArray> entry : pageObjects.entrySet()) {
            if (!itemInserted) {
                String data = entry.getKey();
                if (data.equalsIgnoreCase(pageName)) {
                    JsonArray array = entry.getValue();
                    JsonObject obj = array.getJsonObject(0);
                    obj = jsonObjectToBuilder(obj).add(key, strValue).build();
                    JsonArrayBuilder builder = Json.createArrayBuilder();
                    builder.add(obj);
                    array = builder.build();
                    newArray = array;
                    itemInserted = true;
                    break;
                }
            }
        }
        if (newArray != null) {
            pageObjects.put(pageName, newArray);
        }
    }

    private static String getORFileName() {
        String orFile = "";
        orFile = MAIN_RESOURCES_PATH + FrameworkData.config.getProperty(PropertyConstants.PAGE_OBJECTS_PATH);
        return orFile;
    }


    public static By getLocator(JsonArray array, String locatorName) {
        By locator = null;
        String param = null;
        String locatorType;
        String locatorValue;
        try {
            for (JsonValue value : array) {
                if (value.toString().contains(locatorName)) {
                    param = array.getJsonObject(0).getString(locatorName);
                }
                if (param != null) {
                    locatorType = param.split(";")[0];
                    locatorValue = param.split(";")[1];
                    locator = SeleniumUtils.getLocator(locatorType, locatorValue);
                }
            }
        } catch (Exception exception) {
            logger.error(exception);
            throw exception;
        }
        return locator;
    }


}
