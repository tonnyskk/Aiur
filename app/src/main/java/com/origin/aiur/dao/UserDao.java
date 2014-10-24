package com.origin.aiur.dao;

import com.origin.aiur.dao.storage.ASQLMapStorage;
import com.origin.aiur.utils.ALogger;
import com.origin.aiur.utils.AppUtils;
import com.origin.aiur.vo.User;
import com.origin.aiur.vo.UserGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by dongjia on 9/24/2014.
 */
public class UserDao {
    private static UserDao instance = new UserDao();

    private enum Keys {
        CurrentUser, CurrentGroup
    }

    public static UserDao getInstance() {
        return instance;
    }

    public void setCurrentUser(User user) {
        try {
            getStore().put(Keys.CurrentUser.name(), user.toJsonObject().toString().getBytes(AppUtils.CHARSET));
        } catch (JSONException e) {
            ALogger.log(ALogger.LogPriority.error, UserDao.class, "Set current user failed for JSON parse!", e);
        } catch (UnsupportedEncodingException e) {
            ALogger.log(ALogger.LogPriority.error, UserDao.class, "Set current user failed for Charset error!", e);
        }
    }

    public User getCurrentUser() {
        try {
            byte[] data = getStore().get(Keys.CurrentUser.name());
            if (data != null) {
                User user = new User(new JSONObject(new String(data, AppUtils.CHARSET)));
                return user;
            }
        } catch (JSONException e) {
            ALogger.log(ALogger.LogPriority.error, UserDao.class, "Get current user failed for JSON parse!", e);
        } catch (UnsupportedEncodingException e) {
            ALogger.log(ALogger.LogPriority.error, UserDao.class, "Get current user failed for Charset error!", e);
        }
        return null;
    }

    public void updateUserGroup(List<UserGroup> userGroupList) {
        User user = getCurrentUser();
        if (user != null) {
            user.setUserGroupList(userGroupList);
            setCurrentUser(user);
        }
    }

    public List<UserGroup> getUserGroupList() {
        User user = getCurrentUser();
        if (user != null) {
            return user.getUserGroupList();
        }
        return null;
    }

    public UserGroup getCurrentGroup() {
        try {
            byte[] data = getStore().get(Keys.CurrentGroup.name());
            if (data != null) {
                UserGroup userGroup = new UserGroup(new JSONObject(new String(data, AppUtils.CHARSET)));
                return userGroup;
            }
        } catch (JSONException e) {
            ALogger.log(ALogger.LogPriority.error, UserDao.class, "Get current group failed for JSON parse!", e);
        } catch (UnsupportedEncodingException e) {
            ALogger.log(ALogger.LogPriority.error, UserDao.class, "Get current group failed for Charset error!", e);
        }
        return null;
    }

    public void setCurrentGroup(UserGroup userGroup) {
        try {
            getStore().put(Keys.CurrentGroup.name(), userGroup.toJsonObject().toString().getBytes(AppUtils.CHARSET));
        } catch (JSONException e) {
            ALogger.log(ALogger.LogPriority.error, UserDao.class, "Set current group failed for JSON parse!", e);
        } catch (UnsupportedEncodingException e) {
            ALogger.log(ALogger.LogPriority.error, UserDao.class, "Set current group failed for Charset error!", e);
        }
    }

    public long getUserId() {
        User user = getCurrentUser();
        if (user != null) {
            return user.getUserID();
        }
        return -1;
    }

    public ASQLMapStorage getStore() {
        return AStoreManager.getInstance().getUserStore();
    }
}
