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

public class NewGroupActivity extends BaseActivity {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, NewGroupActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_new_group);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
    protected HashMap<String, String> getPostParam(String action) {
        return null;
    }

}
