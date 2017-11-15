package uk.co.really99.cordova.plugin.firebaserealtime;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.Query;

class ChildListenerRemover implements ListenerRemover {

    private Query query;
    private ChildEventListener listener;

    public ChildListenerRemover(Query query, ChildEventListener listener) {

        this.query = query;
        this.listener = listener;
    }

    @Override
    public void remove() {

        Log.d(FirebaseRealtimePlugin.TAG, "ChildListenerRemover: remove listener");

        query.removeEventListener(listener);
    }
}
