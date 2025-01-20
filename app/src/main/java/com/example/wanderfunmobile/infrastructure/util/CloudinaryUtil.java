package com.example.wanderfunmobile.infrastructure.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.util.Log;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CloudinaryUtil {
    public static void init(Context context) {
        Map<String, Object> config = new HashMap<>();
        config.put("cloud_name", "justvvu");
        config.put("secure", true);
        MediaManager.init(context, config);
    }

    public static void uploadImageToCloudinary(Context context, Uri imageUri, String fileName) {
        try {
            File imageFile = resizeAndCompressImage(context, imageUri, fileName);

            MediaManager.get().upload(imageFile.getAbsolutePath())
                    .option("folder", "your_folder_name") // Tùy chọn: Đặt thư mục lưu trữ trên Cloudinary
                    .callback(new UploadCallback() {
                        @Override
                        public void onStart(String requestId) {
                            Log.d("Cloudinary", "Upload started for " + imageUri.toString());
                        }

                        @Override
                        public void onProgress(String requestId, long bytes, long totalBytes) {
                            Log.d("Cloudinary", "Uploading: " + bytes + "/" + totalBytes);
                        }

                        @Override
                        public void onSuccess(String requestId, Map resultData) {
                            String secureUrl = (String) resultData.get("secure_url");
                            Log.d("Cloudinary", "Upload success: " + secureUrl);
                        }

                        @Override
                        public void onError(String requestId, ErrorInfo error) {
                            Log.e("Cloudinary", "Upload error: " + error.getDescription());
                        }

                        @Override
                        public void onReschedule(String requestId, ErrorInfo error) {
                            Log.e("Cloudinary", "Upload rescheduled: " + error.getDescription());
                        }
                    }).dispatch();
        } catch (IOException e) {
            Log.e("Cloudinary", "Error converting URI: " + e.getMessage());
        }
    }

    private static File resizeAndCompressImage(Context context, Uri uri, String fileName) throws IOException {
        ImageDecoder.Source source = ImageDecoder.createSource(context.getContentResolver(), uri);
        Bitmap bitmap = ImageDecoder.decodeBitmap(source);
        File tempFile = File.createTempFile(fileName + "-compressed", ".jpg", context.getCacheDir());

        FileOutputStream fos = new FileOutputStream(tempFile);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
        fos.close();

        return tempFile;
    }
}
