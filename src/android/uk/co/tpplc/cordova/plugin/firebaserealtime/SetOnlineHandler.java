package uk.co.tpplc.cordova.plugin.firebaserealtime;

import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;

public class SetOnlineHandler implements ActionHandler {

    private FirebaseRealtimePlugin firebaseRealtimePlugin;

    public SetOnlineHandler(FirebaseRealtimePlugin firebaseRealtimePlugin) {
        this.firebaseRealtimePlugin = firebaseRealtimePlugin;
    }

    @Override
    public boolean handle(JSONArray args, CallbackContext callbackContext) {

        Log.d(FirebaseRealtimePlugin.TAG, "SetLoggingHandler: setting online status");

        if (args.optBoolean(0, false)) {
            firebaseRealtimePlugin.getDatabase().goOnline();
        } else {
            firebaseRealtimePlugin.getDatabase().goOffline();
        }

        return true;
    }
}
