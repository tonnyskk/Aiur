package com.origin.aiur.splash;

import android.os.Bundle;

import com.origin.aiur.BaseActivity;
import com.origin.aiur.BaseModel;
import com.origin.aiur.R;
import com.origin.aiur.main.MainActivity;
import com.origin.aiur.logon.LoginActivity;


public class AiurActivity extends BaseActivity {

    enum Action {
        checkUserStatus
    }

    enum Keys {
        isUserLogin
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aiur);
    }

    @Override
    protected void onStart() {
        super.onStart();
        postSync(Action.checkUserStatus.name());
    }

    @Override
    public void onPostExecuteSuccessful(String action) {
        boolean isUserLogon = getIntent().getBooleanExtra(Keys.isUserLogin.name(), false);
        if (!isUserLogon) {
            LoginActivity.startActivity(this);
        } else {
            MainActivity.startActivity(this);
        }
    }

    @Override
    public void onPostExecuteFailed(String action) {

    }

    @Override
    public BaseModel getModel() {
        return new AiurModel(this);
    }
}
