package com.origin.aiur.activity.group;

import com.origin.aiur.dao.FinanceDao;
import com.origin.aiur.dao.GroupUserDao;
import com.origin.aiur.dao.GroupEventDao;
import com.origin.aiur.dao.RequestEventDao;
import com.origin.aiur.utils.ALogger;
import com.origin.aiur.utils.AppUtils;
import com.origin.aiur.vo.Finance;
import com.origin.aiur.vo.GroupEvent;
import com.origin.aiur.vo.RequestEvent;
import com.origin.aiur.vo.User;
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

    private List<IChangeTabListener> tabChangeListeners = new ArrayList<IChangeTabListener>();

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

    public Finance getFinanceInfo(JSONObject object) {
        JSONObject financeObject = AppUtils.getJsonObject(object, "data");

        if (financeObject == null) {
            return null;
        }

        Finance finance = new Finance();
        try {
            finance.fromJsonObject(financeObject);
            FinanceDao.getInstance().saveGroupFinance(finance);
        } catch (JSONException e) {
            ALogger.log(ALogger.LogPriority.error, GroupHelper.class, "Parse JSON failed. %s", object.toString(), e);
        }
        return finance;
    }

    public List<GroupEvent> getGroupEventList(JSONObject object) {
        JSONArray groupArray = AppUtils.getJsonArray(object, "data");

        if (groupArray == null || groupArray.length() <= 0) {
            GroupEventDao.getInstance().getStore().clear();
            return null;
        }

        List<GroupEvent> groupEventList = new ArrayList<GroupEvent>();
        try {
            for(int i = 0; i < groupArray.length(); i ++) {
                JSONObject groupObject = groupArray.getJSONObject(i);
                groupEventList.add(new GroupEvent(groupObject));
            }
        } catch (JSONException e) {
            ALogger.log(ALogger.LogPriority.error, GroupHelper.class, "Parse JSON failed. %s", object.toString(), e);
        }

        GroupEventDao.getInstance().saveGroupEvents(groupEventList);
        return groupEventList;
    }

    public List<User> getGroupUserList(JSONObject object) {
        JSONArray groupArray = AppUtils.getJsonArray(object, "data");

        if (groupArray == null || groupArray.length() <= 0) {

            return null;
        }
        List<User> groupUserList = new ArrayList<User>();
        try {
            for(int i = 0; i < groupArray.length(); i ++) {
                JSONObject groupObject = groupArray.getJSONObject(i);
                groupUserList.add(new User(groupObject));
            }
        } catch (JSONException e) {
            ALogger.log(ALogger.LogPriority.error, GroupHelper.class, "Parse JSON failed. %s", object.toString(), e);
        }

        GroupUserDao.getInstance().saveGroupUsers(groupUserList);
        return groupUserList;
    }

    public void addChangeTabListener(IChangeTabListener listener) {
        if (!tabChangeListeners.contains(listener)) {
            tabChangeListeners.add(listener);
        }
    }

    public void removeChangeTabListener(IChangeTabListener listener) {
        if (tabChangeListeners.contains(listener)){
            tabChangeListeners.remove(listener);
        }
    }

    public void notifyChangeTab(int index) {
        for(IChangeTabListener listener : tabChangeListeners) {
            listener.onChangeTabEvent(index);
        }
    }


    public List<RequestEvent> getRequestEventList(JSONObject object) {
        JSONArray groupArray = AppUtils.getJsonArray(object, "data");

        if (groupArray == null || groupArray.length() <= 0) {
            //If no valid data found, we need clear the DAO
            RequestEventDao.getInstance().getStore().clear();
            return null;
        }

        List<RequestEvent> groupEventList = new ArrayList<RequestEvent>();
        try {
            for(int i = 0; i < groupArray.length(); i ++) {
                JSONObject groupObject = groupArray.getJSONObject(i);
                groupEventList.add(new RequestEvent(groupObject));
            }
        } catch (JSONException e) {
            ALogger.log(ALogger.LogPriority.error, GroupHelper.class, "Parse JSON failed. %s", object.toString(), e);
        }

        RequestEventDao.getInstance().saveRequestEvents(groupEventList);
        return groupEventList;
    }
}
