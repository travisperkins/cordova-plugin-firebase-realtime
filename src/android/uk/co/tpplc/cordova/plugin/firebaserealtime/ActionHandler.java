package uk.co.tpplc.cordova.plugin.firebaserealtime;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;

public interface ActionHandler {
    boolean handle(JSONArray args, CallbackContext callbackContext);
}
