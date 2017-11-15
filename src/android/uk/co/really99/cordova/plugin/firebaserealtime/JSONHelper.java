package uk.co.really99.cordova.plugin.firebaserealtime;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Map;

public class JSONHelper {

    static Object toSettable(Object value) {

        Object result = value;
        if (value instanceof JSONObject) {
            Type type = new TypeToken<Map<String, Object>>() {
            }.getType();
            result = new Gson().fromJson(value.toString(), type);
        }

        return result;
    }

    static Map<String, Object> toSettableMap(JSONObject value) {

        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        Map<String, Object> result = new Gson().fromJson(value.toString(), type);

        return result;
    }
}
