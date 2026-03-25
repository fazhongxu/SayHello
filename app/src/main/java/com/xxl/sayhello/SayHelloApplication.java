package com.xxl.sayhello;

import android.app.Application;
import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class SayHelloApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }
}