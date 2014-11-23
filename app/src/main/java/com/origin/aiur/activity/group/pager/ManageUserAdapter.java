package com.origin.aiur.activity.group.pager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.origin.aiur.R;
import com.origin.aiur.dao.UserDao;
import com.origin.aiur.vo.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/9/23.
 */
public class ManageUserAdapter extends BaseAdapter {
    private List<User> userList = new ArrayList<User>();
    private Context context;
    private LayoutInflater mInflater;
    private GroupManageFragment listener;

    public ManageUserAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }
    public void setListener(GroupManageFragment baseFragment) {
        this.listener = baseFragment;
    }
    public void setUserList(List<User> activityList) {
        userList.clear();

        if (activityList != null && !activityList.isEmpty()) {
            long ownerId = UserDao.getInstance().getCurrentGroup().getOwnerUserId();
            for(User user : activityList) {
                if (user.getUserID() != ownerId) {
                    userList.add(user);
                }
            }
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
            returnView = mInflater.inflate(R.layout.adapter_manage_user, null);
        }

        if (returnView == null) {
            return returnView;
        }

        User userInfo = userList.get(i);

        TextView itemButtonApprove = (TextView) returnView.findViewById(R.id.groupUserManageApprove);
        itemButtonApprove.setTag(userInfo);
        itemButtonApprove.setOnClickListener(listener);

        TextView itemButtonReject = (TextView) returnView.findViewById(R.id.groupUserManageReject);
        itemButtonReject.setTag(userInfo);
        itemButtonReject.setOnClickListener(listener);
        TextView itemButtonRemove = (TextView) returnView.findViewById(R.id.groupUserManageRemove);
        itemButtonRemove.setTag(userInfo);
        itemButtonRemove.setOnClickListener(listener);

        TextView userManageText = (TextView) returnView.findViewById(R.id.groupUserManageText);
        if ("PENDING".equalsIgnoreCase(userInfo.getJoinStatus())) {
            userManageText.setText(userInfo.getNickName() + "-" + this.context.getString(R.string.tabManageUserListItemJoinRequest));
            itemButtonApprove.setVisibility(View.VISIBLE);
            itemButtonReject.setVisibility(View.VISIBLE);
            itemButtonRemove.setVisibility(View.GONE);
        } else if ("JOINED".equalsIgnoreCase(userInfo.getJoinStatus())) {
            userManageText.setText(userInfo.getNickName());
            itemButtonApprove.setVisibility(View.GONE);
            itemButtonReject.setVisibility(View.GONE);
            itemButtonRemove.setVisibility(View.VISIBLE);
        }
        return returnView;
    }
}
