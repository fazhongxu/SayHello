package com.xxl.sayhello.utils;

import android.app.Application;
import android.content.ContentResolver;
import android.provider.Settings;
import android.widget.Toast;

import com.xxl.sayhello.R;

public class AopHelper {

    private static volatile Application sApp;

    public static void init(Application app) {
        sApp = app;
    }

    public static String getSecureString(ContentResolver resolver, String name) {
        String result = Settings.Secure.getString(resolver, name);
        if ("android_id".equals(name) && sApp != null) {
            Toast.makeText(sApp, sApp.getString(R.string.aop_android_id_detected), Toast.LENGTH_SHORT).show();
        }
        return result;
    }
}
