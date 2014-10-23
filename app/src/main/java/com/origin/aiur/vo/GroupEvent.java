package com.origin.aiur.vo;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2014/9/23.
 */
public class GroupEvent implements IJsonPacket {
    private long eventId;
    private long groupId;
    private String groupName;
    private String description;
    private double money;
    private String status;
    private long createTime;

    public GroupEvent() {
    }

    public GroupEvent(JSONObject jsonObject) {
        try {
            this.fromJsonObject(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public JSONObject toJsonObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("activityId", eventId);
        jsonObject.put("groupId", groupId);
        jsonObject.put("description", description);
        jsonObject.put("groupName", groupName);
        jsonObject.put("money", money);
        jsonObject.put("status", status);
        jsonObject.put("createTime", createTime);
        return jsonObject;
    }

    @Override
    public void fromJsonObject(JSONObject jsonObject) throws JSONException {
        if (jsonObject != null) {
            if (jsonObject.has("activityId")) {
                setEventId(jsonObject.getLong("activityId"));
            }
            if (jsonObject.has("groupId")) {
                setGroupId(jsonObject.getLong("groupId"));
            }

            if (jsonObject.has("description")) {
                setDescription(jsonObject.getString("description"));
            }

            if (jsonObject.has("groupName")) {
                setGroupName(jsonObject.getString("groupName"));
            }

            if (jsonObject.has("money")) {
                setMoney(jsonObject.getDouble("money"));
            }

            if (jsonObject.has("status")) {
                setStatus(jsonObject.getString("status"));
            }

            if (jsonObject.has("createTime")) {
                setCreateTime(jsonObject.getLong("createTime"));
            }
        }
    }
}
