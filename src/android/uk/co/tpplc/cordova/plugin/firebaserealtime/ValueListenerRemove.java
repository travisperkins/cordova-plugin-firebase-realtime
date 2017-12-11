package uk.co.tpplc.cordova.plugin.firebaserealtime;


import android.util.Log;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

class ValueListenerRemover implements ListenerRemover {

    private Query query;
    private ValueEventListener listener;

    public ValueListenerRemover(Query query, ValueEventListener listener) {

        this.query = query;
        this.listener = listener;
    }

    @Override
    public void remove() {

        Log.d(FirebaseRealtimePlugin.TAG, "ValueListenerRemover: remove listener");

        query.removeEventListener(listener);
    }
}
