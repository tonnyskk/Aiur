package com.origin.aiur.http;

/**
 * Created by dongjia on 9/24/2014.
 */
public class HttpUtils {
    public static final String BASE_URL = "http://192.168.1.103:8080/aiur/rest";

    public static final String check_login = "/user/status";
    public static final String init_startup = "/user/startup";

    public static final String user_login = "/user/login";
    public static final String user_reg= "/user/reg";

    public static final String load_user_group = "/user/%s/group";
    public static final String load_user_finance = "/user/%s/finance";

    public static final String load_group_activity = "/group/activity/%s";
    public static final String create_group = "/group/new";
    public static final String search_group = "/group/search/%s/%s"; // UserId/SearchText
    public static final String join_group = "/group/join/%s/%s"; // userId / groupId


    public static String buildPath(String path, Object ... args) {
        return String.format(path, args);
    }
}
