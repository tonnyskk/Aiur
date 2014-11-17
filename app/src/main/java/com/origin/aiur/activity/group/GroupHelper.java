package com.origin.aiur.activity.group;

import com.origin.aiur.utils.ALogger;
import com.origin.aiur.utils.AppUtils;
import com.origin.aiur.vo.UserGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/10/25.
 */
public class GroupHelper {
    private static GroupHelper gpHelper = new GroupHelper();

    private GroupHelper() {
    }

    public static GroupHelper getInstance() {
        return gpHelper;
    }

    public List<UserGroup> getSearchGroupList(JSONObject object) {
        JSONArray groupArray = AppUtils.getJsonArray(object, "data");

        if (groupArray == null || groupArray.length() <= 0) {
            return null;
        }

        List<UserGroup> userGroupList = new ArrayList<UserGroup>();
        try {
            for (int i = 0; i < groupArray.length(); i++) {
                JSONObject groupObject = groupArray.getJSONObject(i);
                userGroupList.add(new UserGroup(groupObject));
            }
        } catch (JSONException e) {
            ALogger.log(ALogger.LogPriority.error, GroupHelper.class, "Parse JSON failed. %s", object.toString(), e);
        }

        return userGroupList;
    }

    public long getJoinedGroupId(JSONObject object) {
        JSONObject jsonObject = AppUtils.getJsonObject(object, "data");
        long joinedGroupId = -1;
        if (jsonObject != null) {
            try {
                joinedGroupId = jsonObject.getLong("groupId");
            } catch (JSONException e) {
                ALogger.log(ALogger.LogPriority.error, GroupHelper.class, "Parse JSON failed. %s", object.toString(), e);
            }
        }
        return joinedGroupId;
    }
}
