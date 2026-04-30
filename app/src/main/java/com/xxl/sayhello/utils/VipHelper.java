package com.xxl.sayhello.utils;

import android.app.Application;
import android.widget.Toast;

import com.xxl.sayhello.R;

public class VipHelper {

    public interface Callback {
        void onNotLoggedIn();
        void onNotVip();
    }

    private static volatile Application sApp;
    private static Callback callback;

    public static void init(Application app) {
        sApp = app;
    }

    public static void setCallback(Callback cb) {
        callback = cb;
    }

    public static boolean checkVip() {
        if (sApp == null) return false;

        if (!SharedPreferencesUtil.isLoggedIn(sApp)) {
            if (callback != null) {
                callback.onNotLoggedIn();
            } else {
                Toast.makeText(sApp, sApp.getString(R.string.vip_not_logged_in), Toast.LENGTH_SHORT).show();
            }
            return false;
        }

        if (!SharedPreferencesUtil.isVip(sApp)) {
            if (callback != null) {
                callback.onNotVip();
            } else {
                Toast.makeText(sApp, sApp.getString(R.string.vip_not_vip), Toast.LENGTH_SHORT).show();
            }
            return false;
        }

        return true;
    }
}
