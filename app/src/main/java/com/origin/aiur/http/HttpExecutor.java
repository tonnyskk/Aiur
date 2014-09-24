package com.origin.aiur.http;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.origin.aiur.BaseActivity;
import com.origin.aiur.app.AiurApplication;
import com.origin.aiur.utils.VolleyErrorHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by dongjia on 9/24/2014.
 */
public class HttpExecutor {
    private static HttpExecutor instance = new HttpExecutor();
    private static final String BASE_URL = "http://127.0.0.1:9090/test";
    private static boolean fakeHttp = true;
    private HttpExecutor() {
    }

    public static HttpExecutor getExecutor() {
        return instance;
    }

    public void executeGet(final String tag, String path, final BaseActivity callback, boolean isList) {
        if (fakeHttp) {
            callback.postExecuteSuccess(tag, null);
            return;
        }
        // cancel pending request
        AiurApplication.getInstance().cancelPendingRequests(tag);
        // Sending request
        if (isList) {
            JsonObjectRequest req = new JsonObjectRequest(parseUrl(path), null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                VolleyLog.v("Response:%n %s", response.toString(4));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (callback != null) {
                                callback.postExecuteSuccess(tag, response);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                    if (callback != null) {
                        callback.postExecuteFailed(tag, VolleyErrorHelper.getMessage(error, callback));
                    }
                }
            });
            // add the request object to the queue to be executed
            AiurApplication.getInstance().addToRequestQueue(req, tag);
        } else {
            JsonArrayRequest req = new JsonArrayRequest(parseUrl(path), new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        VolleyLog.v("Response:%n %s", response.toString(4));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (callback != null) {
                        callback.postExecuteSuccess(tag, response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                    if (callback != null) {
                        callback.postExecuteFailed(tag, VolleyErrorHelper.getMessage(error, callback));
                    }
                }
            });
            // add the request object to the queue to be executed
            AiurApplication.getInstance().addToRequestQueue(req, tag);
        }
    }

    public void executePost(final String tag, String path, HashMap<String, String> params, final BaseActivity callback) {
        if (fakeHttp) {
            callback.postExecuteSuccess(tag, null);
            return;
        }

        // cancel pending request
        AiurApplication.getInstance().cancelPendingRequests(tag);

        // Post params to be sent to the server
        if (params == null) {
            params = new HashMap<String, String>();
        }

        JsonObjectRequest req = new JsonObjectRequest(parseUrl(path), new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (callback != null) {
                            callback.postExecuteSuccess(tag, response);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                        if (callback != null) {
                            callback.postExecuteFailed(tag, VolleyErrorHelper.getMessage(error, callback));
                        }
                    }
                });

        // add the request object to the queue to be executed
        AiurApplication.getInstance().addToRequestQueue(req, tag);
    }

    private String parseUrl(String path) {
        String finalUrl = BASE_URL;
        if (path.startsWith("/")) {
            finalUrl += path;
        } else {
            finalUrl += "/" + path;
        }
        VolleyLog.v("Request url: %s", finalUrl);
        return finalUrl;
    }
}
