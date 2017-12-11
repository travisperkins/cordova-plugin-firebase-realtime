package uk.co.tpplc.cordova.plugin.firebaserealtime;

import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;

public class InitializeHandler implements ActionHandler {

    private FirebaseRealtimePlugin firebaseRealtimePlugin;

    public InitializeHandler(FirebaseRealtimePlugin firebaseRealtimePlugin) {
        this.firebaseRealtimePlugin = firebaseRealtimePlugin;
    }

    @Override
    public boolean handle(JSONArray args, CallbackContext callbackContext) {
        Log.d(FirebaseRealtimePlugin.TAG, "Initialise database - setting persistence");

        firebaseRealtimePlugin.database.setPersistenceEnabled(args.optBoolean(0, false));
        return true;
    }
}
