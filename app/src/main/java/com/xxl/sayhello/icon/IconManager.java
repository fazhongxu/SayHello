package com.xxl.sayhello.icon;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

public class IconManager {
    private static final String TAG = "IconManager";
    private static final String DEFAULT_ICON = "default";
    private static final String ALTERNATE_ICON = "alternate";
    
    // 切换到默认图标
    public static void setDefaultIcon(Context context) {
        setIcon(context, DEFAULT_ICON);
    }
    
    // 切换到备用图标
    public static void setAlternateIcon(Context context) {
        setIcon(context, ALTERNATE_ICON);
    }
    
    // 切换图标
    private static void setIcon(Context context, String iconType) {
        PackageManager pm = context.getPackageManager();
        
        try {
            // 先禁用所有别名，确保只有一个图标
            disableComponent(context, "com.xxl.sayhello.DefaultIconAlias");
            disableComponent(context, "com.xxl.sayhello.AlternateIconAlias");
            
            // 启用指定的别名
            if (ALTERNATE_ICON.equals(iconType)) {
                enableComponent(context, "com.xxl.sayhello.AlternateIconAlias");
            } else {
                enableComponent(context, "com.xxl.sayhello.DefaultIconAlias");
            }
            
            // 保存当前图标类型
            saveCurrentIconType(context, iconType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // 启用组件
    private static void enableComponent(Context context, String componentName) {
        try {
            ComponentName component = new ComponentName(context, componentName);
            PackageManager pm = context.getPackageManager();
            pm.setComponentEnabledSetting(component,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // 禁用组件
    private static void disableComponent(Context context, String componentName) {
        try {
            ComponentName component = new ComponentName(context, componentName);
            PackageManager pm = context.getPackageManager();
            pm.setComponentEnabledSetting(component,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // 保存当前图标类型
    private static void saveCurrentIconType(Context context, String iconType) {
        SharedPreferences prefs = context.getSharedPreferences("icon_prefs", Context.MODE_PRIVATE);
        prefs.edit().putString("current_icon", iconType).apply();
    }
    
    // 获取当前图标类型
    public static String getCurrentIconType(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("icon_prefs", Context.MODE_PRIVATE);
        return prefs.getString("current_icon", DEFAULT_ICON);
    }
    
    // 检查是否是默认图标
    public static boolean isDefaultIcon(Context context) {
        return DEFAULT_ICON.equals(getCurrentIconType(context));
    }
}