package com.origin.aiur.activity.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.origin.aiur.R;
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
    private boolean isGroupActivity;

    public ListActivitiesAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setActivityList(List<GroupEvent> activityList, boolean isGroupActivity) {
        this.isGroupActivity = isGroupActivity;
        if (activityList != null && !activityList.isEmpty()) {
            groupEventList.clear();
            groupEventList.addAll(activityList);
            this.notifyDataSetChanged();
        }
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

        // TODO:
        if (isGroupActivity) {

        } else {

        }
        GroupEvent groupEvent = groupEventList.get(i);
        TextView activityDate = (TextView) returnView.findViewById(R.id.groupActivityDate);
        activityDate.setText(DateUtils.formatDate(groupEvent.getCreateTime()));

        TextView activityDesc = (TextView) returnView.findViewById(R.id.groupActivityDesc);
        activityDesc.setText(groupEvent.getDescription());

        TextView eventStatus = (TextView) returnView.findViewById(R.id.groupActivityStatus);
        eventStatus.setText(groupEvent.getStatus());

        return returnView;
    }
}
