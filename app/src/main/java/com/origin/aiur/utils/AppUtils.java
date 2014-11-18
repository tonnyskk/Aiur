package com.origin.aiur.utils;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.origin.aiur.R;
import com.origin.aiur.app.AiurApplication;
import com.origin.aiur.dao.IdentityDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.text.DecimalFormat;
import java.util.UUID;

import javax.crypto.Cipher;

/**
 * Created by dongjia on 10/22/2014.
 */
public class AppUtils {
    private static final String KEY_ALGORITHM = "RSA";
    private static final int MAX_ENCRYPT_BLOCK = 117;
    public static final String CHARSET = "UTF-8";

    public static String generateDeviceId() {

        TelephonyManager telephonyManager = (TelephonyManager) AiurApplication.getInstance().getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = telephonyManager.getDeviceId();
        String serial = telephonyManager.getSimSerialNumber();
        String androidId = Settings.Secure.getString(AiurApplication.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
        UUID deviceUid = new UUID(androidId.hashCode(), ((long) deviceId.hashCode() << 32) | serial.hashCode());
        return deviceUid.toString();
    }

    public static boolean isEmpty(String param) {
        if (param == null || param.trim().length() <= 0 || param.trim().equalsIgnoreCase("null")) {
            return true;
        }
        return false;
    }

    public static String getJsonString(JSONObject response, String key) {
        if (response == null || key == null) {
            return null;
        }
        try {
            if (response.has(key)) {
                return response.getString(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getJsonObject(JSONObject response, String key) {
        if (response == null || key == null) {
            return null;
        }
        try {
            if (response.has(key)) {
                return response.getJSONObject(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONArray getJsonArray(JSONObject response, String key) {
        if (response == null || key == null) {
            return null;
        }
        try {
            if (response.has(key)) {
                return response.getJSONArray(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getJsonInt(JSONObject response, String key) {
        if (response == null || key == null) {
            return -1;
        }
        if (response != null && response.has(key)) {
            try {
                return response.getInt(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public static String encryptKey(String encrypt) {
        String publicKey = IdentityDao.getInstance().getKey();
        ALogger.log(ALogger.LogPriority.debug, AppUtils.class, "Encrypt message with key > " + publicKey);
        if (isEmpty(publicKey)) {
            return encrypt;
        }

        try {
            return Base64Util.encode(encrypt.getBytes());
            //return Base64Util.encode(encryptByPublicKey(encrypt.getBytes(), publicKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrypt;
    }

    private static byte[] encryptByPublicKey(byte[] data, String publicKey)
            throws Exception {
        byte[] keyBytes = Base64Util.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    public static int getResIdByStatusCode(int statusCode) {
        switch (statusCode) {
            case -1:
                return R.string.invalid_response_format;
            case 4000:
                return R.string.error_msg_4000;
            case 4001:
                return R.string.error_msg_4001;
            case 4002:
                return R.string.error_msg_4002;
            case 4003:
                return R.string.error_msg_4003;
            case 4004:
                return R.string.error_msg_4004;
            case 4005:
                return R.string.error_msg_4005;
            case 4006:
                return R.string.error_msg_4006;
            case 4007:
                return R.string.error_msg_4007;
            case 4008:
                return R.string.error_msg_4008;
            case 4009:
                return R.string.error_msg_4009;
            case 4010:
                return R.string.error_msg_4010;
            case 4011:
                return R.string.error_msg_4011;
            case 4012:
                return R.string.error_msg_4012;
        }
        return R.string.error_msg;
    }

    public static String formatMoney(double money) {
        DecimalFormat format = new DecimalFormat("##,##0.00");
        return format.format(money);
    }
}
