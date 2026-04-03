package com.xxl.sayhello.share;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.xxl.sayhello.R;

public class ShareExample {

    /**
     * 分享文本和链接
     */
    public static void shareText(Activity activity) {
        ShareContent content = new ShareContent.Builder()
                .setTitle("分享标题")
                .setContent("这是分享的内容")
                .setUrl("https://example.com")
                .build();

        ShareManager.getInstance().showShareDialog(activity, content);
    }

    /**
     * 分享本地图片
     */
    public static void shareLocalImage(Activity activity, Bitmap bitmap) {
        ShareContent content = new ShareContent.Builder()
                .setTitle("分享图片")
                .setContent("这是一张图片")
                .setUrl("https://example.com")
                .setImage(bitmap)
                .build();

        ShareManager.getInstance().showShareDialog(activity, content);
    }

    /**
     * 分享网络图片（会自动下载）
     */
    public static void shareNetworkImage(Activity activity) {
        ShareContent content = new ShareContent.Builder()
                .setTitle("分享网络图片")
                .setContent("这是一张网络图片")
                .setUrl("https://example.com")
                .setImageUrl("https://example.com/image.jpg")
                .build();

        ShareManager.getInstance().showShareDialog(activity, content);
    }

    /**
     * 分享网络视频（会自动下载）
     */
    public static void shareNetworkVideo(Activity activity) {
        ShareContent content = new ShareContent.Builder()
                .setTitle("分享视频")
                .setContent("这是一段视频")
                .setUrl("https://example.com")
                .setVideoUrl("https://example.com/video.mp4")
                .build();

        ShareManager.getInstance().showShareDialog(activity, content);
    }

    /**
     * 直接分享到微信好友
     */
    public static void shareToWeChat(Activity activity) {
        ShareContent content = new ShareContent.Builder()
                .setTitle("微信分享")
                .setContent("这是分享到微信的内容")
                .setUrl("https://example.com")
                .build();

        ShareManager.getInstance().share(activity, SharePlatform.WECHAT_FRIEND, content);
    }

    /**
     * 直接分享到朋友圈
     */
    public static void shareToWeChatMoments(Activity activity) {
        ShareContent content = new ShareContent.Builder()
                .setTitle("朋友圈分享")
                .setContent("这是分享到朋友圈的内容")
                .setUrl("https://example.com")
                .build();

        ShareManager.getInstance().share(activity, SharePlatform.WECHAT_MOMENTS, content);
    }

    /**
     * 分享组合内容（文本+图片+视频）
     */
    public static void shareCombinedContent(Activity activity, Bitmap bitmap) {
        ShareContent content = new ShareContent.Builder()
                .setTitle("组合分享")
                .setContent("这是一个包含图片和视频的分享")
                .setUrl("https://example.com")
                .setImage(bitmap)
                .setVideoUrl("https://example.com/video.mp4")
                .build();

        ShareManager.getInstance().showShareDialog(activity, content);
    }
}