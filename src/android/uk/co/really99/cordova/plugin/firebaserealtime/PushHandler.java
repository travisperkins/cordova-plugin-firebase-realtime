package uk.co.really99.cordova.plugin.firebaserealtime;

import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;

class PushHandler implements ActionHandler {

    private FirebaseRealtimePlugin firebaseRealtimePlugin;
    private CordovaInterface cordova;

    public PushHandler(CordovaInterface cordova, FirebaseRealtimePlugin firebaseRealtimePlugin) {
        this.firebaseRealtimePlugin = firebaseRealtimePlugin;
        this.cordova = cordova;
    }

    public boolean handle(JSONArray args, final CallbackContext callbackContext) {

        final String path = args.optString(0);
        final Object value = JSONHelper.toSettable(args.opt(1));

        Log.d(FirebaseRealtimePlugin.TAG, "PushHandler: pushing data");

        firebaseRealtimePlugin.database.getReference(path).push().setValue(value, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                if (databaseError != null) {
                    callbackContext.sendPluginResult(PluginResultHelper.createPluginResult(databaseError, false));
                } else {
                    callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, path + '/' + databaseReference.getKey()));
                }
            }
        });

        return true;
    }
}
