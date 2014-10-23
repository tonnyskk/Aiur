package com.origin.aiur;

import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.origin.aiur.activity.logon.LoginActivity;
import com.origin.aiur.app.AiurApplication;
import com.origin.aiur.dao.IdentityDao;
import com.origin.aiur.http.HttpExecutor;
import com.origin.aiur.utils.AppUtils;

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
            postExecuteFailed(action, this.getResources().getString(R.string.invalid_path), -1);
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
            postExecuteFailed(action, this.getResources().getString(R.string.invalid_path), -1);
            return;
        }

        // Call onPreExecute in UI Thread to show loading dialog or other
        onPreExecute(action);
        HttpExecutor.getExecutor().executePost(action, path, getPostParam(action), this);
    }

    public void postExecuteFailed(String action, String message, int errorCode) {
        if (errorCode == 4096) {
            // token expire or invalid any more, need refresh token
            IdentityDao.getInstance().clearToken();
            LoginActivity.startActivity(this);
        } else {
            if (showToastMessage() && message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
            onPostExecuteFailed(action);
        }
    }

    public void postExecuteSuccess(String action, JSONObject message) {
        //1.  cancel loading dialog

        //2. Check RSA public key
        String rsaPublicKey = AppUtils.getJsonString(message, "key");
        if (!AppUtils.isEmpty(rsaPublicKey)) {
            IdentityDao.getInstance().setKey(rsaPublicKey);
        }

        //3. Check token
        String token = AppUtils.getJsonString(message, "token");
        if (!AppUtils.isEmpty(token)) {
            IdentityDao.getInstance().setToken(token);
        }

        //3. Alert warning message
        int responseStatus = AppUtils.getJsonInt(message, "statusCode");
        if (responseStatus != 200) {
            String respMessage = AppUtils.getJsonString(message, "statusMessage");
            Toast.makeText(this, getText(AppUtils.getResIdByStatusCode(responseStatus)), Toast.LENGTH_LONG).show();
            onPostExecuteFailed(action);
        } else {
            //4. Notify activity
            onPostExecuteSuccessful(action, message);
        }

    }

    protected abstract void onPostExecuteSuccessful(String action, JSONObject response);
    protected abstract void onPostExecuteFailed(String action);

    /**
     * Request path for your request.
     * @param action
     * @return
     */
    protected abstract String getPath(String action);

    /**
     * Parameter map for POST request, fill your parameters into a hashmap
     * @param action
     * @return HashMap
     */
    protected abstract HashMap<String, String> getPostParam(String action);

    protected void onPreExecute(String action) {
    }

    protected boolean showToastMessage() {
        return true;
    }

    protected void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
