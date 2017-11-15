package uk.co.really99.cordova.plugin.firebaserealtime;

import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.Map;

public class UpdateHandler implements ActionHandler {

    private FirebaseRealtimePlugin firebaseRealtimePlugin;

    public UpdateHandler(FirebaseRealtimePlugin firebaseRealtimePlugin) {
        this.firebaseRealtimePlugin = firebaseRealtimePlugin;
    }

    public boolean handle(JSONArray args, final CallbackContext callbackContext) {

        try {
            final String path = args.getString(0);
            final Map<String, Object> updates = JSONHelper.toSettableMap(args.optJSONObject(1));

            Log.d(FirebaseRealtimePlugin.TAG, "UpdateHandler: Updating value");

            firebaseRealtimePlugin.database.getReference(path).updateChildren(updates, new DatabaseReference.CompletionListener() {
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
            e.printStackTrace();
        }

        return true;
    }
}
