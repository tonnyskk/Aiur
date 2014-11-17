package com.origin.aiur;

import android.content.Context;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2014/11/17.
 */
public interface IBaseActivity {
    public void getSync(String action, Object... pathParam);
    public void postSync(String action, HashMap<String, Object> paramMap, Object... pathParam);


    public void onPreExecute(String action);
    public void onPostExecuteSuccessful(String action, JSONObject response);
    public void onPostExecuteFailed(String action);

    public void showProcessDialog(String message);
    public void hideProcessDialog();

    public void showToastMessage(String message);

    public String getPath(String action, Object... args);
    public boolean isToastError();
    public Context getContext();
}
