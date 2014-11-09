package com.origin.aiur.activity.group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.origin.aiur.R;
import com.origin.aiur.vo.UserGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/10/25.
 */
public class SearchGroupAdapter extends BaseAdapter {
    private ArrayList<UserGroup> activityArrayList = new ArrayList<UserGroup>();
    private Context context;
    private LayoutInflater mInflater;

    public SearchGroupAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setGroupList(List<UserGroup> activityList) {
        activityArrayList.clear();
        if (activityList != null && !activityList.isEmpty()) {
            activityArrayList.addAll(activityList);
        }
        this.notifyDataSetChanged();
    }

    public void updateJoinedGroup(long groupId) {
        if (groupId <= 0) {
            return;
        }
        for (UserGroup group : activityArrayList) {
            if (group.getGroupId() == groupId) {
                group.setRequired(true);
            }
        }
        this.notifyDataSetChanged();
    }

    public void clearGroupItem() {
        activityArrayList.clear();
        this.notifyDataSetChanged();
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
            returnView = mInflater.inflate(R.layout.adapter_search_group, null);
        }
        if (returnView == null) {
            return returnView;
        }

        UserGroup group = activityArrayList.get(i);
        returnView.setTag(group);

        TextView groupName = (TextView) returnView.findViewById(R.id.joinGroupInfo);
        groupName.setText(group.getGroupName());

        TextView groupDesc = (TextView) returnView.findViewById(R.id.joinGroupDesc);
        groupDesc.setText(group.getGroupDesc());

        TextView joinBtn = (TextView) returnView.findViewById(R.id.joinGroupRequest);
        joinBtn.setTag(R.string.tagSearchGroupListJoinButton, group.getGroupId());

        if (group.isRequired()) {
            joinBtn.setEnabled(false);
        } else {
            joinBtn.setEnabled(true);
        }
        return returnView;
    }
}
