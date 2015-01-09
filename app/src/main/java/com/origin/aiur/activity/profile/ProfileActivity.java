package com.origin.aiur.activity.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.origin.aiur.BaseActivity;
import com.origin.aiur.R;
import com.origin.aiur.dao.UserDao;
import com.origin.aiur.http.HttpExecutor;
import com.origin.aiur.vo.User;

import org.json.JSONObject;

public class ProfileActivity extends BaseActivity {
    private TextView userProfileName;
    private TextView userProfileAccount;
    private ImageView userProfileAvatar;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ProfileActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_profile);

        userProfileName = (TextView) this.findViewById(R.id.userProfileName);
        userProfileAccount = (TextView) this.findViewById(R.id.userProfileAccount);
        userProfileAvatar = (ImageView) findViewById(R.id.userProfileAvatar);

        User user = UserDao.getInstance().getCurrentUser();
        if (user != null) {

            userProfileName.setText(user.getNickName());
            userProfileAccount.setText(user.getLoginName());

            if (user.getAvatarUrl() != null) {
                HttpExecutor.getExecutor().loadImage(user.getAvatarUrl(), userProfileAvatar, R.drawable.user_avatar_default_unfocused, R.drawable.user_avatar_default_unfocused);
            }
        }
    }

    @Override
    public void performClick(View view) {

    }

    @Override
    public void onPostExecuteSuccessful(String action, JSONObject response) {

    }

    @Override
    public void onPostExecuteFailed(String action) {

    }

    @Override
    public String getPath(String action, Object... args) {
        return null;
    }
}
