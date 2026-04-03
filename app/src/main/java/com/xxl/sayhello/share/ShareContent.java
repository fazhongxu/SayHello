package com.xxl.sayhello.share;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;

public class ShareContent implements Serializable {
    private String title;
    private String content;
    private String url;
    private Bitmap image;
    private Uri imageUri;
    private String imageUrl;
    private Uri videoUri;
    private String videoUrl;

    private ShareContent(Builder builder) {
        this.title = builder.title;
        this.content = builder.content;
        this.url = builder.url;
        this.image = builder.image;
        this.imageUri = builder.imageUri;
        this.imageUrl = builder.imageUrl;
        this.videoUri = builder.videoUri;
        this.videoUrl = builder.videoUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public Bitmap getImage() {
        return image;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public Uri getVideoUri() {
        return videoUri;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public static class Builder {
        private String title;
        private String content;
        private String url;
        private Bitmap image;
        private Uri imageUri;
        private String imageUrl;
        private Uri videoUri;
        private String videoUrl;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setImage(Bitmap image) {
            this.image = image;
            return this;
        }

        public Builder setImageUri(Uri imageUri) {
            this.imageUri = imageUri;
            return this;
        }

        public Builder setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder setVideoUri(Uri videoUri) {
            this.videoUri = videoUri;
            return this;
        }

        public Builder setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
            return this;
        }

        public ShareContent build() {
            return new ShareContent(this);
        }
    }
}