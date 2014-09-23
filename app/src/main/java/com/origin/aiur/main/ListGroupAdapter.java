package com.origin.aiur.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.origin.aiur.R;
import com.origin.aiur.vo.UserGroup;

import java.util.ArrayList;

/**
 * Created by Administrator on 2014/9/24.
 */
public class ListGroupAdapter extends BaseAdapter {
    private ArrayList<UserGroup> activityArrayList = new ArrayList<UserGroup>();
    private Context context;
    private LayoutInflater mInflater;

    public ListGroupAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setGroupList(ArrayList<UserGroup> activityList) {
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
            returnView = mInflater.inflate(R.layout.adapter_main_group, null);
        }
        if (returnView == null) {
            return returnView;
        }

        return returnView;
    }
}
