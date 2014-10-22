package com.origin.aiur.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.origin.aiur.app.AiurApplication;
import com.origin.aiur.utils.AppUtils;
import com.origin.aiur.vo.User;

/**
 * Created by dongjia on 9/24/2014.
 */
public class IdentityDao {
    private static IdentityDao instance = new IdentityDao();
    private static final String SHARE_PREFER_IDENTITY = "aiur_iden";

    private static final String PREFER_KEY_DEVICE_ID = "device_id";
    private static final String PREFER_KEY_TOKEN = "token";
    private static final String PREFER_KEY_RSA_PUBLIC = "key";
    private IdentityDao() {
    }

    public static IdentityDao getInstance() {
        return instance;
    }

    public String getDeviceId() {
        SharedPreferences preferences = AiurApplication.getInstance().getSharedPreferences(SHARE_PREFER_IDENTITY, Context.MODE_PRIVATE);
        String deviceId = preferences.getString(PREFER_KEY_DEVICE_ID, null);

        if (deviceId == null) {
            deviceId = AppUtils.generateDeviceId();
            SharedPreferences.Editor edit = preferences.edit();
            edit.putString(PREFER_KEY_DEVICE_ID, deviceId);
            edit.commit();
        }

        return deviceId;
    }

    public String getToken() {
        SharedPreferences preferences = AiurApplication.getInstance().getSharedPreferences(SHARE_PREFER_IDENTITY, Context.MODE_PRIVATE);
        return preferences.getString(PREFER_KEY_TOKEN, null);
    }

    public void setToken(String token) {
        SharedPreferences preferences = AiurApplication.getInstance().getSharedPreferences(SHARE_PREFER_IDENTITY, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(PREFER_KEY_TOKEN, token);
        edit.commit();
    }

    public void clearToken() {
        SharedPreferences preferences = AiurApplication.getInstance().getSharedPreferences(SHARE_PREFER_IDENTITY, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.remove(PREFER_KEY_TOKEN);
        edit.commit();
    }

    public String getKey() {
        SharedPreferences preferences = AiurApplication.getInstance().getSharedPreferences(SHARE_PREFER_IDENTITY, Context.MODE_PRIVATE);
        return preferences.getString(PREFER_KEY_RSA_PUBLIC, null);
    }

    public void setKey(String key) {
        SharedPreferences preferences = AiurApplication.getInstance().getSharedPreferences(SHARE_PREFER_IDENTITY, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(PREFER_KEY_RSA_PUBLIC, key);
        edit.commit();
    }

    public void setUser(User user) {

    }

    public User getUser() {
        return  null;
    }
}
