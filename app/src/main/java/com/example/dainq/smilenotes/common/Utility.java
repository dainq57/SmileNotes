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
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import com.example.dainq.smilenotes.model.CustomerObject;
import com.example.dainq.smilenotes.model.MeetingObject;
import com.example.dainq.smilenotes.model.ProductObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.realm.RealmResults;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

public class Utility {
    public static final String fileName = "smilenote.txt";
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.FORMAT_DATE, Locale.US);

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

    //decode from String to image
    public static Bitmap decodeImage(String imageString) {
        byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    //encode bitmap to string
    private static String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 10, baos);
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

    public static Date resetCalendar(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }


    /*JSON backup data*/

    public static JSONObject makJsonObject(JSONArray jsonArray1, JSONArray jsonArray2, JSONArray jsonArray3) throws JSONException {

        JSONObject finalobject = new JSONObject();
        finalobject.put("customer", jsonArray1);
        finalobject.put("meeting", jsonArray2);
        finalobject.put("product", jsonArray3);
        return finalobject;
    }

    public static JSONArray makeJsonArrayCustomer(RealmResults<CustomerObject> realmResults) {
        JSONObject jsonObject = null;
        CustomerObject customer = null;
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < realmResults.size(); i++) {
            jsonObject = new JSONObject();
            customer = realmResults.get(i);
            try {
                jsonObject.put("id", customer.getId());
                jsonObject.put("level", customer.getLevel());
                jsonObject.put("ada", customer.getAda());
                jsonObject.put("name", customer.getName());
                jsonObject.put("dateofbirth", customer.getDateofbirth());
                jsonObject.put("phonenumber", customer.getPhonenumber());
                jsonObject.put("address", customer.getAddress());
                jsonObject.put("reason", customer.getReason());
                jsonObject.put("gender", customer.getGender());
                jsonObject.put("job", customer.getJob());
                jsonObject.put("problem", customer.getProblem());
                jsonObject.put("solution", customer.getSolution());
                jsonObject.put("note", customer.getNote());
                jsonObject.put("product", customer.getProduct());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    public static JSONArray makeJsonArrayMeeting(RealmResults<MeetingObject> realmResults) {
        JSONObject jsonObject = null;
        MeetingObject meeting = null;
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < realmResults.size(); i++) {
            jsonObject = new JSONObject();
            meeting = realmResults.get(i);
            try {
                jsonObject.put("idcustomer", meeting.getIdcustomer());
                jsonObject.put("id", meeting.getId());
                jsonObject.put("meeting", meeting.getMeeting());
                jsonObject.put("schedule", meeting.getSchedule());
                jsonObject.put("content", meeting.getContent());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    public static JSONArray makeJsonArrayProduct(RealmResults<ProductObject> realmResults) {
        JSONObject jsonObject = null;
        ProductObject product = null;
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < realmResults.size(); i++) {
            jsonObject = new JSONObject();
            product = realmResults.get(i);
            try {
                jsonObject.put("idcustomer", product.getIdcustomer());
                jsonObject.put("id", product.getId());
                jsonObject.put("name", product.getName());
                jsonObject.put("usedate", product.getUsedate());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    public static void writeFile(String string) {
        // Thư mục gốc của SD Card.
        File extStore = Environment.getExternalStorageDirectory();
        // ==> /storage/emulated/0/note.txt
        String path = extStore.getAbsolutePath() + "/" + fileName;
        Log.i("ExternalStorageDemo", "Save to: " + path);

        try {
            File myFile = new File(path);
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(string);
            myOutWriter.close();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //COLOR
    //GREEN - VIOLET - BLUE - ORANGE
    public static final int[] MATERIAL_COLORS = {
            rgb("#49b800"), rgb("#b541b5"), rgb("#00a1c9"), rgb("#ed6c02")
    };

    /**
     * check valid password
     * param String password
     **/
    public static boolean isPasswordValid(String password) {
        //password need more than 5
        return password.length() > 5;
    }

    /**
     * check valid email
     * param String email
     **/
    public static boolean isEmailValid(String email) {
        //here check format of email need @ and .
        return (email.contains("@") && (email.contains(".")));
    }
}
