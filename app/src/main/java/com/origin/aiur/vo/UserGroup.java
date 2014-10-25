package com.origin.aiur.vo;

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2014/9/23.
 */
public class UserGroup implements IJsonPacket {
    private long groupId;
    private String groupName;
    private String groupDesc;
    private long ownerUserId;

    public UserGroup() {

    }

    public UserGroup(JSONObject jsonObject) {
        try {
            this.fromJsonObject(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public long getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(long ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    @Override
    public void fromJsonObject(JSONObject jsonObject) throws JSONException{
        if (jsonObject != null) {
            if (jsonObject.has("groupId")) {
                setGroupId(jsonObject.getLong("groupId"));
            }

            if (jsonObject.has("groupName")) {
                setGroupName(jsonObject.getString("groupName"));
            }

            if (jsonObject.has("ownerUserId")) {
                setOwnerUserId(jsonObject.getLong("ownerUserId"));
            }

            if (jsonObject.has("groupDesc")) {
                setGroupDesc(jsonObject.getString("groupDesc"));
            }
        }
    }

    @Override
    public JSONObject toJsonObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("groupId", groupId);
        jsonObject.put("groupName", groupName);
        jsonObject.put("groupDesc", groupDesc);
        jsonObject.put("ownerUserId", ownerUserId);
        return jsonObject;
    }
}
