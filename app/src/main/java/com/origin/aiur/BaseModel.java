package com.origin.aiur;

import android.content.Intent;

/**
 * Created by Administrator on 2014/9/23.
 */
public abstract class BaseModel {

    private BaseActivity baseActivity;

    public BaseModel(BaseActivity activity) {
        this.baseActivity = activity;
    }

    public Intent getIntent() {
        return baseActivity.getIntent();
    }

    protected abstract void doInBackground(String actionName);
}
