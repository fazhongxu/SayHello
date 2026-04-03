package com.xxl.sayhello.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MediaDownloader {

    public interface DownloadCallback {
        void onSuccess(Uri uri);
        void onFailure(Exception e);
        void onProgress(int progress);
    }

    public static void downloadImage(Context context, String imageUrl, DownloadCallback callback) {
        new DownloadTask(context, callback, "jpg").execute(imageUrl);
    }

    public static void downloadVideo(Context context, String videoUrl, DownloadCallback callback) {
        new DownloadTask(context, callback, "mp4").execute(videoUrl);
    }

    private static class DownloadTask extends AsyncTask<String, Integer, Uri> {
        private final Context context;
        private final DownloadCallback callback;
        private final String extension;
        private Exception exception;

        DownloadTask(Context context, DownloadCallback callback, String extension) {
            this.context = context;
            this.callback = callback;
            this.extension = extension;
        }

        @Override
        protected Uri doInBackground(String... urls) {
            String urlStr = urls[0];
            try {
                URL url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                int fileLength = connection.getContentLength();
                InputStream input = connection.getInputStream();

                File file = createTempFile(context, extension);
                FileOutputStream output = new FileOutputStream(file);

                byte[] data = new byte[1024];
                long total = 0;
                int count;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    if (fileLength > 0) {
                        publishProgress((int) (total * 100 / fileLength));
                    }
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();

                return Uri.fromFile(file);
            } catch (Exception e) {
                exception = e;
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (callback != null) {
                callback.onProgress(values[0]);
            }
        }

        @Override
        protected void onPostExecute(Uri uri) {
            if (callback != null) {
                if (uri != null) {
                    callback.onSuccess(uri);
                } else {
                    callback.onFailure(exception);
                }
            }
        }
    }

    private static File createTempFile(Context context, String extension) {
        File directory = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "share");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return new File(directory, "temp_" + System.currentTimeMillis() + "." + extension);
    }

    public static Bitmap getBitmapFromUri(Context context, Uri uri) {
        try {
            return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isLocalFile(Uri uri) {
        return uri != null && ("file".equals(uri.getScheme()) || "content".equals(uri.getScheme()));
    }

    public static boolean isUrl(String url) {
        return url != null && (url.startsWith("http://") || url.startsWith("https://"));
    }
}