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
import com.origin.aiur.http.HttpUtils;
import com.origin.aiur.activity.main.MainActivity;

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
                if (validateInput()) {
                    this.postSync(Actions.user_login.name());
                }
                break;

            case R.id.loginRegister:
                RegisterActivity.startActivity(this);
                break;
        }
    }

    private boolean validateInput() {
        // TODO:

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

    protected HashMap<String, String> getParam(String action) {
        HashMap<String, String> param = new HashMap<String, String>();
        switch (Actions.valueOf(action)) {
            case user_login:
                param.put("account", userAccount.getText().toString());
                param.put("password", userPassword.getText().toString());
                break;
        }
        return param;
    }
}
