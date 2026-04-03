package com.xxl.sayhello.share;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class ShareManager {
    private static final String TAG = "ShareManager";
    private static ShareManager instance;
    private final Map<SharePlatform, ShareStrategy> strategies;

    private ShareManager() {
        strategies = new HashMap<>();
        // 注册分享策略
        registerStrategies();
    }

    public static ShareManager getInstance() {
        if (instance == null) {
            synchronized (ShareManager.class) {
                if (instance == null) {
                    instance = new ShareManager();
                }
            }
        }
        return instance;
    }

    private void registerStrategies() {
        // 注册微信好友分享策略
        strategies.put(SharePlatform.WECHAT_FRIEND, new WeChatShareStrategy());
        // 注册朋友圈分享策略
        strategies.put(SharePlatform.WECHAT_MOMENTS, new WeChatMomentsShareStrategy());
        // 注册系统分享策略
        strategies.put(SharePlatform.SYSTEM, new SystemShareStrategy());
    }

    public void share(Activity activity, SharePlatform platform, ShareContent content) {
        ShareStrategy strategy = strategies.get(platform);
        if (strategy == null) {
            Log.e(TAG, "Share strategy not found for platform: " + platform);
            return;
        }

        if (!strategy.isAvailable()) {
            Log.e(TAG, "Share platform not available: " + platform);
            return;
        }

        // 处理媒体下载
        handleMediaDownload(activity, platform, content);
    }

    private void handleMediaDownload(Activity activity, SharePlatform platform, ShareContent content) {
        // 处理图片下载
        if (content.getImageUrl() != null && !MediaDownloader.isLocalFile(Uri.parse(content.getImageUrl()))) {
            MediaDownloader.downloadImage(activity, content.getImageUrl(), new MediaDownloader.DownloadCallback() {
                @Override
                public void onSuccess(Uri uri) {
                    ShareContent newContent = new ShareContent.Builder()
                            .setTitle(content.getTitle())
                            .setContent(content.getContent())
                            .setUrl(content.getUrl())
                            .setImageUri(uri)
                            .setVideoUri(content.getVideoUri())
                            .setVideoUrl(content.getVideoUrl())
                            .build();
                    doShare(activity, platform, newContent);
                }

                @Override
                public void onFailure(Exception e) {
                    Log.e(TAG, "Image download failed: " + e.getMessage());
                    // 继续分享，不包含图片
                    doShare(activity, platform, content);
                }

                @Override
                public void onProgress(int progress) {
                    // 可以在这里显示下载进度
                }
            });
        } else if (content.getVideoUrl() != null && !MediaDownloader.isLocalFile(Uri.parse(content.getVideoUrl()))) {
            // 处理视频下载
            MediaDownloader.downloadVideo(activity, content.getVideoUrl(), new MediaDownloader.DownloadCallback() {
                @Override
                public void onSuccess(Uri uri) {
                    ShareContent newContent = new ShareContent.Builder()
                            .setTitle(content.getTitle())
                            .setContent(content.getContent())
                            .setUrl(content.getUrl())
                            .setImageUri(content.getImageUri())
                            .setVideoUri(uri)
                            .build();
                    doShare(activity, platform, newContent);
                }

                @Override
                public void onFailure(Exception e) {
                    Log.e(TAG, "Video download failed: " + e.getMessage());
                    // 继续分享，不包含视频
                    doShare(activity, platform, content);
                }

                @Override
                public void onProgress(int progress) {
                    // 可以在这里显示下载进度
                }
            });
        } else {
            // 没有需要下载的媒体，直接分享
            doShare(activity, platform, content);
        }
    }

    private void doShare(Activity activity, SharePlatform platform, ShareContent content) {
        ShareStrategy strategy = strategies.get(platform);
        if (strategy != null) {
            strategy.share(activity, content);
        }
    }

    public void showShareDialog(Activity activity, ShareContent content) {
        ShareDialog.show(activity, content, platform -> share(activity, platform, content));
    }

    // 微信好友分享策略（占位实现）
    private static class WeChatShareStrategy implements ShareStrategy {
        @Override
        public void share(Activity activity, ShareContent content) {
            // 这里将由外部实现微信分享逻辑
            Log.d(TAG, "Share to WeChat friend: " + content.getTitle());
        }

        @Override
        public boolean isAvailable() {
            // 这里将检查微信是否安装
            return true; // 暂时返回true
        }
    }

    // 朋友圈分享策略（占位实现）
    private static class WeChatMomentsShareStrategy implements ShareStrategy {
        @Override
        public void share(Activity activity, ShareContent content) {
            // 这里将由外部实现朋友圈分享逻辑
            Log.d(TAG, "Share to WeChat moments: " + content.getTitle());
        }

        @Override
        public boolean isAvailable() {
            // 这里将检查微信是否安装
            return true; // 暂时返回true
        }
    }

    // 系统分享策略
    private static class SystemShareStrategy implements ShareStrategy {
        @Override
        public void share(Activity activity, ShareContent content) {
            // 实现系统分享逻辑
            android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(android.content.Intent.EXTRA_TITLE, content.getTitle());
            intent.putExtra(android.content.Intent.EXTRA_TEXT, content.getContent() + " " + content.getUrl());
            activity.startActivity(android.content.Intent.createChooser(intent, "分享到"));
        }

        @Override
        public boolean isAvailable() {
            return true;
        }
    }
}