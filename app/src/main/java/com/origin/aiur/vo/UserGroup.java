package com.origin.aiur.vo;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2014/9/23.
 */
public class UserGroup implements Parcelable {
    private long groupId;
    private String groupName;
    private long ownerUserId;

    public UserGroup() {

    }

    public UserGroup(JSONObject jsonObject) {
        this.fromJsonObject(jsonObject);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    public void fromJsonObject(JSONObject jsonObject) {
        try {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
