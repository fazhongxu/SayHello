package com.xxl.sayhello.share;

import android.app.Activity;

public interface ShareStrategy {
    void share(Activity activity, ShareContent content);
    boolean isAvailable();
}