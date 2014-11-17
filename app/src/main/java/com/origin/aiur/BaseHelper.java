package com.origin.aiur;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;
import android.widget.Toast;

import com.origin.aiur.activity.logon.LoginActivity;
import com.origin.aiur.dao.IdentityDao;
import com.origin.aiur.http.HttpExecutor;
import com.origin.aiur.utils.ALogger;
import com.origin.aiur.utils.AppUtils;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2014/11/17.
 */
public class BaseHelper {
    private IBaseActivity callback;
    private ProgressDialog progressDialog;

    public BaseHelper(IBaseActivity callback) {
        this.callback = callback;
    }

    /**
     * Send out a GET request to backend server
     *
     * @param action
     * @param pathParam   parameters to construct restful get url
     */
    public void getSync(final String action, final Object... pathParam) {
        String path = callback.getPath(action, pathParam);
        ALogger.log(ALogger.LogPriority.debug, BaseHelper.class, "Send GET request to %s", path);

        if (path == null) {
            postExecuteFailed(action, callback.getContext().getResources().getString(R.string.invalid_path), -1);
            return;
        }

        // Call onPreExecute in UI Thread to show loading dialog or other
        callback.onPreExecute(action);

        // Sendout http request
        HttpExecutor.getExecutor().executeGet(action, path, this);
    }


    /**
     * Send out a post request to backend server
     *
     * @param action
     * @param paramMap  post parameters
     * @param pathParam parameters to construct restful post url
     */
    public void postSync(final String action, final HashMap<String, Object> paramMap, final Object... pathParam) {
        String path = callback.getPath(action, pathParam);
        ALogger.log(ALogger.LogPriority.debug, BaseActivity.class, "Send POST request to %s", path);

        if (path == null) {
            postExecuteFailed(action, callback.getContext().getResources().getString(R.string.invalid_path), -1);
            return;
        }

        // Call onPreExecute in UI Thread to show loading dialog or other
        callback.onPreExecute(action);
        HttpExecutor.getExecutor().executePost(action, path, paramMap, this);
    }


    public void postExecuteFailed(String action, String message, int errorCode) {
        ALogger.log(ALogger.LogPriority.debug, BaseActivity.class, "postExecuteFailed %s [%d]", message, errorCode);
        // Cancel loading dialog if exists
        hideProcessDialog();

        // process error logic
        if (errorCode == 4096) {
            // token expire or invalid any more, need refresh token
            IdentityDao.getInstance().clearToken();
            LoginActivity.startActivity(callback.getContext());
        } else {
            if (callback.isToastError() && message != null) {
                showToastMessage(message);
            }
            callback.onPostExecuteFailed(action);
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
        if (statusCode >= 4000) {
            showToastMessage(callback.getContext().getText(AppUtils.getResIdByStatusCode(statusCode)).toString());
            callback.onPostExecuteFailed(action);
        } else {
            //4. Notify activity
            callback.onPostExecuteSuccessful(action, message);
        }
    }

    public void showProcessDialog(String message) {
        hideProcessDialog();

        progressDialog = new ProgressDialog(callback.getContext(), R.style.dialog);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        if (message != null) {
            progressDialog.setMessage(message);
        }
        progressDialog.show();

    }

    public void hideProcessDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public void showToastMessage(String message) {
        Toast.makeText(callback.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public Context getContext() {
        return callback.getContext();
    }

}
