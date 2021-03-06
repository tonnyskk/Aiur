package com.origin.aiur.activity.logon;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.origin.aiur.BaseActivity;
import com.origin.aiur.R;
import com.origin.aiur.activity.main.MainActivity;
import com.origin.aiur.dao.UserDao;
import com.origin.aiur.http.HttpExecutor;
import com.origin.aiur.http.HttpUtils;
import com.origin.aiur.utils.AppUtils;
import com.origin.aiur.vo.User;

import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends BaseActivity implements TextWatcher {
    private TextView loginButton;
    private TextView registerButton;
    private ImageView userAvatar;

    private EditText userAccount;
    private EditText userPassword;

    enum Actions {
        user_login
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        loginButton = (TextView) findViewById(R.id.loginLogon);
        registerButton = (TextView) findViewById(R.id.loginRegister);

        userAvatar = (ImageView) findViewById(R.id.loginUserAvatar);

        userAccount = (EditText) findViewById(R.id.loginAccount);
        userAccount.addTextChangedListener(this);
        userPassword = (EditText) findViewById(R.id.loginPassword);
        userPassword.addTextChangedListener(this);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);


        User user = UserDao.getInstance().getCurrentUser();
        if (user != null) {
            if (user.getLoginName() != null) {
                userAccount.setText(user.getLoginName());
            }

            Drawable avatar = AppUtils.getAvatar(user.getAvatarData());
            if (avatar != null) {
                userAvatar.setImageDrawable(avatar);
            }
        }
    }

    @Override
    public void onPostExecuteSuccessful(String action, JSONObject response) {
        // Update user identity data
        JSONObject userInfo = AppUtils.getJsonObject(response, "data");
        User loginUser = new User(userInfo);
        UserDao.getInstance().setCurrentUser(loginUser);

        // Start main activity
        MainActivity.startActivity(this);
        this.finish();
    }

    @Override
    public void onPostExecuteFailed(String action) {
        // invalid user or others
    }

    @Override
    public void performClick(View view) {
        if (view == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.loginLogon:
                if (isInputValid()) {
                    HashMap<String, Object> param = new HashMap<String, Object>();
                    param.put("loginName", userAccount.getText().toString());
                    param.put("password", AppUtils.encryptKey(userPassword.getText().toString()));

                    this.postSync(Actions.user_login.name(), param);
                }
                break;

            case R.id.loginRegister:
                RegisterActivity.startActivity(this);

                // TODO:
                // Terminate this activity temporary, if not, after register finished, user
                // click back from main activity, this loginActivity will display.
                this.finish();
                break;
        }
    }

    private boolean isInputValid() {
        String account = userAccount.getText().toString();
        String pwd = userPassword.getText().toString();

        if (AppUtils.isEmpty(account) || AppUtils.isEmpty(pwd)) {
            showToastMessage(this.getString(R.string.no_input_value));
            return false;
        }

        if (!AppUtils.isValidEmail(userAccount.getText())) {
            userAccount.setFocusable(true);
            userAccount.setTextColor(getResources().getColor(R.color.commonErrorFiled));
            showToastMessage(this.getString(R.string.error_email_format));
            return false;
        }
        return true;
    }

    @Override
    public String getPath(String action, Object... args) {
        String path = null;
        switch (Actions.valueOf(action)) {
            case user_login:
                path = HttpUtils.user_login;
                break;
        }
        return path;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        String account = userAccount.getText().toString();
        String pwd = userPassword.getText().toString();

        userAccount.setTextColor(getResources().getColor(R.color.commonTextColor));

        if (AppUtils.isEmpty(account) || AppUtils.isEmpty(pwd)) {
            loginButton.setEnabled(false);
        } else {
            loginButton.setEnabled(true);
        }

        userAvatar.setImageResource(R.drawable.user_avatar_default_unfocused);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}

