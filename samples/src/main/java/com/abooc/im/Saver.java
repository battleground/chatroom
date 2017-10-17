package com.abooc.im;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dayu on 2017/10/17.
 */

public class Saver {

    public static void save(Context context, String phone) {
        SharedPreferences preferences = context.getSharedPreferences("facetime", Context.MODE_APPEND);
        preferences.edit().putString("uid", phone).apply();
    }

    public static String read(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("facetime", Context.MODE_APPEND);
        return preferences.getString("uid", null);
    }

    public static void remove(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("facetime", Context.MODE_APPEND);
        preferences.edit().remove("uid").apply();
    }

}
