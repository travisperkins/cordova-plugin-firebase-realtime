package uk.co.tpplc.cordova.plugin.firebaserealtime;

import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;

public class OffHandler implements ActionHandler {

    private FirebaseRealtimePlugin firebaseRealtimePlugin;

    public OffHandler(FirebaseRealtimePlugin firebaseRealtimePlugin) {
        this.firebaseRealtimePlugin = firebaseRealtimePlugin;
    }

    @Override
    public boolean handle(JSONArray args, CallbackContext callbackContext) {
        String id = args.optString(0);

        Log.d(FirebaseRealtimePlugin.TAG, "OffHandler: remove listener");

        firebaseRealtimePlugin.removeListener(id);

        return true;
    }
}
