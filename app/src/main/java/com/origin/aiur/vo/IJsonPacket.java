package com.origin.aiur.vo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2014/10/22.
 */
public interface IJsonPacket {
    public JSONObject toJsonObject() throws JSONException;
    public void fromJsonObject(JSONObject json) throws JSONException;
}
