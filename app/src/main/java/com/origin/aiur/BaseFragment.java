package com.origin.aiur;

/**
 * Created by dongjia on 11/17/2014.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.HashMap;

/**
 * A placeholder fragment containing a simple view.
 */
public abstract class BaseFragment extends Fragment implements IBaseActivity, View.OnClickListener {
    private BaseHelper helper;

    public BaseFragment() {
        this.setArguments(new Bundle());
        helper = new BaseHelper(this);
    }

    public abstract String getTitle();

    public abstract void performClick(View view);

    @Override
    public void getSync(String action, Object... pathParam) {
        helper.getSync(action, pathParam);
    }

    @Override
    public void postSync(String action, HashMap<String, Object> paramMap, Object... pathParam) {
        helper.postSync(action, paramMap, pathParam);
    }

    @Override
    public void onPreExecute(String action) {

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
    public void showToastMessage(String message) {
        helper.showToastMessage(message);
    }

    @Override
    public boolean isToastError() {
        return true;
    }

    @Override
    public Context getContext() {
        return this.getActivity();
    }

    @Override
    public void onClick(View view) {
        performClick(view);
    }

    protected void closeBoard() {
        Context mContext = getContext();
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
