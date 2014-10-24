package com.origin.aiur.activity.group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.origin.aiur.BaseActivity;
import com.origin.aiur.R;

import org.json.JSONObject;

import java.util.HashMap;

public class JoinGroupActivity extends BaseActivity {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, JoinGroupActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_join_group);
    }

    @Override
    protected void onPostExecuteSuccessful(String action, JSONObject response) {

    }

    @Override
    protected void onPostExecuteFailed(String action) {

    }

    @Override
    protected String getPath(String action) {
        return null;
    }

    @Override
    protected HashMap<String, Object> getPostParam(String action) {
        return null;
    }

}
