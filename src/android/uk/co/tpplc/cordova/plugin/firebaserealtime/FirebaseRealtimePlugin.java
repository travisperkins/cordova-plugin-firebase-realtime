package uk.co.tpplc.cordova.plugin.firebaserealtime;

import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class FirebaseRealtimePlugin extends CordovaPlugin {

    final static String TAG = "FirebaseRealtimePlugin";
    FirebaseDatabase database;
    Map<String, ListenerRemover> listeners = new HashMap<String, ListenerRemover>();
    Map<String, ActionHandler> handlers = new HashMap<String, ActionHandler>();
    private QueryBuilder queryBuilder;

    @Override
    protected void pluginInitialize() {

        Log.d(FirebaseRealtimePlugin.TAG, "Initialize plugin");

        database = FirebaseDatabase.getInstance();
        queryBuilder = new QueryBuilder(database);

        handlers.put("once", new OnceHandler(cordova, queryBuilder));
        handlers.put("push", new PushHandler(cordova, this));
        handlers.put("on", new OnHandler(cordova, queryBuilder, this));
        handlers.put("off", new OffHandler(this));
        handlers.put("update", new UpdateHandler(this));
        handlers.put("set", new SetHandler(this));
        handlers.put("remove", new RemoveHandler(this));
        handlers.put("initialize", new InitializeHandler(this));
        handlers.put("setOnline", new SetOnlineHandler(this));
        handlers.put("setLoggingEnabled", new SetLoggingHandler(this));
        handlers.put("keepSynced", new KeepSyncedHandler(queryBuilder));
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        Log.d(FirebaseRealtimePlugin.TAG, "Execute action: " + action);

        if (handlers.containsKey(action)) {
            return handlers.get(action).handle(args, callbackContext);
        }

        return false;
    }

    FirebaseDatabase getDatabase() {
        return database;
    }

    public void addListener(String id, ListenerRemover remover) {
        listeners.put(id, remover);
    }

    public void removeListener(String id) {

        if (listeners.containsKey(id)) {
            listeners.remove(id).remove();
        }
    }
}
