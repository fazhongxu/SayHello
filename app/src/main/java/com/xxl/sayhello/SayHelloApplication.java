package com.xxl.sayhello;

import android.app.Application;
import android.widget.Toast;

import com.xxl.sayhello.utils.AopHelper;
import com.xxl.sayhello.utils.VipHelper;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class SayHelloApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AopHelper.init(this);
        VipHelper.init(this);
        VipHelper.setCallback(new VipHelper.Callback() {
            @Override
            public void onNotLoggedIn() {
                Toast.makeText(SayHelloApplication.this,
                        getString(R.string.vip_not_logged_in), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNotVip() {
                Toast.makeText(SayHelloApplication.this,
                        getString(R.string.vip_not_vip), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
