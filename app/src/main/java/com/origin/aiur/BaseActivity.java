package com.origin.aiur;

import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.origin.aiur.app.AiurApplication;
import com.origin.aiur.http.HttpExecutor;

import java.util.HashMap;

/**
 * Created by Administrator on 2014/9/23.
 */
public abstract class BaseActivity extends ActionBarActivity {

    protected final void getSync(final String action) {
        getSync(action, false);
    }

    protected final void getSync(final String action, final boolean isList) {
        String path = getPath(action);
        if (path == null) {
            postExecuteFailed(action, this.getResources().getString(R.string.invalid_path));
            return;
        }

        // Call onPreExecute in UI Thread to show loading dialog or other
        onPreExecute(action);

        // Sendout http request
        HttpExecutor.getExecutor().executeGet(action, path, this, isList);
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

    public void postExecuteSuccess(String action, Object message) {
        //1.  cancel loading dialog

        //2. Notify activity
        onPostExecuteSuccessful(action, message);
    }

    protected abstract void onPostExecuteSuccessful(String action, Object response);
    protected abstract void onPostExecuteFailed(String action);

    protected String getPath(String action){
        return null;
    }

    protected HashMap<String, String> getParam(String action) {
        return null;
    }

    protected void onPreExecute(String action) {
    }

    protected boolean showToastMessage() {
        return true;
    }

}
