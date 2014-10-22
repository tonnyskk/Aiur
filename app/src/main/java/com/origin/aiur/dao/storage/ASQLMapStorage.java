package com.origin.aiur.dao.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/10/22.
 */
public class ASQLMapStorage implements IAStorage {
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DATA = "data";
    private static final String COLUMN_SORT = "sort";

    private SQLiteOpenHelper sqliteHelper;
    private String storeName;
    private boolean isSortable;

    public ASQLMapStorage(SQLiteOpenHelper helper, String storeName) {
        this(helper, storeName, false);
    }

    public ASQLMapStorage(SQLiteOpenHelper helper, String storeName, boolean isSortable) {
        this.storeName = storeName;

        this.sqliteHelper = helper;

        this.isSortable = isSortable;

        load();
    }

    private void load() {
        this.createTable();
    }

    protected SQLiteDatabase getReadableDatabase() {
        return sqliteHelper.getReadableDatabase();
    }

    protected SQLiteDatabase getWritableDatabase() {
        return sqliteHelper.getWritableDatabase();
    }

    public String getStoreName() {
        return this.storeName;
    }

    @Override
    public void clear() {
        this.deleteTable();

    }

    public void put(String key, byte[] data) {
        this.put(key, data, -1);
    }

    public void put(String key, byte[] data, long orderColumnValue) {
        this.saveData(key, data, orderColumnValue);
    }

    public void remove(String key) {
        this.deleteData(key);
    }

    public byte[] get(String key) {
        return this.readData(key);
    }

    public List<byte[]> getAllData() {
        return list(false, 0, 0);
    }

    public List<byte[]> list(boolean isAscending, int startIndex, int count) {
        return this.readAllData(isAscending, startIndex, count);
    }

    protected void createTable() {
        try {
            SQLiteDatabase database = getWritableDatabase();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("CREATE TABLE IF NOT EXISTS ").append(this.storeName).append(" (")
                    .append(COLUMN_ID).append(" varchar(32) PRIMARY KEY, ")
                    .append(COLUMN_DATA).append(" byte[]");

            if (this.isSortable) {
                stringBuilder.append(", " + COLUMN_SORT + " integer");
            }

            stringBuilder.append(")");

            database.execSQL(stringBuilder.toString());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    protected void deleteTable() {
        try {
            SQLiteDatabase database = getWritableDatabase();
            database.delete(this.storeName, null, null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    protected void saveData(String id, byte[] data, long orderColumnValue) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, id);
        contentValues.put(COLUMN_DATA, data);
        if (this.isSortable) {
            contentValues.put(COLUMN_SORT, orderColumnValue);
        }

        try {
            SQLiteDatabase database = getWritableDatabase();
            database.replace(this.storeName, "", contentValues);
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            contentValues.clear();
        }
    }

    protected byte[] readData(String id) {
        byte[] data = null;
        Cursor c = null;
        try {
            SQLiteDatabase database = getReadableDatabase();
            c = database.query(this.storeName, new String[]{COLUMN_DATA}, COLUMN_ID + " = ?", new String[]{id}, null, null, null);
            if (c != null && c.getCount() >= 1 && c.moveToFirst()) {
                data = c.getBlob(0);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }

        return data;
    }

    protected List<byte[]> readAllData(boolean isAscending, int startIndex, int count) {
        List<byte[]> datas = new ArrayList<byte[]>();
        Cursor c = null;
        try {
            SQLiteDatabase database = getReadableDatabase();
            String orderBy = null;
            String limit = null;
            if (this.isSortable) {
                orderBy = COLUMN_SORT + (isAscending ? " asc" : " desc");
                if (count > 0) {
                    limit = " LIMIT " + startIndex + ", " + (startIndex + count);
                }
            }
            c = database.query(this.storeName, new String[]{COLUMN_DATA}, null, null, null, null, orderBy, limit);
            if (c.getCount() >= 1) {
                while (c.moveToNext()) {
                    datas.add(c.getBlob(0));
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }

        return datas;
    }

    protected void deleteData(String id) {
        try {
            SQLiteDatabase database = getWritableDatabase();
            database.delete(this.storeName, COLUMN_ID + " = ?", new String[]{id});
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
