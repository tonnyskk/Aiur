package com.origin.aiur;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.widget.Toast;

import com.origin.aiur.activity.logon.LoginActivity;
import com.origin.aiur.app.AiurApplication;
import com.origin.aiur.dao.IdentityDao;
import com.origin.aiur.http.HttpExecutor;
import com.origin.aiur.utils.ALogger;
import com.origin.aiur.utils.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2014/9/23.
 */
public abstract class BaseActivity extends ActionBarActivity {
    private ProgressDialog progressDialog;

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

    protected void onPreExecute(final String action) {
    }

    public void postExecuteFailed(String action, String message, int errorCode) {
        ALogger.log(ALogger.LogPriority.debug, BaseActivity.class, "postExecuteFailed %s [%d]", message, errorCode);
        // Cancel loading dialog if exists
        hideProcessDialog();

        // process error logic
        if (errorCode == 4096) {
            // token expire or invalid any more, need refresh token
            IdentityDao.getInstance().clearToken();
            LoginActivity.startActivity(this);
        } else {
            if (isToastError() && message != null) {
                showToastMessage(message);
            }
            onPostExecuteFailed(action);
        }
    }

    public void postExecuteSuccess(String action, JSONObject message) {
        //1.  cancel loading dialog
        hideProcessDialog();

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
        int statusCode = AppUtils.getJsonInt(message, "statusCode");
        if (statusCode != 200) {
            if (statusCode >= 4000) {
                showToastMessage(getText(AppUtils.getResIdByStatusCode(statusCode)).toString());
            }
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
    protected abstract HashMap<String, Object> getPostParam(String action);

    protected boolean isToastError() {
        return true;
    }

    protected void showToastMessage(String message) {
        Toast.makeText(BaseActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    protected void showProcessDialog(String message) {
        hideProcessDialog();

        progressDialog = new ProgressDialog(this, R.style.dialog);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        if (message != null) {
            progressDialog.setMessage(message);
        }
        progressDialog.show();

    }

    protected void hideProcessDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
