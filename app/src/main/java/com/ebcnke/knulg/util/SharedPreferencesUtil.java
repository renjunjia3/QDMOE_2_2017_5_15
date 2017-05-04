package com.ebcnke.knulg.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @FileName:com.scene.baselib.util.SharedPreferencesUtil.java
 * @功能描述：SharedPreferences工具类
 * @author: scene
 * @date: 2016-07-25 16:09
 */
public class SharedPreferencesUtil {

    private static final String SHAREPREFERENCES_NAME = "scene.ini";

    private static SharedPreferences sharedPreferences = null;

    private static synchronized SharedPreferences getSharedPreferences(
            Context context, String name) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(name,
                    Context.MODE_APPEND);
        }
        return sharedPreferences;
    }

    public static void deleteByKey(Context context, String key) {
        SharedPreferences sharedPreferences = getSharedPreferences(context, SHAREPREFERENCES_NAME);
        Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    public static void putString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences(context, SHAREPREFERENCES_NAME);
        Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key,
                                   String defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(context, SHAREPREFERENCES_NAME);
        return sharedPreferences.getString(key, defaultValue);
    }

    public static void putBoolean(Context context, String key, Boolean value) {
        SharedPreferences sharedPreferences = getSharedPreferences(context, SHAREPREFERENCES_NAME);
        Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static Boolean getBoolean(Context context, String key,
                                     Boolean defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(context, SHAREPREFERENCES_NAME);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public static void putLong(Context context, String key, long value) {
        SharedPreferences sharedPreferences = getSharedPreferences(context, SHAREPREFERENCES_NAME);
        Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static long getLong(Context context, String key, long defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(context, SHAREPREFERENCES_NAME);
        return sharedPreferences.getLong(key, defaultValue);
    }

    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(context, SHAREPREFERENCES_NAME);
        return sharedPreferences.getInt(key, defaultValue);
    }

    public static void putInt(Context context, String key, int value) {
        SharedPreferences sharedPreferences = getSharedPreferences(context, SHAREPREFERENCES_NAME);
        Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }


}
