package com.origin.aiur.activity.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.origin.aiur.R;
import com.origin.aiur.utils.AppUtils;
import com.origin.aiur.utils.DateUtils;
import com.origin.aiur.vo.GroupEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/9/23.
 */
public class ListActivitiesAdapter extends BaseAdapter {
    private List<GroupEvent> groupEventList = new ArrayList<GroupEvent>();
    private Context context;
    private LayoutInflater mInflater;

    public ListActivitiesAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setActivityList(List<GroupEvent> activityList) {
        groupEventList.clear();
        if (activityList != null && !activityList.isEmpty()) {
            groupEventList.addAll(activityList);
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return groupEventList.size();
    }

    @Override
    public Object getItem(int i) {
        return groupEventList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View returnView = view;
        if (returnView == null) {
            returnView = mInflater.inflate(R.layout.adapter_main_activity, null);
        }

        if (returnView == null) {
            return returnView;
        }

        GroupEvent groupEvent = groupEventList.get(i);

        TextView activityDate = (TextView) returnView.findViewById(R.id.groupActivityDate);
        activityDate.setText(DateUtils.formatDate(groupEvent.getCreateTime(), "MM/dd HH:mm"));

        String type = groupEvent.getType();

        TextView activityDesc = (TextView) returnView.findViewById(R.id.groupActivityDesc);
        TextView eventStatus = (TextView) returnView.findViewById(R.id.groupActivityStatus);
        eventStatus.setVisibility(View.VISIBLE);

        if ("GROUP".equalsIgnoreCase(type)) {
            // Is Activity in GroupIndexFragment: User Join or Leave group
            eventStatus.setVisibility(View.GONE);
            String displayText = null;
            if ("JOINED".equalsIgnoreCase(groupEvent.getStatus())) {
                displayText = this.context.getString(R.string.msg_index_join_group, groupEvent.getDescription());
            } else if ("PENDING".equalsIgnoreCase(groupEvent.getStatus())) {
                displayText = this.context.getString(R.string.msg_index_join_group_req, groupEvent.getDescription());
            } else if ("LEFT".equalsIgnoreCase(groupEvent.getStatus())) {
                displayText = this.context.getString(R.string.msg_index_join_group_left, groupEvent.getDescription());
            }
            activityDesc.setText(displayText);
        } else if ("CONSUME".equalsIgnoreCase(type)) {
            // Is Activity in GroupIndexFragment: User consume details
            String userConsume = AppUtils.formatMoney(groupEvent.getUserConsume());
            String groupConsume = AppUtils.formatMoney(groupEvent.getGroupConsume());

            if ("INVALID".equalsIgnoreCase(groupEvent.getStatus())) {
                activityDesc.setText(this.context.getString(R.string.msg_index_consume_invalid, groupConsume));
                eventStatus.setVisibility(View.GONE);
            } else {
                String displayText = this.context.getString(R.string.msg_index_group_consume, groupConsume, userConsume, groupEvent.getDescription());
                activityDesc.setText(displayText);

                if ("PENDING".equalsIgnoreCase(groupEvent.getStatus())) {
                    eventStatus.setText(this.context.getString(R.string.msg_index_consume_pending));
                } else {
                    eventStatus.setVisibility(View.GONE);
                }
            }
        } else if ("INCOMING".equalsIgnoreCase(type)) {
            String incoming = AppUtils.formatMoney(groupEvent.getUserConsume());
            String displayText = this.context.getString(R.string.msg_index_group_incoming, groupEvent.getDescription(), incoming);
            activityDesc.setText(displayText);

            if ("PENDING".equalsIgnoreCase(groupEvent.getStatus())) {
                eventStatus.setText(this.context.getString(R.string.msg_index_incoming_pending));
            } else {
                eventStatus.setVisibility(View.GONE);
            }
        } else {
            // Is Activity Item in MainActivity
            String groupConsume = AppUtils.formatMoney(groupEvent.getGroupConsume());
            String displayText = this.context.getString(R.string.msg_main_group_consume, groupEvent.getGroupName(), groupConsume, groupEvent.getDescription());
            activityDesc.setText(displayText);

            if ("PENDING".equalsIgnoreCase(groupEvent.getStatus())) {
                eventStatus.setText(this.context.getString(R.string.msg_index_consume_pending));
            } else {
                eventStatus.setVisibility(View.GONE);
            }
        }
        return returnView;
    }
}
