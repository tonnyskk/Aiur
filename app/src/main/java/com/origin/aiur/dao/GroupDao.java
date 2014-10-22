package com.origin.aiur.dao;

import com.origin.aiur.vo.UserGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongjia on 9/24/2014.
 */
public class GroupDao {

    private static GroupDao instance = new GroupDao();
    private List<UserGroup> groupList = new ArrayList<UserGroup>();
    private UserGroup currentGroup;

    private GroupDao() {

    }

    public static GroupDao getInstance() {
        return instance;
    }

    public UserGroup getCurrentGroup() {
        return currentGroup;
    }

    public void setCurrentGroup(UserGroup userGroup) {
        this.currentGroup = userGroup;
    }

    public List<UserGroup> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<UserGroup> groupList) {
        this.groupList.clear();
        if (groupList != null) {
            this.groupList.addAll(groupList);
        }
    }
}
