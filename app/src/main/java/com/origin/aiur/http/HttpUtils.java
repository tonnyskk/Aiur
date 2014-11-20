package com.origin.aiur.http;

/**
 * Created by dongjia on 9/24/2014.
 */
public class HttpUtils {
    private static final String HOME_URL = "http://192.168.1.104:8080/aiur/rest";
    private static final String WORK_URL = "http://10.148.202.87:8088/aiur/rest";

    public static final String BASE_URL = HOME_URL;

    public static final String check_login = "/user/status";
    public static final String init_startup = "/user/startup";

    public static final String user_login = "/user/login";
    public static final String user_reg= "/user/reg";
    public static final String load_user_group = "/user/%s/group";
    public static final String load_user_finance = "/user/%s/finance";

    public static final String load_user_activity = "/group/activity/user/%s"; // #userId
    public static final String load_group_activity = "/group/activity/%s/%s"; // /#userId/#groupId
    public static final String create_group = "/group/new";
    public static final String search_group = "/group/search/%s/%s"; // #UserId/ #SearchText
    public static final String join_group = "/group/join/%s/%s"; // #userId/#groupId
    public static final String load_user_finance_by_group = "/group/finance/%s/%s";// /finance/#userId/#groupId
    public static final String load_group_users = "/group/users/%s";// /#groupId
    public static final String send_group_charge = "/group/charge";

    public static String buildPath(String path, Object ... args) {
        return String.format(path, args);
    }
}
