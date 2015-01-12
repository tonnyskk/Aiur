package com.origin.aiur.activity.group.pager;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.origin.aiur.R;
import com.origin.aiur.http.HttpExecutor;
import com.origin.aiur.utils.AppUtils;
import com.origin.aiur.vo.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/9/23.
 */
public class UserAvatarAdapter extends BaseAdapter {
    private List<User> userList = new ArrayList<User>();
    private Context context;
    private LayoutInflater mInflater;

    public UserAvatarAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setUserList(List<User> activityList) {
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
            returnView = mInflater.inflate(R.layout.adapter_user_avatar, null);
        }

        if (returnView == null) {
            return returnView;
        }

        User userInfo = userList.get(i);

        TextView userBalance = (TextView) returnView.findViewById(R.id.userBalanceAmount);
        userBalance.setText(AppUtils.formatMoney(userInfo.getPrepayMoney() - userInfo.getConsumeMoney()));

        TextView userName = (TextView) returnView.findViewById(R.id.userDisplayName);
        userName.setText(userInfo.getNickName());

        ImageView avatarUserImage = (ImageView)returnView.findViewById(R.id.avatarUserImage);
        avatarUserImage.setImageResource(R.drawable.user_avatar_default_unfocused);

        Drawable avatar = AppUtils.getAvatar(userInfo.getAvatarData());
        if (avatar != null) {
            avatarUserImage.setImageDrawable(avatar);
        }


        return returnView;
    }
}
