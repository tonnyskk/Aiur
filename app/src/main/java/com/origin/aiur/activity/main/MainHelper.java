package com.origin.aiur.activity.main;

import com.origin.aiur.dao.GroupDao;
import com.origin.aiur.dao.UserDao;
import com.origin.aiur.vo.GroupEvent;
import com.origin.aiur.vo.UserGroup;

import org.json.JSONArray;
import org.json.JSONObject;

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

    public ArrayList<GroupEvent> getGroupActivityList(Object object) {

        // parse response and save data into DAO
        ArrayList<GroupEvent> groupEventList = new ArrayList<GroupEvent>();

        GroupEvent groupEvent = new GroupEvent();
        groupEvent.setActivityDesc("A has Joined Group!");
        groupEvent.setActivityTimestamp(new Date().getTime());
        groupEventList.add(groupEvent);

        groupEvent = new GroupEvent();
        groupEvent.setActivityDesc("Group B payed 100$ for lunch!");
        groupEvent.setActivityTimestamp(new Date().getTime());
        groupEventList.add(groupEvent);

        return groupEventList;
    }

    public List<UserGroup> getGroupList(JSONObject object) {

        // parse response and save data into DAO
        List<UserGroup> groupList = new ArrayList<UserGroup>();
        UserGroup userGroup = new UserGroup();
        userGroup.setGroupName("Eating GP");
        groupList.add(userGroup);

        userGroup = new UserGroup();
        userGroup.setGroupName("Super Man");
        groupList.add(userGroup);

        // Save group list to
        UserDao.getInstance().updateUserGroup(groupList);
        return groupList;
    }
}
