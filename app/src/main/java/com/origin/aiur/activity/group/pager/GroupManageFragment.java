package com.origin.aiur.activity.group.pager;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.origin.aiur.BaseFragment;
import com.origin.aiur.R;
import com.origin.aiur.activity.group.GroupHelper;
import com.origin.aiur.dao.GroupUserDao;
import com.origin.aiur.dao.RequestEventDao;
import com.origin.aiur.dao.UserDao;
import com.origin.aiur.http.HttpUtils;
import com.origin.aiur.utils.ALogger;
import com.origin.aiur.vo.RequestEvent;
import com.origin.aiur.vo.User;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by dongjia on 11/17/2014.
 */
public class GroupManageFragment extends BaseFragment {
    private Context context;
    private ListView groupUserItemList;
    private ListView userRequestItemList;
    private ManageUserAdapter groupUserAdapter;
    private ManageRequestAdapter userRequestAdapter;
    private View userRequestContainer;

    private enum Actions {
        query_request_event_list, send_manage_finance_event_approve, send_manage_finance_event_reject, send_manage_user_request_approve, send_manage_user_request_reject, send_manage_user_request_remove
    }
    public static GroupManageFragment startFragment(Context context) {
        GroupManageFragment instance = new GroupManageFragment();
        instance.setContext(context);
        return instance;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ALogger.log(ALogger.LogPriority.debug, GroupManageFragment.class, "GroupManageFragment@onCreateView");
        View rootView = inflater.inflate(R.layout.group_tab_manage, container, false);

        groupUserItemList = (ListView) rootView.findViewById(R.id.groupUserItemList);
        groupUserAdapter = new ManageUserAdapter(this.getContext());
        groupUserAdapter.setListener(this);
        groupUserItemList.setAdapter(groupUserAdapter);

        userRequestItemList = (ListView) rootView.findViewById(R.id.userRequestItemList);
        userRequestAdapter = new ManageRequestAdapter(this.getContext());
        userRequestAdapter.setListener(this);
        userRequestItemList.setAdapter(userRequestAdapter);
        userRequestContainer = rootView.findViewById(R.id.userRequestContainer);


        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ALogger.log(ALogger.LogPriority.debug, GroupManageFragment.class, "GroupManageFragment@onCreate");
        long currentGroupId = UserDao.getInstance().getCurrentGroup().getGroupId();
        this.getSync(Actions.query_request_event_list.name(), currentGroupId);
    }

    @Override
    public void onStart() {
        super.onStart();
        ALogger.log(ALogger.LogPriority.debug, GroupManageFragment.class, "GroupManageFragment@onStart");

    }

    @Override
    public void onResume() {
        super.onResume();
        ALogger.log(ALogger.LogPriority.debug, GroupManageFragment.class, "GroupManageFragment@onResume");

        long currentGroupId = UserDao.getInstance().getCurrentGroup().getGroupId();
        List<User> userList = GroupUserDao.getInstance().getGroupUserList(currentGroupId);
        refreshUserList(userList);

        List<RequestEvent> requestList = RequestEventDao.getInstance().getRequestEvents();
        refreshRequestList(requestList);

    }

    @Override
    public void onStop() {
        super.onStop();
        ALogger.log(ALogger.LogPriority.debug, GroupManageFragment.class, "GroupManageFragment@onStop");
    }

    @Override
    public String getTitle() {
        return this.context.getString(R.string.title_section_manage);
    }

    @Override
    public void performClick(View view) {
        User userInfo = null;
        RequestEvent requestEvent = null;
        HashMap<String, Object> param = null;
        long currentGroupId = UserDao.getInstance().getCurrentGroup().getGroupId();

        switch (view.getId()) {
            case R.id.groupUserManageApprove:
                userInfo = (User) view.getTag();

                param = new HashMap<String, Object>();
                param.put("userID", userInfo.getUserID());
                param.put("groupId", userInfo.getGroupId());
                this.postSync(Actions.send_manage_user_request_approve.name(), param, currentGroupId);
                break;
            case R.id.groupUserManageReject:
                userInfo = (User) view.getTag();

                param = new HashMap<String, Object>();
                param.put("userID", userInfo.getUserID());
                param.put("groupId", userInfo.getGroupId());
                this.postSync(Actions.send_manage_user_request_reject.name(), param, currentGroupId);
                break;
            case R.id.groupUserManageRemove:
                userInfo = (User) view.getTag();

                param = new HashMap<String, Object>();
                param.put("userID", userInfo.getUserID());
                param.put("groupId", userInfo.getGroupId());
                this.postSync(Actions.send_manage_user_request_remove.name(), param, currentGroupId);
                break;
            case R.id.userRequestManageApprove:
                requestEvent = (RequestEvent) view.getTag();

                param = new HashMap<String, Object>();
                param.put("eventId", requestEvent.getEventId());
                param.put("eventType", requestEvent.getEventType());
                this.postSync(Actions.send_manage_finance_event_approve.name(), param, currentGroupId);
                break;
            case R.id.userRequestManageReject:
                requestEvent = (RequestEvent) view.getTag();

                param = new HashMap<String, Object>();
                param.put("eventId", requestEvent.getEventId());
                param.put("eventType", requestEvent.getEventType());
                this.postSync(Actions.send_manage_finance_event_reject.name(), param, currentGroupId);
                break;
        }
    }

    @Override
    public void onPostExecuteSuccessful(String action, JSONObject response) {
        switch (Actions.valueOf(action)) {
            case query_request_event_list:
                List<RequestEvent> requestList = GroupHelper.getInstance().getRequestEventList(response);
                refreshRequestList(requestList);
                break;
            case send_manage_finance_event_approve:
            case send_manage_finance_event_reject:
                List<RequestEvent> financeList = GroupHelper.getInstance().getRequestEventList(response);
                refreshRequestList(financeList);
                this.showToastMessage(this.getContext().getString(R.string.common_group_manage_finance));
                break;
            case send_manage_user_request_approve:
            case send_manage_user_request_reject:
            case send_manage_user_request_remove:
                List<User> userList = GroupHelper.getInstance().getGroupUserList(response);
                refreshUserList(userList);
                this.showToastMessage(this.getContext().getString(R.string.common_group_manage_user));
                break;

        }
    }

    @Override
    public void onPostExecuteFailed(String action) {
    }

    @Override
    public String getPath(String action, Object... args) {
        String path = null;
        switch (Actions.valueOf(action)) {
            case query_request_event_list:
                path = HttpUtils.buildPath(HttpUtils.load_user_request_event, args);
                break;
            case send_manage_finance_event_approve:
                path = HttpUtils.buildPath(HttpUtils.send_manage_finance_event_approve, args);
                break;
            case send_manage_finance_event_reject:
                path = HttpUtils.buildPath(HttpUtils.send_manage_finance_event_reject, args);
                break;
            case send_manage_user_request_approve:
                path = HttpUtils.buildPath(HttpUtils.send_manage_user_request_approve, args);
                break;
            case send_manage_user_request_reject:
                path = HttpUtils.buildPath(HttpUtils.send_manage_user_request_reject, args);
                break;
            case send_manage_user_request_remove:
                path = HttpUtils.buildPath(HttpUtils.send_manage_user_request_remove, args);
                break;
        }
        return path;
    }

    private void refreshUserList(List<User> userList) {
        if (groupUserAdapter != null) {
            groupUserAdapter.setUserList(userList);
        }
    }

    private void refreshRequestList(List<RequestEvent> requestList) {
        if (userRequestAdapter != null) {
            userRequestAdapter.setRequestList(requestList);
        }

        if (requestList == null || requestList.isEmpty()) {
            userRequestContainer.setVisibility(View.GONE);
        }else {
            userRequestContainer.setVisibility(View.VISIBLE);
        }
    }
}
