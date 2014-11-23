package com.origin.aiur.activity.group.pager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.origin.aiur.R;
import com.origin.aiur.utils.DateUtils;
import com.origin.aiur.vo.RequestEvent;
import com.origin.aiur.vo.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/9/23.
 */
public class ManageRequestAdapter extends BaseAdapter {
    private List<RequestEvent> userList = new ArrayList<RequestEvent>();
    private Context context;
    private LayoutInflater mInflater;
    private GroupManageFragment listener;

    public ManageRequestAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }
    public void setListener(GroupManageFragment baseFragment) {
        this.listener = baseFragment;
    }
    public void setRequestList(List<RequestEvent> activityList) {
        userList.clear();

        if (activityList != null && !activityList.isEmpty()) {
            userList.addAll(activityList);
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int i) {
        return userList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View returnView = view;
        if (returnView == null) {
            returnView = mInflater.inflate(R.layout.adapter_manage_request, null);
        }

        if (returnView == null) {
            return returnView;
        }

        RequestEvent userInfo = userList.get(i);

        TextView tabManageRequestItemDate = (TextView)returnView.findViewById(R.id.tabManageRequestItemDate);
        tabManageRequestItemDate.setText(DateUtils.formatDate(userInfo.getCreateTime(), "MM/dd HH:mm"));

        String itemText = null;
        if ("CONSUME_EVENT".equalsIgnoreCase(userInfo.getEventType())) {
            itemText = this.context.getString(R.string.msg_manage_group_consume, userInfo.getMoney(), userInfo.getEventUser(), userInfo.getDescription());
        } else {
            // INCOME_EVENT
            itemText = this.context.getString(R.string.msg_manage_user_incoming, userInfo.getMoney(), userInfo.getEventUser());
        }
        TextView requestManageText = (TextView)returnView.findViewById(R.id.requestManageText);
        if (itemText != null) {
            requestManageText.setText(itemText);
        } else {
            requestManageText.setText("");
        }

        TextView groupUserManageApprove = (TextView)returnView.findViewById(R.id.userRequestManageApprove);
        groupUserManageApprove.setOnClickListener(this.listener);
        groupUserManageApprove.setTag(userInfo);

        TextView groupUserManageReject = (TextView)returnView.findViewById(R.id.userRequestManageReject);
        groupUserManageReject.setOnClickListener(this.listener);
        groupUserManageReject.setTag(userInfo);

        return returnView;
    }
}
