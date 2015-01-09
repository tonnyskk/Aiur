package com.origin.aiur.http;

/**
 * Created by dongjia on 9/24/2014.
 */
public class HttpUtils {
    private static final String HOME_URL = "http://192.168.1.105:8080/aiur/rest";
    private static final String WORK_URL = "http://54.175.121.4:8080/aiur/rest";

    public static final String BASE_URL = WORK_URL;

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

    // group charge tab
    public static final String send_group_charge = "/group/charge";
    // group prepay tab
    public static final String send_group_user_prepay = "/group/prepay";

    // Group manage tab
    public static final String load_user_request_event = "/group/request/%s";// /#groupId

    public static final String send_manage_finance_event_approve = "/group/manageFinance/%s?type=approve"; // /#groupId
    public static final String send_manage_finance_event_reject = "/group/manageFinance/%s?type=reject"; // /#groupId

    public static final String send_manage_user_request_approve = "/group/manageUser?type=approve";
    public static final String send_manage_user_request_reject = "/group/manageUser?type=reject";
    public static final String send_manage_user_request_remove = "/group/manageUser?type=remove";

    public static String buildPath(String path, Object ... args) {
        return String.format(path, args);
    }
}
