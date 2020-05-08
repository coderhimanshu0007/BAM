package com.teamcomputers.bam.Activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import butterknife.BindView
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.teamcomputers.bam.Interface.KBAMConstant
import com.teamcomputers.bam.Models.AppVersionResponse
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.R
import com.teamcomputers.bam.Requesters.KAppVersionRequester
import com.teamcomputers.bam.Requesters.KLoginRequester
import com.teamcomputers.bam.Requesters.LoginRequester
import com.teamcomputers.bam.Utils.BackgroundExecutor
import com.teamcomputers.bam.Utils.KBAMUtils
import kotlinx.android.synthetic.main.activity_login.*
import org.greenrobot.eventbus.Subscribe

class KLoginActivity : KBaseActivity() {
    internal var save = false
    internal var show = false
    override fun getLayout(): Int {
        return R.layout.activity_login
    }

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        runOnUiThread {
            when (eventObject.id) {
                KBAMConstant.Events.LOGIN_SUCCESSFUL -> {
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
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
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

    fun submit() {
        KBAMUtils?.hideSoftKeyboard(this)
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
            showVersionCheck()
        } else {
            BackgroundExecutor.getInstance().execute(KLoginRequester(txtUserName?.getText().toString(), txtPassword?.getText().toString()))
        }

        //TextView textViewVersionInfo = (TextView) findViewById(R.id.textview_version_info);
        //textViewVersionInfo.setText(String.format("Version name = %s \nVersion code = %d", versionName, versionCode));
    }
}
