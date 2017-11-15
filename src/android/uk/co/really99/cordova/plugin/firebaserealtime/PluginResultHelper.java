package uk.co.really99.cordova.plugin.firebaserealtime;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class PluginResultHelper {
    public static PluginResult createPluginResult(DatabaseError databaseError, boolean keepCallback) {

        Log.d(FirebaseRealtimePlugin.TAG, "PluginResultHelper: createPluginResult for DatabaseError");

        JSONObject data = new JSONObject();
        try {
            data.put("code", databaseError.getCode());
            data.put("details", databaseError.getDetails());
            data.put("message", databaseError.getMessage());
        } catch (JSONException e) {
            Log.d(FirebaseRealtimePlugin.TAG, "PluginResultHelper: Error in createPluginResult for DatabaseError", e);
        }

        PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, data);
        pluginResult.setKeepCallback(keepCallback);
        return pluginResult;
    }

    public static PluginResult createPluginResult(DataSnapshot dataSnapshot, boolean keepCallback) {

        Log.d(FirebaseRealtimePlugin.TAG, "PluginResultHelper: createPluginResult for DataSnapshot");

        JSONObject data = new JSONObject();
        Object value = dataSnapshot.getValue(false);
        try {
            data.put("priority", dataSnapshot.getPriority());
            data.put("key", dataSnapshot.getKey());
            if (value instanceof Map) {
                value = toJSON((Map<String, Object>) value);
            } else if (value instanceof List) {
                value = new JSONArray((List) value);
            }
            data.put("value", value);
        } catch (JSONException e) {
            Log.d(FirebaseRealtimePlugin.TAG, "PluginResultHelper: Error in createPluginResult for DataSnapshot", e);
        }

        PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, data);
        pluginResult.setKeepCallback(keepCallback);
        return pluginResult;
    }

    private static JSONObject toJSON(Map<String, Object> values) throws JSONException {
        JSONObject result = new JSONObject();
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof Map) {
                value = new JSONObject((Map) value);
            } else if (value instanceof List) {
                value = new JSONArray((List) value);
            }
            result.put(entry.getKey(), value);
        }
        return result;
    }
}
