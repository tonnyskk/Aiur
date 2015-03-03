package com.origin.aiur.activity.main;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.origin.aiur.BaseActivity;
import com.origin.aiur.R;
import com.origin.aiur.activity.group.GroupTabActivity;
import com.origin.aiur.activity.group.JoinGroupActivity;
import com.origin.aiur.activity.group.NewGroupActivity;
import com.origin.aiur.activity.profile.ProfileActivity;
import com.origin.aiur.dao.FinanceDao;
import com.origin.aiur.dao.UserEventDao;
import com.origin.aiur.dao.UserDao;
import com.origin.aiur.http.HttpUtils;
import com.origin.aiur.utils.AppUtils;
import com.origin.aiur.vo.Finance;
import com.origin.aiur.vo.GroupEvent;
import com.origin.aiur.vo.UserGroup;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private TextView btnGotoGroup;
    private TextView btnChangeGroup;
    private TextView btnCreateGroup;
    private TextView btnJoinGroup;

    private TextView txtTotalBalance;
    private TextView txtTotalCost;

    private View userGroupInfoContainer;
    private View userGroupModifyContainer;
    private View btnGroupSplitLine;

    private ListView groupActivityList;
    private ListActivitiesAdapter groupActivityAdapter;
    private PopupWindow groupPopupWindow;

    private ListGroupAdapter popupAdapter = null;
    private ProgressBar loadGroupActivity;
    private ProgressBar loadUserGroupList;
    private View userGroupListContainer;

    public MainActivity() {
    }

    enum Actions {
        load_user_group, load_group_activity, load_user_finance
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);

        btnGotoGroup = (TextView) findViewById(R.id.btnGotoGroup);
        btnGotoGroup.setOnClickListener(this);

        btnChangeGroup = (TextView) findViewById(R.id.btnChangeGroup);
        btnChangeGroup.setOnClickListener(this);

        btnCreateGroup = (TextView) findViewById(R.id.btnCreateGroup);
        btnCreateGroup.setOnClickListener(this);

        btnJoinGroup = (TextView) findViewById(R.id.btnJoinGroup);
        btnJoinGroup.setOnClickListener(this);

        btnGroupSplitLine = findViewById(R.id.btnGroupSplitLine);

        userGroupInfoContainer = findViewById(R.id.userGroupInfoContainer);
        userGroupModifyContainer = findViewById(R.id.userGroupModifyContainer);

        txtTotalBalance = (TextView) findViewById(R.id.txtTotalBalance);
        txtTotalCost = (TextView) findViewById(R.id.txtTotalCost);

        groupActivityList = (ListView) findViewById(R.id.listGroupActivity);
        groupActivityAdapter = new ListActivitiesAdapter(this);
        groupActivityList.setAdapter(groupActivityAdapter);

        userGroupListContainer = findViewById(R.id.userGroupListContainer);
        loadGroupActivity = (ProgressBar) findViewById(R.id.loadGroupActivity);
        loadUserGroupList = (ProgressBar) findViewById(R.id.loadUserGroupList);
    }


    @Override
    protected void onResume() {
        super.onResume();

        UserGroup currentGroup = UserDao.getInstance().getCurrentGroup();

        if (currentGroup != null) {
            String txt = getResources().getString(R.string.main_group_select, currentGroup.getGroupName());
            btnGotoGroup.setVisibility(View.VISIBLE);
            btnGotoGroup.setText(txt);
        } else {
            btnGotoGroup.setVisibility(View.GONE);
        }

        this.getSync(Actions.load_user_group.name());
        this.getSync(Actions.load_group_activity.name());
        this.getSync(Actions.load_user_finance.name());

        loadGroupActivity.setVisibility(View.VISIBLE);
        groupActivityList.setVisibility(View.GONE);

        loadUserGroupList.setVisibility(View.VISIBLE);
        userGroupListContainer.setVisibility(View.GONE);
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
            NewGroupActivity.startActivity(this);
            return true;
        } else if (id == R.id.menuGroupJoin) {
            JoinGroupActivity.startActivity(this);
            return true;
        } else if (id == R.id.menuGroupProfile) {
            ProfileActivity.startActivity(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostExecuteSuccessful(String action, JSONObject response) {
        switch (Actions.valueOf(action)) {
            case load_group_activity:
                List<GroupEvent> groupEventList = MainHelper.getInstance().getGroupEventList(response);
                refreshGroupEvent(groupEventList);

                loadGroupActivity.setVisibility(View.GONE);
                groupActivityList.setVisibility(View.VISIBLE);
                break;

            case load_user_group:
                List<UserGroup> userGroupList = MainHelper.getInstance().getGroupList(response);
                refreshUserGroup(userGroupList);

                loadUserGroupList.setVisibility(View.GONE);
                userGroupListContainer.setVisibility(View.VISIBLE);
                break;
            case load_user_finance:
                Finance finance = MainHelper.getInstance().getFinanceInfo(response);
                refreshFinance(finance);
                break;
        }
    }

    @Override
    public void onPostExecuteFailed(String action) {
        // Use offline data to generate page if exist
        switch (Actions.valueOf(action)) {
            case load_group_activity:
                List<GroupEvent> groupEventList = UserEventDao.getInstance().getUserEvents();
                refreshGroupEvent(groupEventList);

                loadGroupActivity.setVisibility(View.GONE);
                groupActivityList.setVisibility(View.VISIBLE);
                break;

            case load_user_group:
                List<UserGroup> userGroupList = UserDao.getInstance().getUserGroupList();
                refreshUserGroup(userGroupList);

                loadUserGroupList.setVisibility(View.GONE);
                userGroupListContainer.setVisibility(View.VISIBLE);
                break;
            case load_user_finance:
                Finance finance = FinanceDao.getInstance().getUserFinance();
                refreshFinance(finance);
                break;
        }
    }

    private void refreshGroupEvent(List<GroupEvent> groupEventList) {
        groupActivityAdapter.setActivityList(groupEventList);
    }

    private void refreshUserGroup(List<UserGroup> userGroupList) {
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
                btnGroupSplitLine.setVisibility(View.GONE);
            } else {
                btnChangeGroup.setVisibility(View.VISIBLE);
                btnGroupSplitLine.setVisibility(View.VISIBLE);
            }

            UserGroup currentGroup = UserDao.getInstance().getCurrentGroup();
            if (currentGroup == null) {
                UserGroup defaultGroup = userGroupList.get(0);
                UserDao.getInstance().setCurrentGroup(defaultGroup);
                // set userGroupList.get(0) as selected group, and save into DAO
                String txt = getResources().getString(R.string.main_group_select, defaultGroup.getGroupName());
                btnGotoGroup.setText(txt);
            }
            btnGotoGroup.setVisibility(View.VISIBLE);
        }
    }

    private void refreshFinance(Finance finance) {
        if (finance != null) {
            txtTotalCost.setText(AppUtils.formatMoney(finance.getConsumeSummary()));
            txtTotalBalance.setText(AppUtils.formatMoney(finance.getIncomingSummary() - finance.getConsumeSummary()));
        }
    }

    @Override
    public String getPath(String action, Object... args) {
        String path = null;
        switch (Actions.valueOf(action)) {
            case load_user_group:
                path = HttpUtils.buildPath(HttpUtils.load_user_group, UserDao.getInstance().getUserId());
                break;
            case load_group_activity:
                path = HttpUtils.buildPath(HttpUtils.load_user_activity, UserDao.getInstance().getUserId());
                break;
            case load_user_finance:
                path = HttpUtils.buildPath(HttpUtils.load_user_finance, UserDao.getInstance().getUserId());
                break;
        }
        return path;
    }

    @Override
    public void performClick(View view) {
        if (view == null) {
            return;
        }

        switch (view.getId()) {
            case R.id.btnChangeGroup:
                List<UserGroup> userGroupList = UserDao.getInstance().getUserGroupList();
                if (userGroupList != null && !userGroupList.isEmpty()) {
                    UserGroup currentGroup = UserDao.getInstance().getCurrentGroup();
                    if (currentGroup != null) {
                        ArrayList<UserGroup> filterList = new ArrayList<UserGroup>();
                        for (UserGroup userGroup : userGroupList) {
                            if (!userGroup.getGroupName().equalsIgnoreCase(currentGroup.getGroupName())) {
                                filterList.add(userGroup);
                            }
                        }
                        showPopupWindow(view, filterList);
                    } else {
                        showPopupWindow(view, userGroupList);
                    }
                }
                break;
            case R.id.btnGotoGroup:
                GroupTabActivity.startActivity(this, UserDao.getInstance().getCurrentGroup());
                break;
            case R.id.btnCreateGroup:
                NewGroupActivity.startActivity(this);
                break;

            case R.id.btnJoinGroup:
                JoinGroupActivity.startActivity(this);
                break;
        }
    }

    private void showPopupWindow(View parent, List<UserGroup> groupList) {

        if (groupPopupWindow == null) {
            LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View topWindow = mLayoutInflater.inflate(R.layout.popup_main_groups, null);

            ListView popupGroupList = (ListView) topWindow.findViewById(R.id.popupGroupList);
            popupAdapter = new ListGroupAdapter(this);
            popupGroupList.setAdapter(popupAdapter);

            groupPopupWindow = new PopupWindow(topWindow);
            groupPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_corner_bg));
            groupPopupWindow.setOutsideTouchable(true);
            groupPopupWindow.setTouchable(true);
            groupPopupWindow.setFocusable(true);
            groupPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
            groupPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            groupPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupGroupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    UserGroup group = (UserGroup) view.getTag();
                    UserDao.getInstance().setCurrentGroup(group);
                    GroupTabActivity.startActivity(MainActivity.this, group);
                    dismissPopup();
                }
            });
        }

        popupAdapter.setGroupList(groupList);
        groupPopupWindow.showAtLocation(parent, Gravity.CENTER_VERTICAL, 0, 0);


    }

    @Override
    protected void onStop() {
        super.onStop();
        dismissPopup();
    }

    private void dismissPopup() {
        if (groupPopupWindow != null) {
            groupPopupWindow.dismiss();
            groupPopupWindow = null;
        }

    }
}
