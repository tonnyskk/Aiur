package com.origin.aiur.http;

/**
 * Created by dongjia on 9/24/2014.
 */
public class HttpUtils {
    public static final String BASE_URL = "http://192.168.1.104:8080/aiur/rest";

    public static final String user_login = "/login";
    public static final String user_reg= "/reg";
    public static final String load_user_group = "";
    public static final String load_user_group_activity = "";
    public static final String validate_token = "/user/status/%s";

    public static String buildPath(String path, Object ... args) {
        return String.format(path, args);
    }
}
