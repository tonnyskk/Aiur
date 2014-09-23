package com.origin.aiur.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.origin.aiur.R;
import com.origin.aiur.utils.DateUtils;
import com.origin.aiur.vo.GroupActivity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2014/9/23.
 */
public class ListActivitiesAdapter extends BaseAdapter {
    private ArrayList<GroupActivity> activityArrayList = new ArrayList<GroupActivity>();
    private Context context;
    private LayoutInflater mInflater;

    public ListActivitiesAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setActivityList(ArrayList<GroupActivity> activityList) {
        if (activityList != null && !activityList.isEmpty()) {
            activityArrayList.clear();
            activityArrayList.addAll(activityList);
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return activityArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return activityArrayList.get(i);
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
        GroupActivity activity = activityArrayList.get(i);
        TextView activityDate = (TextView) returnView.findViewById(R.id.groupActivityDate);
        activityDate.setText(DateUtils.formatDate(activity.getActivityTimestamp()));
        TextView activityDesc = (TextView) returnView.findViewById(R.id.groupActivityDate);
        activityDesc.setText(activity.getActivityDesc());

        return returnView;
    }
}
