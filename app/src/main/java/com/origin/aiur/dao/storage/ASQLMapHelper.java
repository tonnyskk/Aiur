package com.origin.aiur.dao.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.origin.aiur.app.AiurApplication;

/**
 * Created by Administrator on 2014/10/22.
 */
public class ASQLMapHelper extends SQLiteOpenHelper {
    private static final int DATA_BASE_VERSION = 1;

    private static class InnerHelper {
        private static ASQLMapHelper instance = new ASQLMapHelper(AiurApplication.getInstance().getApplicationContext(),  "AiurDatabase.db");
    }

    private ASQLMapHelper(Context context, String dbFileName)
    {
        super(context, dbFileName, null, DATA_BASE_VERSION);
    }

    public static SQLiteOpenHelper getInstance() {
        return InnerHelper.instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
