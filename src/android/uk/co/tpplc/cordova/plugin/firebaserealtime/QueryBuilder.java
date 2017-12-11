package uk.co.tpplc.cordova.plugin.firebaserealtime;

import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QueryBuilder {

    private FirebaseDatabase database;

    public QueryBuilder(FirebaseDatabase database) {
        this.database = database;
    }

    public Query createQuery(String path, JSONObject orderBy, JSONArray includes, JSONObject limit) throws JSONException {
        Query query = this.database.getReference(path);

        Log.d(FirebaseRealtimePlugin.TAG, "QueryBuilder: syncing reference");

        if (orderBy != null) {

            boolean orderByKey = false;


            query = processOrderBy(query, orderBy);
            if (orderBy.has("key")) {
                orderByKey = true;
            }

            for (int i = 0, n = includes.length(); i < n; ++i) {
                JSONObject filters = includes.getJSONObject(i);

                String key = filters.optString("key");
                Object endAt = filters.opt("endAt");
                Object startAt = filters.opt("startAt");
                Object equalTo = filters.opt("equalTo");

                query = startAtHandler(query, startAt, key, orderByKey);
                query = endAtHandler(query, endAt, key, orderByKey);
                query = equalToHandler(query, equalTo, key, orderByKey);
            }

            if (limit != null) {
                Log.d(FirebaseRealtimePlugin.TAG, "limit");

                if (limit.has("first")) {
                    query = query.limitToFirst(limit.getInt("first"));
                } else if (limit.has("last")) {
                    query = query.limitToLast(limit.getInt("last"));
                }
            }
        }

        return query;
    }

    private Query processOrderBy(Query query, JSONObject orderBy) throws JSONException {

        Log.d(FirebaseRealtimePlugin.TAG, "QueryBuilder: processing orderBy");

        if (orderBy.has("key")) {
            Log.d(FirebaseRealtimePlugin.TAG, "orderby key");
            query = query.orderByKey();
        } else if (orderBy.has("value")) {
            Log.d(FirebaseRealtimePlugin.TAG, "orderby value");
            query = query.orderByValue();
        } else if (orderBy.has("priority")) {
            Log.d(FirebaseRealtimePlugin.TAG, "orderby priority");
            query = query.orderByPriority();
        } else if (orderBy.has("child")) {
            Log.d(FirebaseRealtimePlugin.TAG, "orderby child");
            query = query.orderByChild(orderBy.getString("child"));
        } else {
            throw new JSONException("order is invalid");
        }

        return query;
    }

    private Query startAtHandler(Query query, Object startAt, String key, boolean orderByKey) {

        if (startAt != null) {
            Log.d(FirebaseRealtimePlugin.TAG, "QueryBuilder: processing startAt");

            if (orderByKey) {
                if (startAt instanceof Number) {
                    query = query.startAt((Double) startAt);
                } else if (startAt instanceof Boolean) {
                    query = query.startAt((Boolean) startAt);
                } else {
                    query = query.startAt(startAt.toString());
                }
            } else {
                if (startAt instanceof Number) {
                    query = query.startAt((Double) startAt, key);
                } else if (startAt instanceof Boolean) {
                    query = query.startAt((Boolean) startAt, key);
                } else {
                    query = query.startAt(startAt.toString(), key);
                }
            }
        }

        return query;
    }

    private Query endAtHandler(Query query, Object endAt, String key, boolean orderByKey) {
        if (endAt != null) {
            Log.d(FirebaseRealtimePlugin.TAG, "QueryBuilder: processing endAt");

            if (orderByKey) {
                if (endAt instanceof Number) {
                    query = query.endAt((Double) endAt);
                } else if (endAt instanceof Boolean) {
                    query = query.endAt((Boolean) endAt);
                } else {
                    query = query.endAt(endAt.toString());
                }
            } else {
                if (endAt instanceof Number) {
                    query = query.endAt((Double) endAt, key);
                } else if (endAt instanceof Boolean) {
                    query = query.endAt((Boolean) endAt, key);
                } else {
                    query = query.endAt(endAt.toString(), key);
                }
            }
        }

        return query;
    }

    private Query equalToHandler(Query query, Object equalTo, String key, boolean orderByKey) {
        if (equalTo != null) {
            Log.d(FirebaseRealtimePlugin.TAG, "QueryBuilder: processing equalTo");

            if (orderByKey) {
                if (equalTo instanceof Number) {
                    query = query.equalTo((Double) equalTo);
                } else if (equalTo instanceof Boolean) {
                    query = query.equalTo((Boolean) equalTo);
                } else {
                    query = query.equalTo(equalTo.toString());
                }
            } else {
                if (equalTo instanceof Number) {
                    query = query.equalTo((Double) equalTo, key);
                } else if (equalTo instanceof Boolean) {
                    query = query.equalTo((Boolean) equalTo, key);
                } else {
                    query = query.equalTo(equalTo.toString(), key);
                }
            }
        }

        return query;
    }
}
