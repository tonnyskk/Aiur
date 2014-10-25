package com.origin.aiur.http;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.origin.aiur.BaseActivity;
import com.origin.aiur.app.AiurApplication;
import com.origin.aiur.dao.IdentityDao;
import com.origin.aiur.dao.UserDao;
import com.origin.aiur.utils.ALogger;
import com.origin.aiur.utils.AppUtils;
import com.origin.aiur.utils.VolleyErrorHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dongjia on 9/24/2014.
 */
public class HttpExecutor {
    private static HttpExecutor instance = new HttpExecutor();
    private static boolean fakeHttp = false;

    private HttpExecutor() {
    }

    public static HttpExecutor getExecutor() {
        return instance;
    }

    public void executeGet(final String tag, String path, final BaseActivity callback) {
        if (fakeHttp) {
            callback.postExecuteSuccess(tag, null);
            return;
        }
        // cancel pending request
        AiurApplication.getInstance().cancelPendingRequests(tag);
        // Sending request
        JsonObjectRequest req = new JsonObjectRequest(parseUrl(path), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            ALogger.log(ALogger.LogPriority.debug, HttpExecutor.class, "Response:%n %s", response.toString(4));
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
                VolleyLog.e("Error: %s", error.getMessage());
                ALogger.log(ALogger.LogPriority.error, HttpExecutor.class, "Error:%n %s", error.getMessage());
                NetworkResponse response = error.networkResponse;
                int statusCode = -1;
                if (response != null) {
                    statusCode = response.statusCode;
                }
                if (callback != null) {
                    callback.postExecuteFailed(tag, VolleyErrorHelper.getMessage(error, callback), statusCode);
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json;charset=UTF-8");
                headers.put("device-id", IdentityDao.getInstance().getDeviceId());
                headers.put("token", IdentityDao.getInstance().getToken());
                headers.put("uId", String.valueOf(UserDao.getInstance().getUserId()));
                return headers;
            }
        };
        // add the request object to the queue to be executed
        AiurApplication.getInstance().addToRequestQueue(req, tag);

    }

    public void executePost(final String tag, String path, HashMap<String, Object> params, final BaseActivity callback) {
        if (fakeHttp) {
            callback.postExecuteSuccess(tag, null);
            return;
        }

        // cancel pending request
        AiurApplication.getInstance().cancelPendingRequests(tag);

        // Post params to be sent to the server
        if (params == null) {
            params = new HashMap<String, Object>();
        }

        JsonObjectRequest req = new JsonObjectRequest(parseUrl(path), new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            ALogger.log(ALogger.LogPriority.debug, HttpExecutor.class, "Response:%n %s", response.toString(4));
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
                VolleyLog.e("Error: %s", error.getMessage());
                ALogger.log(ALogger.LogPriority.error, HttpExecutor.class, "Error:%n %s", error.getMessage());
                NetworkResponse response = error.networkResponse;
                int statusCode = -1;
                if (response != null) {
                    statusCode = response.statusCode;
                }
                if (callback != null) {
                    callback.postExecuteFailed(tag, VolleyErrorHelper.getMessage(error, callback), statusCode);
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json;charset=UTF-8");
                headers.put("device-id", IdentityDao.getInstance().getDeviceId());
                headers.put("token", IdentityDao.getInstance().getToken());
                headers.put("uId", String.valueOf(UserDao.getInstance().getUserId()));
                return headers;
            }
        };

        // add the request object to the queue to be executed
        AiurApplication.getInstance().addToRequestQueue(req, tag);
    }

    private String parseUrl(String path) {
        String finalUrl = HttpUtils.BASE_URL;
        if (path.startsWith("/")) {
            finalUrl += path;
        } else {
            finalUrl += "/" + path;
        }
        VolleyLog.v("Request url: %s", finalUrl);
        return finalUrl;
    }
}
