package com.origin.aiur.dao.storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2014/10/22.
 */
public class APrivateMapStorage implements IAStorage{
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public APrivateMapStorage(Context context, String storageName) {
        this.sharedPreferences = context.getSharedPreferences(storageName, Context.MODE_PRIVATE);
        this.editor = this.sharedPreferences.edit();
    }

    public void clear() {
        this.editor.clear();
        this.editor.commit();
    }

    public String getString(String key, String defaultValue) {
        return this.sharedPreferences.getString(key, defaultValue);
    }

    public void setString(String key, String value) {
        this.editor.putString(key, value);
        this.editor.commit();
    }

    public long getLong(String key, long defaultValue) {
        return this.sharedPreferences.getLong(key, defaultValue);
    }

    public void setLong(String key, long value) {
        this.editor.putLong(key, value);
        this.editor.commit();
    }

    public void remove(String key) {
        this.editor.remove(key);
        this.editor.commit();
    }
}
