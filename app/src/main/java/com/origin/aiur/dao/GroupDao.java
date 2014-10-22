package com.origin.aiur.dao;

import com.origin.aiur.dao.storage.ASQLMapStorage;

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


    public ASQLMapStorage getStore() {
        return AStoreManager.getInstance().getGroupStore();
    }
}
