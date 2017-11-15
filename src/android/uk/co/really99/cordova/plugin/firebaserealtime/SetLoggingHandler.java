package uk.co.really99.cordova.plugin.firebaserealtime;

import android.util.Log;

import com.google.firebase.database.Logger;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;

public class SetLoggingHandler implements ActionHandler {

    private FirebaseRealtimePlugin firebaseRealtimePlugin;

    public SetLoggingHandler(FirebaseRealtimePlugin firebaseRealtimePlugin) {
        this.firebaseRealtimePlugin = firebaseRealtimePlugin;
    }

    @Override
    public boolean handle(JSONArray args, CallbackContext callbackContext) {

        Log.d(FirebaseRealtimePlugin.TAG, "SetLoggingHandler: setting log level");

        if (args.optBoolean(0, false)) {
            firebaseRealtimePlugin.getDatabase().setLogLevel(Logger.Level.DEBUG);
        } else {
            firebaseRealtimePlugin.getDatabase().setLogLevel(Logger.Level.NONE);
        }
        return true;
    }
}
