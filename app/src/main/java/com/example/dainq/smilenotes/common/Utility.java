package com.example.dainq.smilenotes.common;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utility {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.FORMAT_DATE, Locale.US);

    public static List<String> createList(int n, String string) {
        List<String> list = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            list.add(string + " " + i);
        }

        return list;
    }

    public static boolean isEmptyString(String string) {
        return string.trim().isEmpty();
    }

    public static int createId(int id) {
        return id + 1;
    }

    public static void setTextViewDrawableColor(Context context, TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawablesRelative()) {
            if (drawable != null) {
                drawable.mutate().setTint(ContextCompat.getColor(context, color));
            }
        }
    }

    /*---------Image---------*/

    public static Bitmap decodeImage(String imageString) {
        byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    private static String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static String convertImage(Context context, Uri uri) {
        String encodedImage = null;
        try {
            final InputStream imageStream = context.getContentResolver().openInputStream(uri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            encodedImage = encodeImage(selectedImage);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return encodedImage;
    }

    public static void requestPermission(Activity activity) {
        if (!checkPermissionForReadExtertalStorage(activity)) {
            try {
                requestPermissionForReadExtertalStorage(activity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void requestPermissionForReadExtertalStorage(Activity activity) throws Exception {
        try {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static boolean checkPermissionForReadExtertalStorage(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }


    /*----------Date---------*/

    public static String dateToString(Date date) {
        if (date != null) {
            return dateFormat.format(date.getTime());
        }
        return null;
    }

    public static Date stringToDate(String date) throws ParseException {
        if (date != null) {
            return dateFormat.parse(date);
        }
        return null;
    }

}
