package com.origin.aiur.activity.profile;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.origin.aiur.BaseActivity;
import com.origin.aiur.R;

import org.json.JSONObject;

public class ProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_profile);
    }

    @Override
    public void performClick(View view) {

    }

    @Override
    public void onPostExecuteSuccessful(String action, JSONObject response) {

    }

    @Override
    public void onPostExecuteFailed(String action) {

    }

    @Override
    public String getPath(String action, Object... args) {
        return null;
    }
}
