package sqs.core.utils;

import java.io.*;
import java.util.Map;

import javax.json.*;

import org.apache.log4j.Logger;

import sqs.framework.FrameworkData;

public class JsonUtils extends FrameworkData {

    private static Logger logger = Logger.getLogger(JsonUtils.class);

    public static String getObjectData(String dataName, String dataFileFullPath) throws IOException {
        String param = null;
        JsonReader reader = null;
        JsonObject obj = null;
        JsonArray array;
        try (InputStream is = new FileInputStream(new File(dataFileFullPath))) {
            reader = Json.createReader(is);
            obj = reader.readObject();
            array = obj.getJsonArray("TestData");
            for (JsonValue value : array) {
                if (value.toString().contains(dataName)) {
                    param = array.getJsonObject(0).getString(dataName);
                }
            }
        } catch (Exception exception) {
            logger.error("Given TestData '" + dataFileFullPath + "' is not found in TestData Path.", exception);
        } finally {
            if (reader != null) reader.close();
        }
        return param;
    }


    public static JsonObjectBuilder jsonObjectToBuilder(JsonObject jo) {
        JsonObjectBuilder job = Json.createObjectBuilder();
        for (Map.Entry<String, JsonValue> entry : jo.entrySet()) {
            job.add(entry.getKey(), entry.getValue());
        }

        return job;
    }
    private static String getDataFromJsonArray(JsonArray array, String key) {
        String data = "";
        JsonObject obj = array.getJsonObject(0);
        for (Map.Entry<String, JsonValue> item : obj.entrySet()) {
            if (item.getKey().equalsIgnoreCase(key)) {
                data = item.getValue().toString();
                break;
            }
        }

        return data;
    }
}
