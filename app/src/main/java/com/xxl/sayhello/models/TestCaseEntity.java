package com.xxl.sayhello.models;

public class TestCaseEntity {
    private String previewUrl;
    private String title;
    private int width;
    private int height;
    private String promptTxt;
    private int funId;
    private String descTxt;
    private int picNumLimit;

    public TestCaseEntity() {
    }

    public TestCaseEntity(String previewUrl, String title, int width, int height) {
        this.previewUrl = previewUrl;
        this.title = title;
        this.width = width;
        this.height = height;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getPromptTxt() {
        return promptTxt;
    }

    public void setPromptTxt(String promptTxt) {
        this.promptTxt = promptTxt;
    }

    public int getFunId() {
        return funId;
    }

    public void setFunId(int funId) {
        this.funId = funId;
    }

    public String getDescTxt() {
        return descTxt;
    }

    public void setDescTxt(String descTxt) {
        this.descTxt = descTxt;
    }

    public int getPicNumLimit() {
        return picNumLimit;
    }

    public void setPicNumLimit(int picNumLimit) {
        this.picNumLimit = picNumLimit;
    }
}