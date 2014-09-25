package com.origin.aiur.activity.logon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.origin.aiur.BaseActivity;
import com.origin.aiur.R;
import com.origin.aiur.http.HttpUtils;
import com.origin.aiur.activity.main.MainActivity;

import org.json.JSONObject;

import java.util.HashMap;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private TextView regButton = null;

    enum Actions {
        user_reg, user_login
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);

        regButton = (TextView)findViewById(R.id.btnRegister);
        regButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.btnRegister:
                this.postSync(Actions.user_reg.name());
                break;
        }
    }

    @Override
    public void onPostExecuteSuccessful(String action, JSONObject response) {
        switch (Actions.valueOf(action)) {
            case user_login:
                MainActivity.startActivity(this);
                this.finish();
                break;
            case user_reg:
                this.postSync(Actions.user_login.name());
                break;
        }
    }

    @Override
    public void onPostExecuteFailed(String action) {

    }

    @Override
    protected String getPath(String action){
        String path = null;
        switch (Actions.valueOf(action)) {
            case user_login:
                path = HttpUtils.user_login;
                break;
            case user_reg:
                path = HttpUtils.user_reg;
                break;
        }
        return path;
    }

    protected HashMap<String, String> getParam(String action) {
        HashMap<String, String> param = new HashMap<String, String>();
        switch (Actions.valueOf(action)) {
            case user_login:
                //param.put("account", userAccount.getText().toString());
                //param.put("password", userPassword.getText().toString());
                break;
            case user_reg:
                //param.put("account", userAccount.getText().toString());
                //param.put("password", userPassword.getText().toString());
                break;
        }
        return param;
    }
}
