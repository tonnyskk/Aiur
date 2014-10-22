package com.origin.aiur.vo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongjia on 10/22/2014.
 */
public class User implements IJsonPacket {
    private long userID;
    private String loginName;
    private String nickName;

    private List<UserGroup> userGroupList;

    public User() {

    }

    public User(JSONObject jsonObject) {
        try {
            this.fromJsonObject(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public List<UserGroup> getUserGroupList() {
        return userGroupList;
    }

    public void setUserGroupList(List<UserGroup> userGroupList) {
        this.userGroupList = userGroupList;
    }

    @Override
    public void fromJsonObject(JSONObject jsonObject) throws JSONException {
        if (jsonObject != null) {
            if (jsonObject.has("userID")) {
                setUserID(jsonObject.getLong("userID"));
            }

            if (jsonObject.has("loginName")) {
                setLoginName(jsonObject.getString("loginName"));
            }

            if (jsonObject.has("nickName")) {
                setNickName(jsonObject.getString("nickName"));
            }

            if (jsonObject.has("userGroupList")) {
                JSONArray groupList = jsonObject.getJSONArray("userGroupList");
                userGroupList = new ArrayList<UserGroup>();
                for (int i = 0; i < groupList.length(); i++) {
                    JSONObject groupObj = (JSONObject) groupList.get(i);
                    UserGroup group = new UserGroup(groupObj);
                    userGroupList.add(group);
                }
            }
        }
    }

    @Override
    public JSONObject toJsonObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userID", userID);
        jsonObject.put("loginName", loginName);
        jsonObject.put("nickName", nickName);

        if (userGroupList != null) {
            JSONArray jsonArray = new JSONArray();
            for (UserGroup userGroup : userGroupList) {
                JSONObject groupObject = userGroup.toJsonObject();
                jsonArray.put(groupObject);
            }
            jsonObject.put("userGroupList", jsonArray);
        }
        return jsonObject;
    }
}
