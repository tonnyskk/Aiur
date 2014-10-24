package com.origin.aiur.activity.main;

import com.origin.aiur.dao.FinanceDao;
import com.origin.aiur.dao.GroupDao;
import com.origin.aiur.dao.UserDao;
import com.origin.aiur.utils.ALogger;
import com.origin.aiur.utils.AppUtils;
import com.origin.aiur.vo.Finance;
import com.origin.aiur.vo.GroupEvent;
import com.origin.aiur.vo.UserGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2014/9/23.
 */
public class MainHelper {
    private static MainHelper mainHelper = new MainHelper();
    private MainHelper() {
    }

    public static MainHelper getInstance() {
        return mainHelper;
    }

    public List<GroupEvent> getGroupEventList(JSONObject object) {
        JSONArray groupArray = AppUtils.getJsonArray(object, "data");

        if (groupArray == null || groupArray.length() <= 0) {
            return null;
        }

        List<GroupEvent> groupEventList = new ArrayList<GroupEvent>();
        try {
            for(int i = 0; i < groupArray.length(); i ++) {
                JSONObject groupObject = groupArray.getJSONObject(i);
                groupEventList.add(new GroupEvent(groupObject));
            }
        } catch (JSONException e) {
            ALogger.log(ALogger.LogPriority.error, MainHelper.class, "Parse JSON failed. %s", object.toString(), e);
        }

        GroupDao.getInstance().saveGroupEvents(groupEventList);
        return groupEventList;
    }

    public List<UserGroup> getGroupList(JSONObject object) {
        JSONArray groupArray = AppUtils.getJsonArray(object, "data");

        if (groupArray == null || groupArray.length() <= 0) {
            return null;
        }

        List<UserGroup> userGroupList = new ArrayList<UserGroup>();
        try {
            for(int i = 0; i < groupArray.length(); i ++) {
                JSONObject groupObject = groupArray.getJSONObject(i);
                userGroupList.add(new UserGroup(groupObject));
            }
        } catch (JSONException e) {
            ALogger.log(ALogger.LogPriority.error, MainHelper.class, "Parse JSON failed. %s", object.toString(), e);
        }

        // Save group list to
        UserDao.getInstance().updateUserGroup(userGroupList);
        return userGroupList;
    }

    public Finance getFinanceInfo(JSONObject object) {
        JSONObject financeObject = AppUtils.getJsonObject(object, "data");

        if (financeObject == null) {
            return null;
        }

        Finance finance = new Finance();
        try {
            finance.fromJsonObject(financeObject);
            FinanceDao.getInstance().saveUserFinance(finance);
        } catch (JSONException e) {
            ALogger.log(ALogger.LogPriority.error, MainHelper.class, "Parse JSON failed. %s", object.toString(), e);
        }
        return finance;
    }

    public String formatMoney(double money) {
        DecimalFormat format = new DecimalFormat("##,##0.00");
        return format.format(money);
    }
}
