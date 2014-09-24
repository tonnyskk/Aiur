package com.origin.aiur.activity.group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.origin.aiur.BaseActivity;
import com.origin.aiur.R;
import com.origin.aiur.vo.UserGroup;

public class GroupActivity extends BaseActivity {

    enum Keys {
        group_id
    }

    public static void startActivity(Context context, UserGroup userGroup) {
        Intent intent = new Intent(context, GroupActivity.class);
        intent.putExtra(Keys.group_id.name(), userGroup.getGroupName());
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.group, menu);
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
    protected void onPostExecuteSuccessful(String action, Object response) {

    }

    @Override
    protected void onPostExecuteFailed(String action) {

    }
}
