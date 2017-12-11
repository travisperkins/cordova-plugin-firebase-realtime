package uk.co.tpplc.cordova.plugin.firebaserealtime;


import android.util.Log;

import com.google.firebase.database.ChildEventListener;
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

public class OnHandler implements ActionHandler {

    private final static String EVENT_TYPE_VALUE = "value";
    private final static String EVENT_TYPE_CHILD_ADDED = "child_added";
    private final static String EVENT_TYPE_CHILD_CHANGED = "child_changed";
    private final static String EVENT_TYPE_CHILD_REMOVED = "child_removed";
    private final static String EVENT_TYPE_CHILD_MOVED = "child_moved";

    private FirebaseRealtimePlugin firebaseRealtimePlugin;
    private CordovaInterface cordova;
    private QueryBuilder queryBuilder;

    public OnHandler(CordovaInterface cordova, QueryBuilder queryBuilder, FirebaseRealtimePlugin firebaseRealtimePlugin) {
        this.firebaseRealtimePlugin = firebaseRealtimePlugin;
        this.cordova = cordova;
        this.queryBuilder = queryBuilder;
    }

    public boolean handle(JSONArray args, final CallbackContext callbackContext) {

        final String path = args.optString(0);
        final String type = args.optString(1, EVENT_TYPE_VALUE);
        final JSONObject orderBy = args.optJSONObject(2);
        final JSONArray includes = args.optJSONArray(3);
        final JSONObject limit = args.optJSONObject(4);
        final String id = args.optString(5);

        Log.d(FirebaseRealtimePlugin.TAG, "OnHandler: starting to listen");

        try {
            Query query = queryBuilder.createQuery(path, orderBy, includes, limit);
            if (EVENT_TYPE_VALUE.equals(type)) {
                listenForValue(query, callbackContext, id);
            } else {
                listenForChild(query, callbackContext, id, type);
            }
        } catch (JSONException e) {
            callbackContext.error(e.getMessage());
        }

        return true;
    }

    private void listenForValue(Query query, final CallbackContext callbackContext, String id) {

        Log.d(FirebaseRealtimePlugin.TAG, "OnHandler: listen for value");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callbackContext.sendPluginResult(PluginResultHelper.createPluginResult(dataSnapshot, true));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                PluginResult errorResult = PluginResultHelper.createPluginResult(databaseError, false);
                callbackContext.sendPluginResult(errorResult);
            }
        };
        query.addValueEventListener(valueEventListener);
        firebaseRealtimePlugin.addListener(id, new ValueListenerRemover(query, valueEventListener));
    }

    private void listenForChild(Query query, final CallbackContext callbackContext, String id, final String type) {

        Log.d(FirebaseRealtimePlugin.TAG, "OnHandler: listen for child");

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (EVENT_TYPE_CHILD_ADDED.equals(type)) {

                    callbackContext.sendPluginResult(PluginResultHelper.createPluginResult(dataSnapshot, true));
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                if (EVENT_TYPE_CHILD_CHANGED.equals(type)) {

                    callbackContext.sendPluginResult(PluginResultHelper.createPluginResult(dataSnapshot, true));
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                if (EVENT_TYPE_CHILD_REMOVED.equals(type)) {

                    callbackContext.sendPluginResult(PluginResultHelper.createPluginResult(dataSnapshot, true));
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                if (EVENT_TYPE_CHILD_MOVED.equals(type)) {

                    callbackContext.sendPluginResult(PluginResultHelper.createPluginResult(dataSnapshot, true));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                PluginResult errorResult = PluginResultHelper.createPluginResult(databaseError, false);
                callbackContext.sendPluginResult(errorResult);
            }
        };
        query.addChildEventListener(childEventListener);
        firebaseRealtimePlugin.addListener(id, new ChildListenerRemover(query, childEventListener));

    }
}
