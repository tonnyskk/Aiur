package com.origin.aiur.dao;

import com.origin.aiur.dao.storage.ASQLMapStorage;
import com.origin.aiur.utils.ALogger;
import com.origin.aiur.utils.AppUtils;
import com.origin.aiur.vo.GroupEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongjia on 9/24/2014.
 */
public class GroupDao {

    private static GroupDao instance = new GroupDao();

    private GroupDao() {

    }

    public static GroupDao getInstance() {
        return instance;
    }

    public void saveGroupEvents(List<GroupEvent> groupEventList) {
        if (groupEventList == null || groupEventList.isEmpty()) {
            return;
        }

        try {
            // clear exist data firstly
            getStore().clear();
            for (GroupEvent groupEvent : groupEventList) {
                JSONObject groupEventJson = groupEvent.toJsonObject();
                getStore().put(String.valueOf(groupEvent.getEventId()), groupEventJson.toString().getBytes(AppUtils.CHARSET), groupEvent.getCreateTime());
            }
        } catch (JSONException e) {
            ALogger.log(ALogger.LogPriority.error, GroupDao.class, "Save GroupEvents failed for JSON parse!", e);
        } catch (UnsupportedEncodingException e) {
            ALogger.log(ALogger.LogPriority.error, GroupDao.class, "Save GroupEvents failed for Charset error!", e);
        }
    }

    public List<GroupEvent> getGroupEvents() {
        List<GroupEvent> groupEvents = new ArrayList<GroupEvent>();
        try {
            List<byte[]> groupData = getStore().getAllData();
            if (groupData.size() > 0) {
                for (byte[] data : groupData) {
                    if (data != null) {
                        GroupEvent group = new GroupEvent(new JSONObject(new String(data, AppUtils.CHARSET)));
                        groupEvents.add(group);
                    }
                }
            }
        } catch (JSONException e) {
            ALogger.log(ALogger.LogPriority.error, GroupDao.class, "Get GroupEvents failed for JSON parse!", e);
        } catch (UnsupportedEncodingException e) {
            ALogger.log(ALogger.LogPriority.error, GroupDao.class, "Get GroupEvents failed for Charset error!", e);
        }
        return groupEvents;
    }

    public ASQLMapStorage getStore() {
        return AStoreManager.getInstance().getGroupStore();
    }
}
