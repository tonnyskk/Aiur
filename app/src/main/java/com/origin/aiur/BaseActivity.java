package com.origin.aiur;

import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.origin.aiur.app.AiurApplication;
import com.origin.aiur.http.HttpExecutor;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2014/9/23.
 */
public abstract class BaseActivity extends ActionBarActivity {

    protected final void getSync(final String action) {
        String path = getPath(action);
        if (path == null) {
            postExecuteFailed(action, this.getResources().getString(R.string.invalid_path));
            return;
        }

        // Call onPreExecute in UI Thread to show loading dialog or other
        onPreExecute(action);

        // Sendout http request
        HttpExecutor.getExecutor().executeGet(action, path, this);
    }

    protected final void postSync(final String action) {
        String path = getPath(action);
        if (path == null) {
            postExecuteFailed(action, this.getResources().getString(R.string.invalid_path));
            return;
        }

        // Call onPreExecute in UI Thread to show loading dialog or other
        onPreExecute(action);
        HttpExecutor.getExecutor().executePost(action, path, getParam(action), this);
    }

    public void postExecuteFailed(String action, String message) {
        if (showToastMessage() && message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        onPostExecuteFailed(action);
    }

    public void postExecuteSuccess(String action, JSONObject message) {
        //1.  cancel loading dialog

        //2. Alert warning message
        int responseStatus = getResponseStatus(message);
        if (responseStatus <= 0) {
            Toast.makeText(this, getResources().getText(R.string.invalid_response_format), Toast.LENGTH_LONG).show();
        }
        //3. Notify activity
        onPostExecuteSuccessful(action, message);
    }

    protected abstract void onPostExecuteSuccessful(String action, JSONObject response);
    protected abstract void onPostExecuteFailed(String action);
    protected abstract String getPath(String action);

    protected HashMap<String, String> getParam(String action) {
        return null;
    }

    protected void onPreExecute(String action) {
    }

    protected boolean showToastMessage() {
        return true;
    }

    protected int getResponseStatus(JSONObject response) {
        if (response != null && response.has("statusCode")) {
            try {
                //JSONObject responseStatus = response.getJSONObject("status");
                return response.getInt("statusCode");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }
}
