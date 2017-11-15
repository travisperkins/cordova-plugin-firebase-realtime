package uk.co.really99.cordova.plugin.firebaserealtime;

import android.util.Log;

import com.google.firebase.database.Query;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;

public class KeepSyncedHandler implements ActionHandler {

    private QueryBuilder queryBuilder;

    public KeepSyncedHandler(QueryBuilder queryBuilder) {
        this.queryBuilder = queryBuilder;
    }

    @Override
    public boolean handle(JSONArray args, CallbackContext callbackContext) {

        final String path = args.optString(0);
        final boolean keepSynced = args.optBoolean(1);

        Log.d(FirebaseRealtimePlugin.TAG, "KeepSyncedHandler: setting log level");

        try {
            Query query = this.queryBuilder.createQuery(path, null, null, null);
            query.keepSynced(keepSynced);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return true;
    }
}
