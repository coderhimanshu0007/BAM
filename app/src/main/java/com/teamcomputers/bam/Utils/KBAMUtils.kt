package com.teamcomputers.bam.Utils

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.app.NotificationCompat
import com.google.gson.Gson
import com.teamcomputers.bam.Interface.BAMConstant
import com.teamcomputers.bam.KBAMApplication
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectOutputStream
import java.lang.reflect.Type
import java.math.RoundingMode
import java.security.SecureRandom
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class KBAMUtils : KBAMApplication() {
    companion object {
        val TAG = KBAMUtils::class.java.name
        val FILE_NAME = "draw_state.ser"
        @JvmStatic
        fun fromJson(responseString: String, listType: Type): Any? {
            try {
                val gson = Gson()
                return gson.fromJson<Any>(responseString, listType)
            } catch (e: Exception) {
                Log.e(TAG, "Error In Converting JsonToModel", e)
            }

            return null
        }

        fun toJson(`object`: Any): String? {
            try {
                val gson = Gson()
                return gson.toJson(`object`)
            } catch (e: Exception) {
                Log.e(TAG, "Error In Converting ModelToJson", e)
            }

            return null
        }

        // Generic function to convert an Array to List
        @JvmStatic
        fun <T> convertArrayToList(array: Array<T>): List<T> {

            // Create the List by passing the Array
            // as parameter in the constructor

            // Return the converted List
            return Arrays.asList(*array)
        }

        @JvmStatic
        fun hideSoftKeyboard(activity: Activity) {
            val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            if (activity.currentFocus != null) {
                inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
            }
        }

        fun showSoftKeyboard(activity: Activity) {
            val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            if (activity.currentFocus != null) {
                inputMethodManager.showSoftInput(activity.currentFocus, InputMethodManager.SHOW_FORCED)
            }
        }

        fun generateRandomInteger(min: Int, max: Int): Int {
            val rand = SecureRandom()
            rand.setSeed(Date().time)
            return rand.nextInt(max - min + 1) + min
        }

        /*public static boolean isValidEmail(CharSequence email) {
        return email != null && EMAIL_VALIDATION_PATTERN.matcher(email).matches();
    }*/

        fun isConnected(context: Context): Boolean {
            val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connMgr.activeNetworkInfo
            return networkInfo != null && networkInfo!!.isConnected
        }

        fun isWifiConnected(context: Context): Boolean {
            return isConnected(context, ConnectivityManager.TYPE_WIFI)
        }

        fun isMobileConnected(context: Context): Boolean {
            return isConnected(context, ConnectivityManager.TYPE_MOBILE)
        }

        private fun isConnected(context: Context, type: Int): Boolean {
            val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                val networkInfo = connMgr.getNetworkInfo(type)
                return networkInfo != null && networkInfo.isConnected
            } else {
                return isConnected(connMgr, type)
            }
        }

        fun getStringInNoFormat(amount: Double?): String {
            val value = Math.round(amount!!).toInt()
            return value.toString()
        }

        @JvmStatic
        fun getRoundOffValue(amount: Double): String {
            var amount = amount
            amount = amount / 100000
            val df = DecimalFormat("0.00")
            df.roundingMode = RoundingMode.UP
            return df.format(amount)
        }

        fun replaceDataResponse(response: String): String {
            return response.replace("{", "{\"").replace("}", "\"}").replace("=", "\"=\"").replace(", ", "\", \"").replace("}\", \"{", "}, {").replace("=", ": ").replace("\"[", "[").replace("\\", " ").replace("[]\"", "[]").replace("}]\"}", "}]}")
        }

        @JvmStatic
        fun replaceWSDataResponse(response: String): String {
            return response.replace("{", "{\"").replace("}", "\"}").replace("=", "\"=\"").replace(", ", "\", \"").replace("}\", \"{", "}, {").replace("=", ": ").replace("\"[", "[").replace("\\", " ").replace("[]\"", "[]").replace("}]\"}", "}]}").replace("]\", \"Filter\": \"", "], \"Filter\": ").replace("\"}\"", "\"}")
        }

        @JvmStatic
        fun replaceTOSInvoiceDataResponse(response: String): String {
            return response.replace("{", "{\"").replace("}", "\"}").replace("=", "\"=\"").replace(", ", "\", \"").replace("}\", \"{", "}, {").replace("=", ": ").replace("\"[", "[").replace("\\", " ").replace("[]\"", "[]").replace("}]\"}", "}]}").replace("]\", \"Filter\": \"", "], \"Filter\": ").replace("\"}\"", "\"}").replace("\"{\"", "{\"")
        }

        @JvmStatic
        fun replaceCollectionDataResponse(response: String): String {
            //return response.replace("{", "{\"").replace("}", "\"}").replace("=", "\"=\"").replace(", ", "\", \"").replace("}\", \"{", "}, {").replace("=", ": ").replace("\"[", "[").replace("\\", " ").replace("[]\"", "[]").replace("}]\"}", "}]}")
            return response.replace("{", "{\"").replace("}", "\"}").replace("=", "\"=\"").replace(", ", "\", \"").replace("}\", \"{", "}, {").replace("=", ": ").replace("\"[", "[").replace("\\", " ").replace("[]\"", "[]").replace("}]\"}", "}]}").replace("]\", \"Table\"", "], \"Table\"").replace(" \", \"", "\", \"")
        }

        @JvmStatic
        fun replaceCollectionOutstandingData(response: String): String {
            return response.replace("{", "{\"").replace("}", "\"}").replace("=", "\"=\"").replace(", ", "\", \"").replace("}\", \"{", "}, {").replace("=", ": ").replace("\"{\"", "{\"").replace("\"}\"", "}").replace("\"[{\"", "[{\"").replace("]\"", "]")
        }

        @JvmStatic
        fun replaceTotalOutstandingDataResponse(response: String): String {
            //return response.replace("{", "{\"").replace("}", "\"}").replace("=", "\"=\"").replace(", ", "\", \"").replace("}\", \"{", "}, {").replace("=", ": ").replace("\"[", "[").replace("\\", " ").replace("[]\"", "[]").replace("}]\"}", "}]}")
            return response.replace("{", "{\"").replace("}", "\"}").replace("=", "\"=\"").replace(", ", "\", \"").replace("}\", \"{", "}, {").replace("=", ": ").replace("\"[", "[").replace("\\", " ").replace("[]\"", "[]").replace("]\"}", "]}").replace("]\", \"Table\"", "], \"Table\"").replace(" \", \"", "\", \"").replace("signed\", \"waiting", "signed, waiting")
        }

        @JvmStatic
        fun replaceCollectionWIPDataResponse(response: String): String {
            return response.replace("{", "{\"").replace("}", "\"}").replace("=", "\"=\"").replace(", ", "\", \"").replace("}\", \"{", "}, {").replace("=", ": ").replace("\"[", "[").replace("\\", " ").replace("[]\"", "[]").replace("]\"", "]").replace("]\", \"Table\"", "], \"Table\"").replace(" \", \"", "\", \"").replace("\"{\"", "{\"").replace("}\",", "},")
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private fun isConnected(connMgr: ConnectivityManager, type: Int): Boolean {
            val networks = connMgr.allNetworks
            var networkInfo: NetworkInfo?
            for (mNetwork in networks) {
                networkInfo = connMgr.getNetworkInfo(mNetwork)
                if (networkInfo != null && networkInfo.type == type && networkInfo.isConnected) {
                    return true
                }
            }
            return false
        }

        @JvmStatic
        fun getFormattedDate(dateFormat: String, date: Date): String {
            var formattedDate = ""
            var simpleDateFormat: SimpleDateFormat
            when (dateFormat) {
                BAMConstant.DateFormat.SESSION_DATE_FORMAT -> {
                    simpleDateFormat = SimpleDateFormat(BAMConstant.DateFormat.SESSION_DATE_FORMAT, Locale.getDefault())
                    formattedDate = simpleDateFormat?.format(date)
                }
            }
            return formattedDate
        }
    }

    /*public static String getFormattedDate(String dateFormat, Date date) {
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
    }*/

    fun getTimeStamp(dateInString: String, dateFormat: String): Long {
        var timeStamp: Long = 0
        val sdf = SimpleDateFormat(dateFormat, Locale.US)
        try {
            timeStamp = sdf.parse(dateInString).time
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return timeStamp
    }

    fun isLocationEnabled(): Boolean {
        var locationMode = 0
        val locationProviders: String

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(KBAMApplication()?.getInstance()?.contentResolver, Settings.Secure.LOCATION_MODE)

            } catch (e: Settings.SettingNotFoundException) {
                e.printStackTrace()
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF

        } else {
            locationProviders = Settings.Secure.getString(KBAMApplication()?.getInstance()?.contentResolver, Settings.Secure.LOCATION_PROVIDERS_ALLOWED)
            return !TextUtils.isEmpty(locationProviders)
        }

    }

    fun fromHtml(html: String): Spanned {
        val result: Spanned
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            result = Html.fromHtml(html)
        }
        return result
    }

    fun saturdaySundayCount(dateFrom: Date, dateTo: Date): Int {
        val calendarFrom = Calendar.getInstance()
        calendarFrom.time = dateFrom
        val calendarTo = Calendar.getInstance()
        calendarTo.time = dateTo
        var sundays = 0
        var saturdays = 0

        while (!calendarFrom.after(calendarTo)) {
            if (calendarFrom.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                sundays++
            } else if (calendarFrom.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                saturdays++
            }
            calendarFrom.add(Calendar.DATE, 1)
        }
        return saturdays + sundays
    }

    fun getDifferenceDays(dateFrom: Date, dateTo: Date): Long {
        val diff = dateTo.time - dateFrom.time
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
        //        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) - saturdaySundayCount(dateFrom, dateTo);
    }


    fun getImageBitMap(base64String: String): Bitmap {
        val base64Bytes = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(base64Bytes, 0, base64Bytes.size)
    }

// Check if a timestamp occurs today
/*public static boolean isToday(long check) {
    Date date = new Date();
    date.setTime(check);
    String today = getFormattedDate(DateFormat.DDMMYYYY, new Date());
    String compare = getFormattedDate(DateFormat.DDMMYYYY, date);
    return today.equals(compare);
}*/

    fun showAlertDialog(context: Context, title: String, message: String) {
        val alert = AlertDialog.Builder(context)
        alert.setTitle(title)
        alert.setMessage(message)
        alert.setPositiveButton("OK", null)
        alert.show()
    }

    //get the current version name of app
    fun getVersionInfo(): Float {
        var versionName = ""
        //        int versionCode = -1;
        /*try {
            val packageInfo = KBAMApplication()?.getInstance()?.packageManager?.getPackageInfo(KBAMApplication()?.getInstance()?.packageName, 0)
            versionName = packageInfo?.versionName ?:
            //            versionCode = packageInfo.versionCode;\
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }*/

        return java.lang.Float.parseFloat(versionName)
    }

    fun getVersionCode(): Float {
        val manager = this.packageManager
        val info = manager.getPackageInfo(this.packageName, PackageManager.GET_ACTIVITIES)
        /*toast("PackageName = " + info.packageName + "\nVersionCode = "
                + info.versionCode + "\nVersionName = "
                + info.versionName + "\nPermissions = " + info.permissions)*/
        return java.lang.Float.parseFloat(info.packageName)
    }

    fun showDatePickerDialog(context: Context, timestamp: Long) {
        val myCalendar = Calendar.getInstance()
        myCalendar.timeInMillis = timestamp
        DatePickerDialog(context,
                null, myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    fun resize(drawable: Drawable): Drawable {
        val bitmap = (drawable as BitmapDrawable).bitmap
        val bitmapResized = Bitmap.createScaledBitmap(bitmap, 25, 35, false)
        return BitmapDrawable(KBAMApplication()?.getInstance()?.resources, bitmapResized)
    }

    fun createNotificationBuilder(iconRes: Int, title: String, body: String, soundUri: Uri): NotificationCompat.Builder {
        return NotificationCompat.Builder(KBAMApplication()?.getInstance())
                .setSmallIcon(iconRes)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(soundUri)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setLights(Color.BLUE, 0, 1000)
                .setVibrate(longArrayOf(0, 1000, 1000, 1000, 1000))
    }

    fun getRandomColor(): Int {
        var color = 0
        when (generateRandomInteger(1, 11)) {
            1 -> color = Color.BLACK
            2 -> color = Color.DKGRAY
            3 -> color = Color.GRAY
            4 -> color = Color.LTGRAY
            5 -> color = Color.BLACK
            6 -> color = Color.RED
            7 -> color = Color.GREEN
            8 -> color = Color.BLUE
            9 -> color = Color.CYAN
            10 -> color = Color.MAGENTA
            11 -> color = Color.BLACK
            else -> color = Color.BLACK
        }
        return color
    }

    fun isEmpty(checkThisOut: String?): Boolean {
        return checkThisOut == null || checkThisOut.trim { it <= ' ' } == ""
    }

    fun getDeviceId(context: Context): String {
        @SuppressLint("MissingPermission") val deviceId = (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).deviceId
        return deviceId ?: ""
    }

    fun dpToPx(dp: Int): Int {
        val density = KBAMApplication()?.getInstance()?.resources?.displayMetrics?.density
        return Math.round(dp.toFloat() * density!!)
    }

    fun getScreenShot(view: View, isRoot: Boolean): Bitmap {
        val screenView: View
        if (isRoot) {
            screenView = view.rootView
        } else {
            screenView = view
        }
        screenView.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(screenView.drawingCache)
        screenView.isDrawingCacheEnabled = false
        return bitmap
    }

    fun store(bm: Bitmap, fileName: String) {
        val dirPath = Environment.getExternalStorageDirectory().absolutePath + "/TeamAppScreenshots"
        val dir = File(dirPath)
        if (!dir.exists())
            dir.mkdirs()
        val file = File(dirPath, fileName)
        try {
            val fOut = FileOutputStream(file)
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut)
            fOut.flush()
            fOut.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun deleteSavedStateFile(context: Context?) {

        if (context != null) {

            var fos: FileOutputStream? = null
            try {
                fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
                val os = ObjectOutputStream(fos)
                os.close()
                fos!!.close()
            } catch (e: Exception) {
                e.printStackTrace()

                if (fos != null) {

                    try {
                        fos.close()
                    } catch (e1: Exception) {
                        e1.printStackTrace()
                    }

                }
            }

        }
    }

    // Shortcut method to run on uiThread a runnable
    private fun runOnUiThread(runnable: Runnable) {

        Handler(Looper.getMainLooper()).post(runnable)
    }

    // Listener for file creation
    interface StateSaveInterface {
        fun onStateSaved()

        fun onStateSaveError()
    }

}