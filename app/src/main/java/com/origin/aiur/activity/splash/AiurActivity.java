package com.origin.aiur.activity.splash;

import android.os.Bundle;
import android.view.Window;

import com.origin.aiur.BaseActivity;
import com.origin.aiur.R;
import com.origin.aiur.activity.logon.LoginActivity;
import com.origin.aiur.activity.main.MainActivity;
import com.origin.aiur.dao.IdentityDao;
import com.origin.aiur.http.HttpUtils;

import org.json.JSONObject;


public class AiurActivity extends BaseActivity {

    enum Actions {
        validate_token
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_aiur);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String token = IdentityDao.getInstance().getToken();
        if (token != null && token.length() > 0) {
            this.getSync(Actions.validate_token.name());
        } else {
            LoginActivity.startActivity(this);
            this.finish();
        }
    }

    @Override
    protected void onPostExecuteSuccessful(String action, JSONObject response) {
        switch (Actions.valueOf(action)) {
            case validate_token:
                int statusCode = super.getResponseStatus(response);
                if (statusCode == 200) {
                    MainActivity.startActivity(this);
                } else {
                    LoginActivity.startActivity(this);
                }
                this.finish();
                break;
        }
    }

    @Override
    protected void onPostExecuteFailed(String action) {
        switch (Actions.valueOf(action)) {
            case validate_token:
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
                break;
        }
    }

    @Override
    protected String getPath(String action){
        String path = null;
        switch (Actions.valueOf(action)) {
            case validate_token:
                String token = IdentityDao.getInstance().getToken();
                path = HttpUtils.buildPath(HttpUtils.validate_token, token);
                break;
        }
        return path;
    }
}
