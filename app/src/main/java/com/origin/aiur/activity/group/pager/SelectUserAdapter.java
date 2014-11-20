package com.origin.aiur.activity.group.pager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.origin.aiur.BaseFragment;
import com.origin.aiur.R;
import com.origin.aiur.vo.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/9/23.
 */
public class SelectUserAdapter extends BaseAdapter {
    private List<User> userList = new ArrayList<User>();
    private List<Long> checkedIdList = new ArrayList<Long>();
    private Context context;
    private LayoutInflater mInflater;
    private GroupChargeFragment listener;

    public SelectUserAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setListener(GroupChargeFragment baseFragment) {
        this.listener = baseFragment;
    }

    public void setUserList(List<User> activityList) {
        userList.clear();

        if (activityList != null && !activityList.isEmpty()) {
            userList.addAll(activityList);
        }
        this.notifyDataSetChanged();
    }

    public void updateCheckedList(List<Long> checkedUserList) {
        checkedIdList.clear();

        if (checkedUserList != null && !checkedUserList.isEmpty()) {
            checkedIdList.addAll(checkedUserList);
        }
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
            returnView = mInflater.inflate(R.layout.adapter_user_selection, null);
        }

        if (returnView == null) {
            return returnView;
        }

        User userInfo = userList.get(i);

        TextView userName = (TextView) returnView.findViewById(R.id.userNameSelection);
        userName.setText(userInfo.getNickName());

        CheckBox checkBox = (CheckBox) returnView.findViewById(R.id.userCheckboxSelection);
        checkBox.setOnCheckedChangeListener(listener);
        checkBox.setTag(Long.valueOf(userInfo.getUserID()));

        if (checkedIdList.contains(Long.valueOf(userInfo.getUserID()))) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }

        return returnView;
    }
}
