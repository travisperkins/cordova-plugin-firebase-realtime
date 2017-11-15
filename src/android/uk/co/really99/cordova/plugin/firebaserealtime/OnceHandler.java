package uk.co.really99.cordova.plugin.firebaserealtime;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OnceHandler implements ActionHandler {

    private final QueryBuilder queryBuilder;
    private CordovaInterface cordova;

    public OnceHandler(CordovaInterface cordova, QueryBuilder queryBuilder) {
        this.cordova = cordova;
        this.queryBuilder = queryBuilder;
    }

    public boolean handle(JSONArray args, final CallbackContext callbackContext) {

        final String path = args.optString(0);
        final JSONObject orderBy = args.optJSONObject(1);
        final JSONArray includes = args.optJSONArray(2);
        final JSONObject limit = args.optJSONObject(3);

        Log.d(FirebaseRealtimePlugin.TAG, "OnceHandler: starting to listen for single value");

        try {
            Query query = queryBuilder.createQuery(path, orderBy, includes, limit);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    callbackContext.sendPluginResult(PluginResultHelper.createPluginResult(dataSnapshot, false));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                    PluginResult errorResult = PluginResultHelper.createPluginResult(databaseError, false);
                    callbackContext.sendPluginResult(errorResult);
                }
            });
        } catch (JSONException e) {
            Log.e(FirebaseRealtimePlugin.TAG, "OnceHandler: error", e);
            callbackContext.error(e.getMessage());
        }

        return true;
    }
}
