package com.origin.aiur.activity.splash;

import android.os.Bundle;
import android.view.Window;

import com.origin.aiur.BaseActivity;
import com.origin.aiur.R;
import com.origin.aiur.activity.logon.LoginActivity;
import com.origin.aiur.activity.main.MainActivity;
import com.origin.aiur.dao.IdentityDao;
import com.origin.aiur.dao.UserDao;
import com.origin.aiur.http.HttpUtils;
import com.origin.aiur.utils.AppUtils;

import org.json.JSONObject;

import java.util.HashMap;


public class AiurActivity extends BaseActivity {

    enum Actions {
        check_login, init_startup
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
        long userId = UserDao.getInstance().getUserId();

        if (token == null || token.trim().length() <= 0 || userId < 0) {
            this.getSync(Actions.init_startup.name());
        } else {
            this.getSync(Actions.check_login.name());
        }
    }

    @Override
    protected void onPostExecuteSuccessful(String action, JSONObject response) {
        switch (Actions.valueOf(action)) {
            case check_login:
                MainActivity.startActivity(this);
                this.finish();
                break;
            case init_startup:
                LoginActivity.startActivity(this);
                this.finish();
        }
    }

    @Override
    protected void onPostExecuteFailed(String action) {
        switch (Actions.valueOf(action)) {
            case check_login:
            case init_startup:
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
            case check_login:
                path = HttpUtils.buildPath(HttpUtils.check_login);
                break;
            case init_startup:
                path = HttpUtils.buildPath(HttpUtils.init_startup);
        }
        return path;
    }

    @Override
    protected HashMap<String, String> getPostParam(String action) {
        return null;
    }
}
