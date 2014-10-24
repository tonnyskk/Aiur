package com.origin.aiur.dao;

import com.origin.aiur.dao.storage.ASQLMapStorage;
import com.origin.aiur.utils.ALogger;
import com.origin.aiur.utils.AppUtils;
import com.origin.aiur.vo.Finance;
import com.origin.aiur.vo.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by dongjia on 10/24/2014.
 */
public class FinanceDao {

    private static FinanceDao instance = new FinanceDao();

    private enum Keys {
        UserFinance
    }

    public static FinanceDao getInstance() {
        return instance;
    }


    public void saveUserFinance(Finance finance) {
        try {
            getStore().put(Keys.UserFinance.name(), finance.toJsonObject().toString().getBytes(AppUtils.CHARSET));
        } catch (JSONException e) {
            ALogger.log(ALogger.LogPriority.error, FinanceDao.class, "Save UserFinance failed for JSON parse!", e);
        } catch (UnsupportedEncodingException e) {
            ALogger.log(ALogger.LogPriority.error, FinanceDao.class, "Save UserFinance failed for Charset error!", e);
        }
    }

    public Finance getUserFinance() {
        try {
            byte[] data = getStore().get(Keys.UserFinance.name());
            if (data != null) {
                Finance finance = new Finance(new JSONObject(new String(data, "utf-8")));
                return finance;
            }
        } catch (JSONException e) {
            ALogger.log(ALogger.LogPriority.error, FinanceDao.class, "Get UserFinance failed for JSON parse!", e);
        } catch (UnsupportedEncodingException e) {
            ALogger.log(ALogger.LogPriority.error, FinanceDao.class, "Get UserFinance failed for Charset error!", e);
        }
        return null;
    }

    public ASQLMapStorage getStore() {
        return AStoreManager.getInstance().getFinanceStore();
    }


}
