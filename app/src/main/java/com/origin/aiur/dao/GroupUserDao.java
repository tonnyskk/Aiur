package com.origin.aiur.dao;

import com.origin.aiur.dao.storage.ASQLMapStorage;
import com.origin.aiur.utils.ALogger;
import com.origin.aiur.utils.AppUtils;
import com.origin.aiur.vo.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongjia on 11/18/2014.
 */
public class GroupUserDao {

    private static GroupUserDao instance = new GroupUserDao();

    private GroupUserDao() {

    }

    public static GroupUserDao getInstance() {
        return instance;
    }


    public void saveGroupUsers(List<User> userList) {
        if (userList == null || userList.isEmpty()) {
            return;
        }

        try {
            // clear exist data firstly
            getStore().clear();
            for (User user : userList) {
                JSONObject userJson = user.toJsonObject();
                getStore().put(String.valueOf(user.getGroupId()) + "_" + String.valueOf(user.getUserID()), userJson.toString().getBytes(AppUtils.CHARSET), user.getCreateTime());
            }
        } catch (JSONException e) {
            ALogger.log(ALogger.LogPriority.error, UserDao.class, "Save Users failed for JSON parse!", e);
        } catch (UnsupportedEncodingException e) {
            ALogger.log(ALogger.LogPriority.error, UserDao.class, "Save Users failed for Charset error!", e);
        }
    }

    public List<User> getGroupJoinedUserList(long groupId) {
        List<User> userList = new ArrayList<User>();
        try {
            List<byte[]> groupData = getStore().getAllData();
            if (groupData.size() > 0) {
                for (byte[] data : groupData) {
                    if (data != null) {
                        User userInfo = new User(new JSONObject(new String(data, AppUtils.CHARSET)));
                        if (groupId == userInfo.getGroupId() && "JOINED".equalsIgnoreCase(userInfo.getJoinStatus())) {
                            userList.add(userInfo);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            ALogger.log(ALogger.LogPriority.error, UserDao.class, "Get Users failed for JSON parse!", e);
        } catch (UnsupportedEncodingException e) {
            ALogger.log(ALogger.LogPriority.error, UserDao.class, "Get Users failed for Charset error!", e);
        }
        return userList;
    }

    public List<User> getGroupUserList(long groupId) {
        List<User> userList = new ArrayList<User>();
        try {
            List<byte[]> groupData = getStore().getAllData();
            if (groupData.size() > 0) {
                for (byte[] data : groupData) {
                    if (data != null) {
                        User userInfo = new User(new JSONObject(new String(data, AppUtils.CHARSET)));
                        if (groupId == userInfo.getGroupId()) {
                            userList.add(userInfo);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            ALogger.log(ALogger.LogPriority.error, UserDao.class, "Get Users failed for JSON parse!", e);
        } catch (UnsupportedEncodingException e) {
            ALogger.log(ALogger.LogPriority.error, UserDao.class, "Get Users failed for Charset error!", e);
        }
        return userList;
    }
    public ASQLMapStorage getStore() {
        return AStoreManager.getInstance().getGroupUserStore();
    }
}
