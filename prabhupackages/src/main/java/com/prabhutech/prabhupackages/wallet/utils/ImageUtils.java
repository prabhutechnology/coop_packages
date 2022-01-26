package com.prabhutech.prabhupackages.wallet.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.prabhutech.prabhupackages.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

public class ImageUtils {
    private static final String TAG = "ImageUtils";

    /**
     * Resize image is its height or width is greater than 800px
     * Reduce image quality to --
     */
    public static Bitmap scaleDownLargeBitmap(@NonNull Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float aspectRatio = (float) width / (float) height;

        Bitmap resultBitmap = bitmap;
//        Log.d(TAG, "Before compression " + getImageFileSize(bitmap) + " Height " + bitmap.getHeight() + " Width " + bitmap.getWidth());
        if (aspectRatio > 1 && width > 800) {

            // For wide images - set maximum width to 800px and scale down height maintaining aspect ratio
            int scaledWidth = 800;
            int scaledHeight = Math.round(scaledWidth / aspectRatio);
            resultBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, false);

        } else if (aspectRatio < 1 && height > 800) {

            // For tall images
            int scaledHeight = 800;
            int scaledWidth = Math.round(scaledHeight * aspectRatio);
            resultBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, false);
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);


//        Log.d(TAG, "After compression " + getImageFileSize(resultBitmap) + " Height " + resultBitmap.getHeight() + " Width " + resultBitmap.getWidth());

        return resultBitmap;
    }

    /**
     * Returns file size in kb
     */
    public static float getImageFileSize(@NonNull Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        DecimalFormat df = new DecimalFormat("#.##");

        return Float.parseFloat(df.format(imageInByte.length / 1024f));
    }

    public static Uri saveBitmapAndGetUri(Context context, Bitmap bitmap, String fileName) {
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Toast.makeText(context, "Cannot access device storage", Toast.LENGTH_SHORT).show();
            return null;
        }

        try {

            File directory = new File(context.getExternalFilesDir(""), "data/images");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File file = new File(context.getExternalFilesDir("data/images"), fileName); // the File to save
            FileOutputStream fOut = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush(); // Not really required
            fOut.close(); // do not forget to close the stream

            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());

            return Uri.fromFile(file);
        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static String getEncodedImageFromBitmap(Bitmap bitmap) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);      //Encoded image
        } catch (Exception e) {
            return "";
        }
    }

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static Drawable scaleDrawable(Drawable drawable) {

        return new ScaleDrawable(drawable, 0, 40, 40);
    }

    /**
     * Load image into imageview using glide,
     * Add placeholder image
     *
     * @param context
     * @param url
     * @param image
     */
    public static void loadImage(Context context, String url, ImageView image) {

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.img_placeholder);
        requestOptions.error(R.drawable.img_placeholder);

        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(url)
                .into(image);
    }

    public static void cropTop(Context context, ImageView imageView) {
        if (imageView == null || imageView.getDrawable() == null) return;

        final Matrix matrix = imageView.getImageMatrix();
        final float imageWidth = imageView.getDrawable().getIntrinsicWidth();
        final int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        final float scaleRatio = screenWidth / imageWidth;
        matrix.postScale(scaleRatio, scaleRatio);
        imageView.setImageMatrix(matrix);
    }
}
