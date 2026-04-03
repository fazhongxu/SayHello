package com.xxl.sayhello.share;

public enum SharePlatform {
    WECHAT_FRIEND("微信好友", 0),
    WECHAT_MOMENTS("朋友圈", 1),
    SYSTEM("系统分享", 2);

    private final String name;
    private final int iconRes;

    SharePlatform(String name, int iconRes) {
        this.name = name;
        this.iconRes = iconRes;
    }

    public String getName() {
        return name;
    }

    public int getIconRes() {
        return iconRes;
    }
}