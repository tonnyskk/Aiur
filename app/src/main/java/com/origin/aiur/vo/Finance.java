package com.origin.aiur.vo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dongjia on 10/24/2014.
 */
public class Finance implements IJsonPacket {
    private long userId;
    private double consumeSummary = 0d;
    private double incomingSummary = 0d;

    public Finance() {
    }

    public Finance(JSONObject jsonObject) {
        try {
            this.fromJsonObject(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public double getConsumeSummary() {
        return consumeSummary;
    }

    public void setConsumeSummary(double consumeSummary) {
        this.consumeSummary = consumeSummary;
    }

    public double getIncomingSummary() {
        return incomingSummary;
    }

    public void setIncomingSummary(double incomingSummary) {
        this.incomingSummary = incomingSummary;
    }

    @Override
    public JSONObject toJsonObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("consumeSummary", consumeSummary);
        jsonObject.put("incomingSummary", incomingSummary);
        jsonObject.put("userId", userId);
        return jsonObject;
    }

    @Override
    public void fromJsonObject(JSONObject json) throws JSONException {
        if (json != null) {
            if (json.has("consumeSummary")) {
                this.consumeSummary = json.getDouble("consumeSummary");
            }
            if (json.has("incomingSummary")) {
                this.incomingSummary = json.getDouble("incomingSummary");
            }
            if (json.has("userId")) {
                this.userId = json.getLong("userId");
            }
        }
    }
}
