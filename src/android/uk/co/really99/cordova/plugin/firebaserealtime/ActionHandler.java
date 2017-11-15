package uk.co.really99.cordova.plugin.firebaserealtime;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;

public interface ActionHandler {
    boolean handle(JSONArray args, CallbackContext callbackContext);
}
