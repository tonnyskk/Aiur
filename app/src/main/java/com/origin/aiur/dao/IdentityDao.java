package com.origin.aiur.dao;

/**
 * Created by dongjia on 9/24/2014.
 */
public class IdentityDao {
    private static IdentityDao instance = new IdentityDao();
    private IdentityDao() {

    }

    public static IdentityDao getInstance() {
        return instance;
    }

    public boolean isLogon() {
        return false;
    }
}
