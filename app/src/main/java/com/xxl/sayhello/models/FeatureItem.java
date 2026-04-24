package com.xxl.sayhello.models;

public class FeatureItem {
    private String title;
    private Class<?> targetClass;

    public FeatureItem(String title, Class<?> targetClass) {
        this.title = title;
        this.targetClass = targetClass;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }
}