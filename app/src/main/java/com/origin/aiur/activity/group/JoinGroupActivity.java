package com.origin.aiur.activity.group;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.origin.aiur.BaseActivity;
import com.origin.aiur.R;
import com.origin.aiur.dao.UserDao;
import com.origin.aiur.http.HttpUtils;
import com.origin.aiur.utils.ALogger;
import com.origin.aiur.utils.AppUtils;
import com.origin.aiur.vo.UserGroup;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

public class JoinGroupActivity extends BaseActivity implements SearchView.OnQueryTextListener, View.OnClickListener {

    private SearchView searchView;
    private ListView listView;
    private SearchGroupAdapter listAdapter;


    enum Actions {
        search_group, join_group
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, JoinGroupActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        initSearchActionBar();

        listView = (ListView) findViewById(R.id.searchGroupItemList);
        listAdapter = new SearchGroupAdapter(this);
        listView.setAdapter(listAdapter);

    }

    private void initSearchActionBar() {
        ActionBar bar = getActionBar();
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customActionBarView = inflater.inflate(R.layout.action_bar_join_group, null);
        searchView = (SearchView) customActionBarView.findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(true);
        searchView.setIconified(false);
        searchView.setBackgroundColor(getResources().getColor(R.color.commonBgColor));

        if (Build.VERSION.SDK_INT >= 14) {
            // when edittest is empty, don't show cancal button
            searchView.onActionViewExpanded();
        }

        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(false);

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        bar.setCustomView(customActionBarView, params);

        // show keyboard
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }

    @Override
    protected void onPostExecuteSuccessful(String action, JSONObject response) {
        switch (Actions.valueOf(action)) {
            case search_group:
                // parse response to update local groupLit
                List<UserGroup> groupList = GroupHelper.getInstance().getSearchGroupList(response);
                listAdapter.setGroupList(groupList);
                break;
            case join_group:
                long joinedGroupId = GroupHelper.getInstance().getJoinedGroupId(response);
                listAdapter.updateJoinedGroup(joinedGroupId);
                String message = joinedGroupId > 0 ? this.getString(R.string.common_add_group_req) :  this.getString(R.string.warn_add_group_req);
                showToastMessage(message);
                break;
        }
    }

    @Override
    protected void onPostExecuteFailed(String action) {
        switch (Actions.valueOf(action)) {
            case search_group:
                listAdapter.setGroupList(null);
                break;
        }
    }

    @Override
    protected String getPath(String action, Object... args) {
        String path = null;
        switch (Actions.valueOf(action)) {
            case search_group:
                path = HttpUtils.buildPath(HttpUtils.search_group, args);
                break;
            case join_group:
                path = HttpUtils.buildPath(HttpUtils.join_group, args);
        }
        return path;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        if (AppUtils.isEmpty(s)) {
            return false;
        }

        String encodeQueryText = s;
        try {
            encodeQueryText = URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            ALogger.log(ALogger.LogPriority.warn, JoinGroupActivity.class, "Encode URL exception %s", s);
        }

        this.getSync(Actions.search_group.name(), UserDao.getInstance().getUserId(), encodeQueryText);
        this.showProcessDialog();

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // listView.setTextFilterEnabled(true);
        // istView.setFilterText(newText.toString());
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.joinGroupRequest:
                long groupId = (Long)view.getTag(R.string.tagSearchGroupListJoinButton);
                this.postSync(Actions.join_group.name(), null, UserDao.getInstance().getUserId(), groupId);
                break;
        }
    }
}
