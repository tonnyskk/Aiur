package com.origin.aiur.activity.group.pager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.origin.aiur.BaseFragment;
import com.origin.aiur.R;
import com.origin.aiur.activity.group.GroupHelper;
import com.origin.aiur.activity.main.ListActivitiesAdapter;
import com.origin.aiur.activity.main.MainHelper;
import com.origin.aiur.dao.FinanceDao;
import com.origin.aiur.dao.UserDao;
import com.origin.aiur.dao.UserEventDao;
import com.origin.aiur.http.HttpUtils;
import com.origin.aiur.utils.ALogger;
import com.origin.aiur.vo.Finance;
import com.origin.aiur.vo.GroupEvent;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by dongjia on 11/17/2014.
 */
@SuppressLint("ValidFragment")
public class GroupIndexFragment extends BaseFragment {
    private Context context;
    private TextView groupPrepayLeft;
    private ListView groupActivityList;
    private ListActivitiesAdapter groupActivityAdapter;

    private enum Actions{
        load_user_finance_by_group, load_group_activity
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ALogger.log(ALogger.LogPriority.debug, GroupIndexFragment.class, "GroupIndexFragment@onCreateView");
        View rootView = inflater.inflate(R.layout.group_tab_index, container, false);

        groupPrepayLeft = (TextView)rootView.findViewById(R.id.groupPrepayLeft);
        groupActivityList = (ListView)rootView.findViewById(R.id.listUserGroupActivity);
        groupActivityAdapter = new ListActivitiesAdapter(this.getActivity());
        groupActivityList.setAdapter(groupActivityAdapter);

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
    public void onPostExecuteSuccessful(String action, JSONObject response) {
        switch (Actions.valueOf(action)) {
             case load_user_finance_by_group:
                 List<GroupEvent> groupEventList = GroupHelper.getInstance().getGroupEventList(response);
                 refreshGroupEvent(groupEventList);
                 break;
             case load_group_activity:
                 Finance finance = GroupHelper.getInstance().getFinanceInfo(response);
                 refreshFinance(finance);
                 break;
        }
    }

    @Override
    public void onPostExecuteFailed(String action) {
        switch (Actions.valueOf(action)) {
            case load_user_finance_by_group:

                break;
            case load_group_activity:
                Finance finance = FinanceDao.getInstance().getGroupFinance();
                refreshFinance(finance);
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
        }
        return path;
    }


    private void refreshFinance(Finance finance) {
        if (finance != null) {
            if (groupPrepayLeft != null) {
                groupPrepayLeft.setText(MainHelper.getInstance().formatMoney(finance.getIncomingSummary() - finance.getConsumeSummary()));
            }
            //txtTotalCost.setText(MainHelper.getInstance().formatMoney(finance.getConsumeSummary()));
            //txtTotalBalance.setText(MainHelper.getInstance().formatMoney(finance.getIncomingSummary() - finance.getConsumeSummary()));
        }
    }

    private void refreshGroupEvent(List<GroupEvent> groupEventList) {
        if (groupEventList != null && !groupEventList.isEmpty() && groupActivityAdapter != null) {
            groupActivityAdapter.setActivityList(groupEventList, true);
        }
    }
}
