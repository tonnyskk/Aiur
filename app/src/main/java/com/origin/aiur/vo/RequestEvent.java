package com.origin.aiur.vo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2014/11/22.
 */
public class RequestEvent implements IJsonPacket {
    private String eventType;
    private long eventId;
    private String description;
    private double money;
    private String eventUser;
    private long createTime;

    public RequestEvent() {

    }

    public RequestEvent(JSONObject jsonObject) {
        try {
            this.fromJsonObject(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
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

    public String getEventUser() {
        return eventUser;
    }

    public void setEventUser(String eventUser) {
        this.eventUser = eventUser;
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
        jsonObject.put("eventType", eventType);
        jsonObject.put("eventId", eventId);
        jsonObject.put("description", description);
        jsonObject.put("money", money);
        jsonObject.put("eventUser", eventUser);
        jsonObject.put("createTime", createTime);
        return jsonObject;
    }

    @Override
    public void fromJsonObject(JSONObject jsonObject) throws JSONException {

        if (jsonObject != null) {
            if (jsonObject.has("eventType")) {
                setEventType(jsonObject.getString("eventType"));
            }
            if (jsonObject.has("eventId")) {
                setEventId(jsonObject.getLong("eventId"));
            }

            if (jsonObject.has("description")) {
                setDescription(jsonObject.getString("description"));
            }

            if (jsonObject.has("money")) {
                setMoney(jsonObject.getDouble("money"));
            }

            if (jsonObject.has("eventUser")) {
                setEventUser(jsonObject.getString("eventUser"));
            }

            if (jsonObject.has("createTime")) {
                setCreateTime(jsonObject.getLong("createTime"));
            }
        }
    }
}
