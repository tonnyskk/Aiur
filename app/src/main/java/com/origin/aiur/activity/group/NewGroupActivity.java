package com.origin.aiur.activity.group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.origin.aiur.BaseActivity;
import com.origin.aiur.R;
import com.origin.aiur.dao.UserDao;
import com.origin.aiur.http.HttpUtils;
import com.origin.aiur.utils.ALogger;
import com.origin.aiur.utils.AppUtils;
import com.origin.aiur.vo.User;

import org.json.JSONObject;

import java.util.HashMap;

public class NewGroupActivity extends BaseActivity implements View.OnClickListener{
    private TextView btnCreateGroup;
    private EditText newGroupName;
    private EditText newGroupIntro;

    private enum Actions {
        create_group
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, NewGroupActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_new_group);

        btnCreateGroup = (TextView) findViewById(R.id.btnNewGroup);
        btnCreateGroup.setOnClickListener(this);

        newGroupName = (EditText) findViewById(R.id.newGroupName);
        newGroupIntro = (EditText) findViewById(R.id.newGroupIntro);
    }

    @Override
    protected void onPostExecuteSuccessful(String action, JSONObject response) {
        switch (Actions.valueOf(action)) {
            case create_group:
                // parse response to update local groupLit

                // terminate this activity
                this.finish();
                break;
        }
    }

    @Override
    protected void onPostExecuteFailed(String action) {

    }

    @Override
    protected String getPath(String action, Object... args) {
        String path = null;
        switch (Actions.valueOf(action)) {
            case create_group:
                path = HttpUtils.create_group;
                break;
        }
        return path;
    }

    @Override
    protected HashMap<String, Object> getPostParam(String action) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        switch (Actions.valueOf(action)) {
            case create_group:
                param.put("groupName", newGroupName.getText().toString());
                param.put("groupDesc", newGroupIntro.getText().toString());
                param.put("ownerUserId", UserDao.getInstance().getUserId());
                break;
        }

        return param;
    }

    @Override
    public void onClick(View view) {
        if (view == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.btnNewGroup:
                if (isInputValid()) {
                    this.postSync(Actions.create_group.name());
                }
                break;
        }
    }

    private boolean isInputValid() {
        String groupName = newGroupName.getText().toString();
        String groupDesc = newGroupIntro.getText().toString();

        if (AppUtils.isEmpty(groupName) || AppUtils.isEmpty(groupDesc)) {
            showToastMessage(this.getString(R.string.no_input_value));
            return false;
        }

        return true;
    }
}
