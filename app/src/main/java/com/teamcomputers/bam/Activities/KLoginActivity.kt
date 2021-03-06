package com.teamcomputers.bam.Activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat
import com.google.firebase.iid.FirebaseInstanceId
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.teamcomputers.bam.BAMApplication
import com.teamcomputers.bam.Interface.KBAMConstant
import com.teamcomputers.bam.Models.AppVersionResponse
import com.teamcomputers.bam.Models.LoginModel
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.R
import com.teamcomputers.bam.Requesters.KActiveEmployeeAccessRequester
import com.teamcomputers.bam.Requesters.KAppVersionRequester
import com.teamcomputers.bam.Requesters.KLoginRequester
import com.teamcomputers.bam.Requesters.LoginRequester
import com.teamcomputers.bam.Utils.BackgroundExecutor
import com.teamcomputers.bam.Utils.KBAMUtils
import com.teamcomputers.bam.controllers.SharedPreferencesController
import kotlinx.android.synthetic.main.activity_login.*
import org.greenrobot.eventbus.Subscribe
import java.util.*


class KLoginActivity : KBaseActivity() {
    internal var save = false
    internal var show = false
    internal var deviceId: String = ""
    var telephonyManager: TelephonyManager? = null
    private var userProfile: LoginModel? = null
    override fun getLayout(): Int {
        return R.layout.activity_login
    }

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        runOnUiThread {
            when (eventObject.id) {
                KBAMConstant.Events.LOGIN_SUCCESSFUL -> {
                    userProfile = SharedPreferencesController.getInstance(BAMApplication.getInstance()).userProfile
                    //showProgress(KBAMConstant.ProgressDialogTexts.LOADING)
                    BackgroundExecutor.getInstance().execute(KActiveEmployeeAccessRequester(userProfile?.getUserID()!!, userProfile?.getMemberName()!!))
                }
                KBAMConstant.Events.GET_ACTIVE_EMPLOYEE_ACCESS_SUCCESSFUL -> {
                    dismissProgress()
                    showToast("RECORD_FOUND")
                    dismissProgress()
                    ActivityCompat.finishAffinity(this@KLoginActivity)
                    val dashBoard = Intent()
                    dashBoard.setClass(this@KLoginActivity, DashboardActivity::class.java)
                    dashBoard.putExtra(KBAMConstant.Constants.FINISH, true)
                    dashBoard.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    dashBoard.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    dashBoard.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
                    startActivity(dashBoard)
                }
                KBAMConstant.Events.GET_ACTIVE_EMPLOYEE_ACCESS_UNSUCCESSFUL -> {
                    dismissProgress()
                    showToast(KBAMConstant.ToastTexts.NO_RECORD_FOUND)
                }
                KBAMConstant.Events.WRONG_LOGIN_CREDENTIALS_USED -> {
                    dismissProgress()
                    showToast(KBAMConstant.ToastTexts.WRONG_LOGIN_CREDENTIALS_USED)
                }
                KBAMConstant.Events.YOU_ARE_USING_OLDER_VERSION_OF_APP -> {
                    dismissProgress()
                    getVersionInfo(eventObject.getObject() as AppVersionResponse)
                }
                KBAMConstant.Events.YOU_ARE_USING_CURRENT_VERSION_OF_APP ->
                    //showProgress(ProgressDialogTexts.AUTHENTICATING);
                    BackgroundExecutor.getInstance().execute(LoginRequester(txtUserName?.getText().toString(), txtPassword?.getText().toString()))
                KBAMConstant.Events.NO_INTERNET_CONNECTION -> {
                    dismissProgress()
                    showToast(KBAMConstant.ToastTexts.NO_INTERNET_CONNECTION)
                }
                KBAMConstant.Events.LOGIN_UN_SUCCESSFUL -> {
                    dismissProgress()
                    showToast(KBAMConstant.ToastTexts.LOGIN_UNSUCCESSFULL)
                }
                KBAMConstant.Events.OOPS_MESSAGE -> {
                    dismissProgress()
                    showToast(KBAMConstant.ToastTexts.OOPS_MESSAGE)
                }
                KBAMConstant.Events.INTERNAL_ERROR -> showDialog(this)
            }//appUrl = ((AppVersionResponse) eventObject.getObject()).getAppVersionUrl();
            //showVersionCheck();
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        llLogin.setOnClickListener() {
            submit()
        }
    }

    private fun init() {
        save = false
        show = false
        for (i in 6 downTo 0 step 2) {
            println(i)
        }
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied) {
                            // permission is denied permenantly, navigate user to app settings
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<com.karumi.dexter.listener.PermissionRequest>, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                })
                .onSameThread()
                .check()

        try {
            val pInfo = this@KLoginActivity.getPackageManager().getPackageInfo(packageName, 0)
            val version = pInfo.versionName
            //showToast(version);
            tviCurrentVersion.setText("Current version $version")
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

    }

    @SuppressLint("MissingPermission")
    fun submit() {
        KBAMUtils?.hideSoftKeyboard(this)
        SharedPreferencesController.getInstance(
                BAMApplication.getInstance()).setFireBaseToken(
                FirebaseInstanceId.getInstance().token)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            /*
        * getDeviceId() returns the unique device ID.
        * For example,the IMEI for GSM and the MEID or ESN for CDMA phones.
        */
            deviceId = telephonyManager?.getDeviceId().toString()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            deviceId = UUID.randomUUID().toString()
        }
        /*
        * getSubscriberId() returns the unique subscriber ID,
        * For example, the IMSI for a GSM phone.
        */
        //val subscriberId = telephonyManager?.getSubscriberId()
        if (!validate()) {
            return
        }
        showProgress(KBAMConstant.ProgressDialogTexts.AUTHENTICATING)
        BackgroundExecutor.getInstance().execute(KAppVersionRequester())
        //BackgroundExecutor.getInstance().execute(new AppVersionRequester());
        //showProgress(ProgressDialogTexts.AUTHENTICATING);
        //BackgroundExecutor.getInstance().execute(new KLoginRequester(txtUserName.getText().toString(), txtPassword.getText().toString()));
    }

    fun validate(): Boolean {
        var valid = true

        val userName = txtUserName.getText().toString()
        val password = txtPassword.getText().toString()

        if (userName.isEmpty()) {
            txtUserName.setError("Enter a valid TMC")
            valid = false
        } else {
            txtUserName.setError(null)
        }

        if (password.isEmpty()) {
            txtPassword.setError("Enter your password")
            valid = false
        } else {
            txtPassword.setError(null)
        }

        return valid
    }

    private fun getVersionInfo(`object`: AppVersionResponse) {
        var versionName = ""
        var versionCode = -1
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            versionName = packageInfo.versionName
            versionCode = packageInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        if (java.lang.Float.parseFloat(`object`.appVersionID) > java.lang.Float.parseFloat(versionName)) {
            appUrl = `object`.appVersionUrl
            showVersionCheck()
        } else {
            BackgroundExecutor.getInstance().execute(KLoginRequester(txtUserName?.getText().toString(), txtPassword?.getText().toString(), SharedPreferencesController.getInstance(
                    BAMApplication.getInstance()).getFireBaseToken()))
        }

        //TextView textViewVersionInfo = (TextView) findViewById(R.id.textview_version_info);
        //textViewVersionInfo.setText(String.format("Version name = %s \nVersion code = %d", versionName, versionCode));
    }
}
