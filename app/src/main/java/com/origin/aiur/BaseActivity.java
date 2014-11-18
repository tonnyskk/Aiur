package com.origin.aiur;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
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
 * Created by Administrator on 2014/9/23.
 */
public abstract class BaseActivity extends FragmentActivity implements IBaseActivity, View.OnClickListener {
    private BaseHelper helper;

    public BaseActivity() {
        helper = new BaseHelper(this);
    }

    public abstract void performClick(View view);

    /**
     * Send out a GET request to backend server
     *
     * @param action
     * @param pathParam parameters to construct restful get url
     */
    @Override
    public void getSync(String action, Object... pathParam) {
        helper.getSync(action, pathParam);
    }

    /**
     * Send out a post request to backend server
     *
     * @param action
     * @param paramMap  post parameters
     * @param pathParam parameters to construct restful post url
     */
    @Override
    public void postSync(String action, HashMap<String, Object> paramMap, Object... pathParam) {
        helper.postSync(action, paramMap, pathParam);
    }

    @Override
    public void onPreExecute(String action) {

    }

    @Override
    public boolean isToastError() {
        return true;
    }

    @Override
    public void showToastMessage(String message) {
        helper.showToastMessage(message);
    }

    @Override
    public void showProcessDialog(String message) {
        helper.showProcessDialog(message);

    }

    @Override
    public void hideProcessDialog() {
        helper.hideProcessDialog();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onClick(View view) {
        performClick(view);
    }
}
