package uk.co.really99.cordova.plugin.firebaserealtime;

import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

public class SetHandler implements ActionHandler {

    private FirebaseRealtimePlugin firebaseRealtimePlugin;

    public SetHandler(FirebaseRealtimePlugin firebaseRealtimePlugin) {
        this.firebaseRealtimePlugin = firebaseRealtimePlugin;
    }

    @Override
    public boolean handle(JSONArray args, final CallbackContext callbackContext) {

        try {
            final String path = args.optString(0);
            final Object value = JSONHelper.toSettable(args.get(1));

            Log.d(FirebaseRealtimePlugin.TAG, "SetHandler: setting value");

            firebaseRealtimePlugin.database.getReference(path).setValue(value, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                    if (databaseError != null) {
                        callbackContext.sendPluginResult(PluginResultHelper.createPluginResult(databaseError, false));
                    } else {
                        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, path));
                    }
                }
            });
        } catch (JSONException e) {
            Log.e(FirebaseRealtimePlugin.TAG, "SetHandler", e);
        }
        return true;
    }
}
