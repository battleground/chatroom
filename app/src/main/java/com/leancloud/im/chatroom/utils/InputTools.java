package com.leancloud.im.chatroom.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * @author zhangjunpu
 * @date 2016/12/9
 */

public class InputTools {

    public static boolean hideInputMethod(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            return imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        return false;
    }

    public static boolean showInputMethod(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            return imm.showSoftInput(view, 0);
        }

        return false;
    }

    public static boolean showForceInputMethod(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            return imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
        return false;
    }

}
