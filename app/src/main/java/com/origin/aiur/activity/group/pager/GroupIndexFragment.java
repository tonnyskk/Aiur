package com.origin.aiur.activity.group.pager;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.origin.aiur.BaseFragment;
import com.origin.aiur.R;
import com.origin.aiur.activity.group.GroupHelper;
import com.origin.aiur.activity.main.ListActivitiesAdapter;
import com.origin.aiur.dao.FinanceDao;
import com.origin.aiur.dao.GroupEventDao;
import com.origin.aiur.dao.GroupUserDao;
import com.origin.aiur.dao.UserDao;
import com.origin.aiur.http.HttpUtils;
import com.origin.aiur.utils.ALogger;
import com.origin.aiur.utils.AppUtils;
import com.origin.aiur.vo.Finance;
import com.origin.aiur.vo.GroupEvent;
import com.origin.aiur.vo.User;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by dongjia on 11/17/2014.
 */
public class GroupIndexFragment extends BaseFragment {
    private Context context;
    private TextView groupPrepayLeft;
    private TextView groupFunnyText;
    private ListView groupActivityList;
    private ListActivitiesAdapter groupActivityAdapter;
    private View tabBtnPrepay;
    private View tabBtnCharge;
    private GridView listUserPrepayList;
    private UserAvatarAdapter userAdapter;

    private enum Actions{
        load_user_finance_by_group, load_group_activity, load_group_user
    }

    public static GroupIndexFragment startFragment(Context context) {
        GroupIndexFragment instance = new GroupIndexFragment();
        instance.setContext(context);
        return instance;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ALogger.log(ALogger.LogPriority.debug, GroupIndexFragment.class, "GroupIndexFragment@onCreate");
        long currentGroupId = UserDao.getInstance().getCurrentGroup().getGroupId();
        long currentUserId = UserDao.getInstance().getCurrentUser().getUserID();

        this.getSync(Actions.load_user_finance_by_group.name(), currentUserId, currentGroupId);
        this.getSync(Actions.load_group_activity.name(), currentUserId, currentGroupId);
        this.getSync(Actions.load_group_user.name(), currentGroupId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ALogger.log(ALogger.LogPriority.debug, GroupIndexFragment.class, "GroupIndexFragment@onCreateView");
        View rootView = inflater.inflate(R.layout.group_tab_index, container, false);

        tabBtnPrepay = rootView.findViewById(R.id.tabBtnPrepay);
        tabBtnPrepay.setOnClickListener(this);
        tabBtnCharge = rootView.findViewById(R.id.tabBtnCharge);
        tabBtnCharge.setOnClickListener(this);

        groupPrepayLeft = (TextView)rootView.findViewById(R.id.groupPrepayLeft);
        groupFunnyText =  (TextView)rootView.findViewById(R.id.groupFunnyText);

        groupActivityList = (ListView)rootView.findViewById(R.id.listUserGroupActivity);
        groupActivityAdapter = new ListActivitiesAdapter(this.getActivity());
        groupActivityList.setAdapter(groupActivityAdapter);

        listUserPrepayList = (GridView)rootView.findViewById(R.id.listUserPrepayList);
        userAdapter = new UserAvatarAdapter(this.getContext());
        listUserPrepayList.setAdapter(userAdapter);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        ALogger.log(ALogger.LogPriority.debug, GroupIndexFragment.class, "GroupIndexFragment@onStart");

    }

    @Override
    public void onResume() {
        super.onResume();
        ALogger.log(ALogger.LogPriority.debug, GroupIndexFragment.class, "GroupIndexFragment@onResume");

        // Refresh Values
        List<GroupEvent> groupEventList = GroupEventDao.getInstance().getGroupEvents();
        refreshGroupEvent(groupEventList);

        Finance finance = FinanceDao.getInstance().getGroupFinance();
        refreshFinance(finance);

        long currentGroupId = UserDao.getInstance().getCurrentGroup().getGroupId();
        List<User> userList = GroupUserDao.getInstance().getGroupUserList(currentGroupId);
        refreshUserList(userList);
    }

    @Override
    public void onStop() {
        super.onStop();
        ALogger.log(ALogger.LogPriority.debug, GroupIndexFragment.class, "GroupIndexFragment@onStop");
    }

    @Override
    public String getTitle() {
        return this.context.getString(R.string.title_section_index);
    }

    @Override
    public void performClick(View view) {
        switch (view.getId()) {
            case R.id.tabBtnPrepay:
                GroupHelper.getInstance().notifyChangeTab(2);
                break;
            case R.id.tabBtnCharge:
                GroupHelper.getInstance().notifyChangeTab(3);
                break;
        }
    }

    @Override
    public void onPostExecuteSuccessful(String action, JSONObject response) {
        switch (Actions.valueOf(action)) {
             case load_user_finance_by_group:
                 Finance finance = GroupHelper.getInstance().getFinanceInfo(response);
                 refreshFinance(finance);
                 break;
             case load_group_activity:
                 List<GroupEvent> groupEventList = GroupHelper.getInstance().getGroupEventList(response);
                 refreshGroupEvent(groupEventList);
                 break;
            case load_group_user:
                List<User> userList = GroupHelper.getInstance().getGroupUserList(response);
                refreshUserList(userList);
                break;
        }
    }

    @Override
    public void onPostExecuteFailed(String action) {
        switch (Actions.valueOf(action)) {
            case load_user_finance_by_group:
                List<GroupEvent> groupEventList = GroupEventDao.getInstance().getGroupEvents();
                refreshGroupEvent(groupEventList);
                break;
            case load_group_activity:
                Finance finance = FinanceDao.getInstance().getGroupFinance();
                refreshFinance(finance);
                break;
            case load_group_user:
                long currentGroupId = UserDao.getInstance().getCurrentGroup().getGroupId();
                List<User> userList = GroupUserDao.getInstance().getGroupUserList(currentGroupId);
                refreshUserList(userList);
                break;
        }
    }

    @Override
    public String getPath(String action, Object... args) {
        String path = null;
        switch (Actions.valueOf(action)) {
            case load_user_finance_by_group:
                path = HttpUtils.buildPath(HttpUtils.load_user_finance_by_group, args);
                break;
            case load_group_activity:
                path = HttpUtils.buildPath(HttpUtils.load_group_activity, args);
                break;
            case load_group_user:
                path = HttpUtils.buildPath(HttpUtils.load_group_users, args);
                break;
        }
        return path;
    }


    private void refreshFinance(Finance finance) {
        String funnyText = null;
        if (finance != null) {
            if (groupPrepayLeft != null) {
                groupPrepayLeft.setText(AppUtils.formatMoney(finance.getIncomingSummary() - finance.getConsumeSummary()));
            }
            funnyText = getFunnyText(finance.getIncomingSummary() - finance.getConsumeSummary());
            //txtTotalCost.setText(MainHelper.getInstance().formatMoney(finance.getConsumeSummary()));
            //txtTotalBalance.setText(MainHelper.getInstance().formatMoney(finance.getIncomingSummary() - finance.getConsumeSummary()));
        }

        if (groupFunnyText != null) {
            if (funnyText != null) {
                groupFunnyText.setText(funnyText);
                groupFunnyText.setVisibility(View.VISIBLE);
            } else {
                groupFunnyText.setVisibility(View.GONE);
            }
        }

    }

    private void refreshGroupEvent(List<GroupEvent> groupEventList) {
        if (groupEventList != null && groupActivityAdapter != null) {
            groupActivityAdapter.setActivityList(groupEventList);
        }
    }

    private void refreshUserList(List<User> userList) {
        if (userList != null && userAdapter != null) {
            userAdapter.setUserList(userList);
        }
    }

    private String getFunnyText(double money) {
        String displayText = null;
        if (money <= 0) {
            displayText = getContext().getString(R.string.msg_index_funny_1);
        } else if (money <= 20) {
            displayText = getContext().getString(R.string.msg_index_funny_2);
        } else if (money <= 50) {
            displayText = getContext().getString(R.string.msg_index_funny_3);
        } else if (money <= 100) {
            displayText = getContext().getString(R.string.msg_index_funny_4);
        } else if (money <= 200) {
            displayText = getContext().getString(R.string.msg_index_funny_5);
        } else {
            displayText = getContext().getString(R.string.msg_index_funny_6);
        }
        return displayText;
    }
}
