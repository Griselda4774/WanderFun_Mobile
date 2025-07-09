package com.example.wanderfunmobile.core.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.wanderfunmobile.R;

public class BitMapUtil {
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888
        );
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap convertVectorToBitmap(Context context, @DrawableRes int vectorResId, int width, int height) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        if (vectorDrawable == null) {
            throw new IllegalArgumentException("Drawable not found with id: " + vectorResId);
        }

        vectorDrawable.setBounds(0, 0, width, height);

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);

        return bitmap;
    }

    public static void getBitMapFromUrl(Context context, String url, BitmapCallback callback) {
        Glide.with(context).asBitmap().load(url).into(
                new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        callback.onBitmapLoaded(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                }
        );
    }

    public static Bitmap addCircularBorder(Bitmap originalBitmap, int borderSize, int borderColor) {
        int diameter = Math.max(originalBitmap.getWidth(), originalBitmap.getHeight());
        int newDiameter = diameter + 2 * borderSize;

        Bitmap output = Bitmap.createBitmap(newDiameter, newDiameter, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setColor(borderColor);
        canvas.drawCircle(newDiameter / 2f, newDiameter / 2f, newDiameter / 2f, paint);

        Rect srcRect = new Rect(0, 0, originalBitmap.getWidth(), originalBitmap.getHeight());
        Rect destRect = new Rect(borderSize, borderSize, newDiameter - borderSize, newDiameter - borderSize);
        canvas.drawBitmap(originalBitmap, srcRect, destRect, null);

        return output;
    }


    public interface BitmapCallback {
        void onBitmapLoaded(Bitmap bitmap);
    }
}
