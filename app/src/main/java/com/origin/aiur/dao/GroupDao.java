package com.origin.aiur.dao;

import com.origin.aiur.vo.UserGroup;

import java.util.ArrayList;

/**
 * Created by dongjia on 9/24/2014.
 */
public class GroupDao {

    private static GroupDao instance = new GroupDao();
    private ArrayList<UserGroup> groupList = null;
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

    public ArrayList<UserGroup> getGroupList() {
        return groupList;
    }

    public void setGroupList(ArrayList<UserGroup> groupList) {
        this.groupList = groupList;
    }
}
