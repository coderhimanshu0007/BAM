package com.teamcomputers.bam.Utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.teamcomputers.bam.BAMApplication;
import com.teamcomputers.bam.Interface.BAMConstant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

//Common task done in app must be done here so they can be used anywhere in this app without writing code again and again(Minimize Redundancy).
public class BAMUtil implements BAMConstant {
    private static final String TAG = BAMUtil.class.getName();
    private static final String FILE_NAME = "draw_state.ser";

    public static Object fromJson(String responseString, Type listType) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(responseString, listType);
        } catch (Exception e) {
            Log.e(TAG, "Error In Converting JsonToModel", e);
        }
        return null;
    }

    public static String toJson(Object object) {
        try {
            Gson gson = new Gson();
            return gson.toJson(object);
        } catch (Exception e) {
            Log.e(TAG, "Error In Converting ModelToJson", e);
        }
        return null;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void showSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.showSoftInput(activity.getCurrentFocus(), InputMethodManager.SHOW_FORCED);
        }
    }

    public static int generateRandomInteger(int min, int max) {
        SecureRandom rand = new SecureRandom();
        rand.setSeed(new Date().getTime());
        return rand.nextInt((max - min) + 1) + min;
    }

    public static boolean isValidEmail(CharSequence email) {
        return email != null && EMAIL_VALIDATION_PATTERN.matcher(email).matches();
    }

    public static boolean isConnected(@NonNull Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public static boolean isWifiConnected(@NonNull Context context) {
        return isConnected(context, ConnectivityManager.TYPE_WIFI);
    }

    public static boolean isMobileConnected(@NonNull Context context) {
        return isConnected(context, ConnectivityManager.TYPE_MOBILE);
    }

    private static boolean isConnected(@NonNull Context context, int type) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            NetworkInfo networkInfo = connMgr.getNetworkInfo(type);
            return networkInfo != null && networkInfo.isConnected();
        } else {
            return isConnected(connMgr, type);
        }
    }

    public static String getStringInNoFormat(Double amount) {
        int value = (int)Math.round(amount);
        return String.valueOf(value);
    }

    public static String getRoundOffValue(double amount) {
        amount = amount / 100000;
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.UP);
        return df.format(amount);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static boolean isConnected(@NonNull ConnectivityManager connMgr, int type) {
        Network[] networks = connMgr.getAllNetworks();
        NetworkInfo networkInfo;
        for (Network mNetwork : networks) {
            networkInfo = connMgr.getNetworkInfo(mNetwork);
            if (networkInfo != null && networkInfo.getType() == type && networkInfo.isConnected()) {
                return true;
            }
        }
        return false;
    }

    public static String getFormattedDate(String dateFormat, Date date) {
        String formattedDate = "";
        SimpleDateFormat simpleDateFormat;
        switch (dateFormat) {
            case DateFormat.DEFAULT:
                simpleDateFormat = new SimpleDateFormat(DateFormat.DEFAULT, Locale.getDefault());
                formattedDate = simpleDateFormat.format(date);
                break;
            case DateFormat.DEFAULT_YY:
                simpleDateFormat = new SimpleDateFormat(DateFormat.DEFAULT_YY, Locale.getDefault());
                formattedDate = simpleDateFormat.format(date);
                break;
            case DateFormat.YYYYMMDD:
                simpleDateFormat = new SimpleDateFormat(DateFormat.YYYYMMDD, Locale.getDefault());
                formattedDate = simpleDateFormat.format(date);
                break;
            case DateFormat.EEE_D_MMM_YYYY:
                simpleDateFormat = new SimpleDateFormat(DateFormat.EEE_D_MMM_YYYY, Locale.getDefault());
                formattedDate = simpleDateFormat.format(date);
                break;
            case DateFormat.HH_MM_SS_A:
                simpleDateFormat = new SimpleDateFormat(DateFormat.HH_MM_SS_A, Locale.getDefault());
                formattedDate = simpleDateFormat.format(date);
                break;
            case DateFormat.MMMM_DD_EEEE:
                simpleDateFormat = new SimpleDateFormat(DateFormat.MMMM_DD_EEEE, Locale.getDefault());
                formattedDate = simpleDateFormat.format(date);
                break;
            case DateFormat.MMMM:
                simpleDateFormat = new SimpleDateFormat(DateFormat.MMMM, Locale.getDefault());
                formattedDate = simpleDateFormat.format(date);
                break;
            case DateFormat.DDMMM:
                simpleDateFormat = new SimpleDateFormat(DateFormat.DDMMM, Locale.getDefault());
                formattedDate = simpleDateFormat.format(date);
                break;
            case DateFormat.DDMMMYYYY:
                simpleDateFormat = new SimpleDateFormat(DateFormat.DDMMMYYYY, Locale.getDefault());
                formattedDate = simpleDateFormat.format(date);
                break;
            case DateFormat.DDMM:
                simpleDateFormat = new SimpleDateFormat(DateFormat.DDMM, Locale.getDefault());
                formattedDate = simpleDateFormat.format(date);
                break;
            case DateFormat.KK_MM:
                simpleDateFormat = new SimpleDateFormat(DateFormat.KK_MM, Locale.getDefault());
                formattedDate = simpleDateFormat.format(date);
                break;
            case DateFormat.HH_MM:
                simpleDateFormat = new SimpleDateFormat(DateFormat.HH_MM, Locale.getDefault());
                formattedDate = simpleDateFormat.format(date);
                break;
            case DateFormat.ATTENDANCE_DATE_FORMAT:
                simpleDateFormat = new SimpleDateFormat(DateFormat.ATTENDANCE_DATE_FORMAT, Locale.getDefault());
                formattedDate = simpleDateFormat.format(date);
                break;
            default:
                simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());
                formattedDate = simpleDateFormat.format(date);
                break;
        }
        return formattedDate;
    }

    public static long getTimeStamp(String dateInString, String dateFormat) {
        long timeStamp = 0;
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        try {
            timeStamp = sdf.parse(dateInString).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp;
    }

    public static boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(BAMApplication.getInstance().getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(BAMApplication.getInstance().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }

    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    public static int saturdaySundayCount(Date dateFrom, Date dateTo) {
        Calendar calendarFrom = Calendar.getInstance();
        calendarFrom.setTime(dateFrom);
        Calendar calendarTo = Calendar.getInstance();
        calendarTo.setTime(dateTo);
        int sundays = 0;
        int saturdays = 0;

        while (!calendarFrom.after(calendarTo)) {
            if (calendarFrom.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                sundays++;
            } else if (calendarFrom.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                saturdays++;
            }
            calendarFrom.add(Calendar.DATE, 1);
        }
        return saturdays + sundays;
    }

    public static long getDifferenceDays(Date dateFrom, Date dateTo) {
        long diff = dateTo.getTime() - dateFrom.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
//        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) - saturdaySundayCount(dateFrom, dateTo);
    }


    public static Bitmap getImageBitMap(String base64String) {
        byte[] base64Bytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(base64Bytes, 0, base64Bytes.length);
    }

    // Check if a timestamp occurs today
    public static boolean isToday(long check) {
        Date date = new Date();
        date.setTime(check);
        String today = getFormattedDate(DateFormat.DDMMYYYY, new Date());
        String compare = getFormattedDate(DateFormat.DDMMYYYY, date);
        return today.equals(compare);
    }

    public static void showAlertDialog(Context context, String title, String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setPositiveButton("OK", null);
        alert.show();
    }

    //get the current version name of app
    public static float getVersionInfo() {
        String versionName = "";
//        int versionCode = -1;
        try {
            PackageInfo packageInfo = BAMApplication.getInstance().getPackageManager().getPackageInfo(BAMApplication.getInstance().getPackageName(), 0);
            versionName = packageInfo.versionName;
//            versionCode = packageInfo.versionCode;\
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return Float.parseFloat(versionName);
    }

    public static void showDatePickerDialog(Context context, long timestamp) {
        Calendar myCalendar = Calendar.getInstance();
        myCalendar.setTimeInMillis(timestamp);
        new DatePickerDialog(context,
                null, myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public static Drawable resize(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmap, 25, 35, false);
        return new BitmapDrawable(BAMApplication.getInstance().getResources(), bitmapResized);
    }

    public static NotificationCompat.Builder createNotificationBuilder(int iconRes, String title, String body, Uri soundUri) {
        return new NotificationCompat.Builder(BAMApplication.getInstance())
                .setSmallIcon(iconRes)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(soundUri)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setLights(Color.BLUE, 0, 1000)
                .setVibrate(new long[]{0, 1000, 1000, 1000, 1000})
                ;
    }

    public static int getRandomColor() {
        int color = 0;
        switch (generateRandomInteger(1, 11)) {
            case 1:
                color = Color.BLACK;
                break;
            case 2:
                color = Color.DKGRAY;
                break;
            case 3:
                color = Color.GRAY;
                break;
            case 4:
                color = Color.LTGRAY;
                break;
            case 5:
                color = Color.BLACK;
                break;
            case 6:
                color = Color.RED;
                break;
            case 7:
                color = Color.GREEN;
                break;
            case 8:
                color = Color.BLUE;
                break;
            case 9:
                color = Color.CYAN;
                break;
            case 10:
                color = Color.MAGENTA;
                break;
            case 11:
                color = Color.BLACK;
                break;
            default:
                color = Color.BLACK;
                break;
        }
        return color;
    }

    public static boolean isEmpty(String checkThisOut) {
        return checkThisOut == null || checkThisOut.trim().equals("");
    }

    public static String getDeviceId(Context context) {
        @SuppressLint("MissingPermission") final String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        if (deviceId != null) {
            return deviceId;
        } else {
            return "";
        }
    }

    public static int dpToPx(int dp) {
        float density = BAMApplication.getInstance().getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public static Bitmap getScreenShot(View view, boolean isRoot) {
        View screenView;
        if (isRoot) {
            screenView = view.getRootView();
        } else {
            screenView = view;
        }
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public static void store(Bitmap bm, String fileName) {
        final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/TeamAppScreenshots";
        File dir = new File(dirPath);
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dirPath, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteSavedStateFile(Context context) {

        if (context != null) {

            FileOutputStream fos = null;
            try {
                fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
                ObjectOutputStream os = new ObjectOutputStream(fos);
                os.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();

                if (fos != null) {

                    try {
                        fos.close();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

    // Shortcut method to run on uiThread a runnable
    private static void runOnUiThread(Runnable runnable) {

        new Handler(Looper.getMainLooper()).post(runnable);
    }


    // Listener for file creation
    public interface StateSaveInterface {
        void onStateSaved();

        void onStateSaveError();
    }


}
