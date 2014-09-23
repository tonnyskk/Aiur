package com.origin.aiur.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.origin.aiur.BaseActivity;
import com.origin.aiur.BaseModel;
import com.origin.aiur.R;
import com.origin.aiur.vo.GroupActivity;
import com.origin.aiur.vo.UserGroup;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private TextView btnGotoGroup;
    private TextView btnChangeGroup;
    private TextView btnCreateGroup;
    private TextView btnJoinGroup;

    private View userGroupInfoContainer;
    private View userGroupModifyContainer;

    private ListView groupActivityList;
    private ListActivitiesAdapter groupActivityAdapter;
    private PopupWindow groupPopupWindow;

    private ListView popupGroupList = null;
    private ListGroupAdapter popupAdapter = null;

    public MainActivity() {
    }

    enum Actions {
        load_group, load_activity
    }

    enum Keys {
        group_list, activity_list
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGotoGroup = (TextView) findViewById(R.id.btnGotoGroup);
        btnGotoGroup.setOnClickListener(this);

        btnChangeGroup = (TextView) findViewById(R.id.btnChangeGroup);
        btnChangeGroup.setOnClickListener(this);

        btnCreateGroup = (TextView) findViewById(R.id.btnCreateGroup);
        btnCreateGroup.setOnClickListener(this);

        btnJoinGroup = (TextView) findViewById(R.id.btnJoinGroup);
        btnJoinGroup.setOnClickListener(this);

        userGroupInfoContainer = findViewById(R.id.userGroupInfoContainer);
        userGroupModifyContainer = findViewById(R.id.userGroupModifyContainer);

        groupActivityList = (ListView) findViewById(R.id.listGroupActivity);
        groupActivityAdapter = new ListActivitiesAdapter(this);
        groupActivityList.setAdapter(groupActivityAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        this.postSync(Actions.load_group.name());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menuGroupCreate) {
            // TODO: go to create group activity
            return true;
        } else if (id == R.id.menuGroupJoin) {
            // TODO: go to search group activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostExecuteSuccessful(String action) {
        switch (Actions.valueOf(action)) {
            case load_activity:
                ArrayList<GroupActivity> userActivityList = this.getIntent().getParcelableArrayListExtra(Keys.activity_list.name());
                groupActivityAdapter.setActivityList(userActivityList);
                break;

            case load_group:
                ArrayList<UserGroup> userGroupList = this.getIntent().getParcelableArrayListExtra(Keys.group_list.name());
                if (userGroupList == null || userGroupList.isEmpty()) {
                    // user has not join or create any group
                    userGroupInfoContainer.setVisibility(View.GONE);
                    userGroupModifyContainer.setVisibility(View.VISIBLE);
                } else {
                    // user has joined some group, then load user activities
                    userGroupInfoContainer.setVisibility(View.VISIBLE);
                    userGroupModifyContainer.setVisibility(View.GONE);
                    if (userGroupList.size() <= 1) {
                        btnChangeGroup.setVisibility(View.GONE);
                        // TODO: set userGroupList.get(0) as selected group, and save into DAO

                    } else {
                        btnChangeGroup.setVisibility(View.VISIBLE);
                    }

                    this.postSync(Actions.load_activity.name());
                }
                break;
        }
    }

    @Override
    public void onPostExecuteFailed(String action) {

    }

    @Override
    public BaseModel getModel() {
        return new MainModel(this);
    }

    @Override
    public void onClick(View view) {
        if (view == null) {
            return;
        }

        switch (view.getId()) {
            case R.id.btnChangeGroup:
                ArrayList<UserGroup> userGroupList = this.getIntent().getParcelableArrayListExtra(Keys.group_list.name());
                if (userGroupList != null && !userGroupList.isEmpty()) {
                    showPopupWindow(userGroupList);
                }
                break;
            case R.id.btnGotoGroup:
                // TODO: Go to group detail page, start group activity

                break;
            case R.id.btnCreateGroup:
                // TODO: go to create group activity
                break;

            case R.id.btnJoinGroup:
                // TODO: go to search group activity
                break;
        }
    }

    private void showPopupWindow(ArrayList<UserGroup> groupList) {
        if (groupPopupWindow == null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(this);

            View topWindow = mLayoutInflater.inflate(R.layout.popup_main_groups, null);

            groupPopupWindow = new PopupWindow(topWindow);
            groupPopupWindow.setOutsideTouchable(false);

            popupGroupList = (ListView)topWindow.findViewById(R.id.popupGroupList);
            popupGroupList.setOnItemClickListener(this);
            popupAdapter = new ListGroupAdapter(this);
            popupGroupList.setAdapter(popupAdapter);
        }

        popupAdapter.setGroupList(groupList);
        groupPopupWindow.showAtLocation(this.findViewById(R.id.mainWin), Gravity.CENTER | Gravity.CENTER, 0, 0);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
