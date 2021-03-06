package com.origin.aiur.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Patterns;

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
        String deviceId = "" + telephonyManager.getDeviceId();
        String serial = "" + telephonyManager.getSimSerialNumber();
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

    public static boolean isValidEmail(CharSequence email) {
        if (email == null) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
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
            case 4013:
                return R.string.error_msg_4013;
        }
        return R.string.error_msg;
    }

    public static String formatMoney(double money) {
        DecimalFormat format = new DecimalFormat("##,##0.00");
        return format.format(money);
    }

    public static Drawable getAvatar(String avatarData) {
        if (isEmpty(avatarData)) {
            return  null;
        }
        try {
            byte[] avatarByte = Base64Util.decode(avatarData.getBytes());
            Bitmap bitmap = BitmapFactory.decodeByteArray(avatarByte, 0, avatarByte.length);
            return new BitmapDrawable(toRoundBitmap(bitmap, true));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    @SuppressWarnings("SuspiciousNameCombination")
    public static Bitmap toRoundBitmap(Bitmap bitmap, boolean recycleOriginalBmp) {
        if (bitmap == null || bitmap.getWidth() == 0 || bitmap.getHeight() == 0 || bitmap.isRecycled()) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2.0f;
            float clip = (height - width) / 2.0f;
            top = clip;
            bottom = height - clip;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2.0f;
            float clip = (width - height) / 2.0f;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }
        Bitmap output = null;
        try {
            output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
            final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);

            paint.setAntiAlias(true);

            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawCircle(roundPx, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, src, dst, paint);
        } catch (Throwable t) {
            ALogger.log(ALogger.LogPriority.error, AppUtils.class, "toRoundBitmap()", t);
        } finally {
            // recycle the original bitmap
            if (recycleOriginalBmp && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
        return output;
    }

}
