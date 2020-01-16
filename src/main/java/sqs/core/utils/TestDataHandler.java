package sqs.core.utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import javax.json.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author : Srirangan
 * @Description : This class contains HashMap Memory setter and getter
 * @since: 23 May 2016
 */

public class TestDataHandler extends JsonUtils {
    static List<String> testDataFolderList = new ArrayList<>();
    static Map<String, JsonObject> testObjects = new HashMap<>();
    static String scenarioDataTag = "Payment";
    private static Logger logger = Logger.getLogger(TestDataHandler.class);

    /**
     * @author - Megala
     * @deprecated Sample JSON parser to extract array, object type data
     */
    @Deprecated
    private static void readSimpleJson() {
        String dataFile = ".\\src\\test\\resources\\TestData\\Payment.json";

        JsonReader reader = null;
        JsonObject obj = null;
        JsonArray array;
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        try (InputStream is = new FileInputStream(new File(dataFile))) {
            reader = Json.createReader(is);
            obj = reader.readObject();
            for (Map.Entry entry : obj.entrySet()) {
                Object key = entry.getKey();
                JsonObject jsonObject;
                String entryType = entry.getValue().getClass().toString();
                if (entryType.contains("JsonObjectBuilderImpl$JsonObjectImpl")) {
                    jsonObject = (JsonObject) entry.getValue();
                    testObjects.put(key.toString(), jsonObject);
                } else if (entryType.contains("JsonArrayBuilderImpl$JsonArrayImpl")) {
                    array = obj.getJsonArray(key.toString());
                    jsonObject = array.getJsonObject(0);
                    testObjects.put(key.toString(), jsonObject);
                } else if (entryType.contains("JsonStringImpl")) {
                    objectBuilder.add(key.toString(), entry.getValue().toString());
                }
            }
            JsonObject defultJsonObject = objectBuilder.build();
            testObjects.put("default", defultJsonObject);

        } catch (Exception exception) {
            logger.error("Exception", exception);
            try {
                throw exception;
            } catch (IOException e) {
                logger.error(e);
            }
        } finally {
            if (reader != null) reader.close();
        }
    }

    public static String getData(String key) {
        return getData(getScenarioTestDataTag(), key);
    }

    private static String getDataFromJsonObject(Map.Entry<String, JsonObject> entry, String key) {
        String data = "";
        for (Map.Entry<String, JsonValue> item : entry.getValue().entrySet()) {
            if (item.getKey().equalsIgnoreCase(key)) {
                data = item.getValue().toString();
                if (data.startsWith("\"") && data.endsWith("\"")) {
                    data = data.substring(3, data.length() - 3);
                }
                break;
            }
        }
        return data;

    }

    private static String getData(String scenarioDataTag, String key) {
           if (key.startsWith("$")) {
            key = key.substring(1);
        }
        String data = "";
        for (Map.Entry<String, JsonObject> entry : testObjects.entrySet()) {
            if (scenarioDataTag.length() != 0) {
                if (entry.getKey().equalsIgnoreCase(scenarioDataTag)) {
                    data = getDataFromJsonObject(entry, key);
                }
            } else {
                data = getDataFromJsonObject(entry, key);
            }
            if (data != "") break;
        }
        if (data == "") {
            data = key;
        }
        return data;
    }


    public static void loadTestData() {
        String dataFile = getTestDataFolderPath();
        loadTestDataFoldersList(dataFile);
        for (String currentDataFile : testDataFolderList) {
            if (FilenameUtils.getExtension(String.valueOf(currentDataFile)).contains("json")) {
                readJson(currentDataFile);
            }
        }
    }

    private static void readJson(String dataFile) {
        Map<String, JsonObject> jsonContent = new HashMap<>();
        JsonReader reader = null;
        JsonObject obj = null;
        JsonArray array;
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        File file = new File(dataFile);
        try (InputStream is = new FileInputStream(file)) {
            reader = Json.createReader(is);
            obj = reader.readObject();
            for (Map.Entry entry : obj.entrySet()) {
                Object key = entry.getKey();
                JsonObject jsonObject;
                String entryType = entry.getValue().getClass().toString();
                if (entryType.contains("JsonObjectBuilderImpl$JsonObjectImpl")) {
                    jsonObject = (JsonObject) entry.getValue();
                    jsonContent.put(key.toString(), jsonObject);
                } else if (entryType.contains("JsonArrayBuilderImpl$JsonArrayImpl")) {
                    array = obj.getJsonArray(key.toString());
                    jsonObject = array.getJsonObject(0);
                    jsonContent.put(key.toString(), jsonObject);
                } else if (entryType.contains("JsonStringImpl")) {
                    objectBuilder.add(key.toString(), entry.getValue().toString());
                }
            }
            JsonObject defultJsonObject = objectBuilder.build();
            jsonContent.put(FilenameUtils.removeExtension(file.getName()), defultJsonObject);
            testObjects.put(FilenameUtils.removeExtension(file.getName()), defultJsonObject);

        } catch (Exception exception) {
            logger.error("Exception", exception);
            try {
                throw exception;
            } catch (IOException e) {
                logger.error(e);
            }
        } finally {
            if (reader != null) reader.close();
        }
    }

    private static void loadTestDataFoldersList(String dataFile) {
        Path folderPath = Paths.get(dataFile);
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(folderPath)) {
            for (Path path : directoryStream) {
                if (FilenameUtils.getExtension(String.valueOf(path)).contains("json")) {
                    testDataFolderList.add(path.toString());
                }
            }
        } catch (IOException e) {
            logger.error("Exception on load Test Data ", e);
        }
    }


    public static void mainMethod() {
        loadTestData();
        scenarioDataTag = "";
        getData("AMyPassword");
        scenarioDataTag = "FundTransfer";
        getData("APaymentUser");
    }
}
