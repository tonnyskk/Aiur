package com.origin.aiur.dao;

import com.origin.aiur.dao.storage.APrivateMapStorage;
import com.origin.aiur.utils.AppUtils;

/**
 * Created by dongjia on 9/24/2014.
 */
public class IdentityDao {
    private static IdentityDao instance = new IdentityDao();

    private static final String PREFER_KEY_DEVICE_ID = "device_id";
    private static final String PREFER_KEY_TOKEN = "token";
    private static final String PREFER_KEY_RSA_PUBLIC = "key";

    private IdentityDao() {
    }

    public static IdentityDao getInstance() {
        return instance;
    }

    public String getDeviceId() {
        String deviceId = getStore().getString(PREFER_KEY_DEVICE_ID, null);

        if (deviceId == null) {
            deviceId = AppUtils.generateDeviceId();
            getStore().setString(PREFER_KEY_DEVICE_ID, deviceId);
        }

        return deviceId;
    }

    private APrivateMapStorage getStore() {
        return AStoreManager.getInstance().getIdentityStore();
    }

    public String getToken() {
        return getStore().getString(PREFER_KEY_TOKEN, null);
    }

    public void setToken(String token) {
        getStore().setString(PREFER_KEY_TOKEN, token);
    }

    public void clearToken() {
        getStore().remove(PREFER_KEY_TOKEN);
    }

    public String getKey() {
        return getStore().getString(PREFER_KEY_RSA_PUBLIC, null);
    }

    public void setKey(String key) {
        getStore().setString(PREFER_KEY_RSA_PUBLIC, key);
    }
}
