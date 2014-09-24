package com.origin.aiur.activity.splash;

import android.os.Bundle;

import com.origin.aiur.BaseActivity;
import com.origin.aiur.R;
import com.origin.aiur.dao.IdentityDao;
import com.origin.aiur.activity.logon.LoginActivity;
import com.origin.aiur.activity.main.MainActivity;


public class AiurActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aiur);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (IdentityDao.getInstance().isLogon()) {
            MainActivity.startActivity(this);
        } else {
            LoginActivity.startActivity(this);
        }

        this.finish();
    }

    @Override
    protected void onPostExecuteSuccessful(String action, Object response) {

    }

    @Override
    protected void onPostExecuteFailed(String action) {

    }
}
