package com.origin.aiur.activity.logon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.origin.aiur.BaseActivity;
import com.origin.aiur.R;
import com.origin.aiur.dao.GroupDao;
import com.origin.aiur.dao.IdentityDao;
import com.origin.aiur.dao.UserDao;
import com.origin.aiur.http.HttpUtils;
import com.origin.aiur.activity.main.MainActivity;
import com.origin.aiur.utils.ALogger;
import com.origin.aiur.utils.AppUtils;
import com.origin.aiur.vo.User;

import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private TextView loginButton;
    private TextView registerButton;

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

        userAccount = (EditText)findViewById(R.id.loginAccount);
        userPassword = (EditText)findViewById(R.id.loginPassword);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
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
    public void onClick(View view) {
        if (view == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.loginLogon:
                if (isInputValid()) {
                    this.postSync(Actions.user_login.name());
                }
                break;

            case R.id.loginRegister:
                RegisterActivity.startActivity(this);
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

        return true;
    }

    @Override
    protected String getPath(String action){
        String path = null;
        switch (Actions.valueOf(action)) {
            case user_login:
                path = HttpUtils.user_login;
                break;
        }
        return path;
    }

    @Override
    protected HashMap<String, String> getPostParam(String action) {
        HashMap<String, String> param = new HashMap<String, String>();
        switch (Actions.valueOf(action)) {
            case user_login:
                param.put("loginName", userAccount.getText().toString());
                String encodePassword = AppUtils.encryptKey(userPassword.getText().toString());
                param.put("password", encodePassword);
                ALogger.log(ALogger.LogPriority.debug, LoginActivity.class, "Encrypt message result > " + encodePassword);
                break;
        }

        return param;
    }
}

