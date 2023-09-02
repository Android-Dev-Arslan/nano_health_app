package com.example.nanohealthsuiteapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.nanohealthsuiteapp.BuildConfig;


public class SharedPrefHelper {
    private static final int MODE = 0;

    private SharedPrefHelper() {
    }

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(BuildConfig.APPLICATION_ID, 0);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

    public static void writeInteger(Context context, String str, int i) {
        getEditor(context).putInt(str, i).apply();
    }

    public static int readInteger(Context context, String str) {
        return getPreferences(context).getInt(str, 0);
    }

    public static void writeString(Context context, String key, String value) {
        getEditor(context).putString(key, value).apply();
    }

    public static String readString(Context context, String str) {
        return getPreferences(context).getString(str, "");
    }

    public static void clearAll(Context context) {
        getEditor(context).clear().apply();
    }

    public static void writeBoolean(Context context, String str, boolean z) {
        getPreferences(context).edit().putBoolean(str, z).apply();
    }

    public static boolean readBoolean(Context context, String str) {
        return getPreferences(context).getBoolean(str, false);
    }
}