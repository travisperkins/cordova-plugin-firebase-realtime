package uk.co.really99.cordova.plugin.firebaserealtime;


import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;

public class RemoveHandler implements ActionHandler {

    private FirebaseRealtimePlugin firebaseRealtimePlugin;

    public RemoveHandler(FirebaseRealtimePlugin firebaseRealtimePlugin) {
        this.firebaseRealtimePlugin = firebaseRealtimePlugin;
    }

    @Override
    public boolean handle(JSONArray args, final CallbackContext callbackContext) {

        final String path = args.optString(0);

        Log.d(FirebaseRealtimePlugin.TAG, "RemoveHandler: removing value");

        firebaseRealtimePlugin.database.getReference(path).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                if (databaseError != null) {
                    callbackContext.sendPluginResult(PluginResultHelper.createPluginResult(databaseError, false));
                } else {
                    callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, path));
                }
            }
        });

        return true;
    }
}
