package com.teamcomputers.bam.Activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import androidx.core.app.ActivityCompat
import com.teamcomputers.bam.Interface.KBAMConstant
import com.teamcomputers.bam.KBAMApplication
import com.teamcomputers.bam.Models.AppVersionResponse
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.R
import com.teamcomputers.bam.Requesters.KAppVersionRequester
import com.teamcomputers.bam.Utils.BackgroundExecutor
import com.teamcomputers.bam.controllers.SharedPreferencesController
import org.greenrobot.eventbus.Subscribe

class KSplashActivity : KBaseActivity() {
    var splashHandler: Handler? = null

    private val splashRunnable = Runnable { launchNextActivity() }

    override fun getLayout(): Int {
        return R.layout.activity_splash
    }

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        runOnUiThread {
            dismissProgress()
            when (eventObject.id) {
                KBAMConstant.Events.YOU_ARE_USING_OLDER_VERSION_OF_APP -> getVersionInfo(eventObject.getObject() as AppVersionResponse)
                KBAMConstant.Events.YOU_ARE_USING_CURRENT_VERSION_OF_APP -> nextActivity()
                KBAMConstant.Events.OOPS_MESSAGE -> showToast(KBAMConstant.ToastTexts.OOPS_MESSAGE)
                KBAMConstant.Events.NO_INTERNET_CONNECTION -> showToast(KBAMConstant.ToastTexts.NO_INTERNET_CONNECTION)
                KBAMConstant.Events.INTERNAL_ERROR -> showDialog(this)
            }//appUrl = ((AppVersionResponse) eventObject.getObject()).getAppVersionUrl();
            //showVersionCheck();/
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashHandler = Handler()
        splashHandler?.postDelayed(splashRunnable, 2000)
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
            nextActivity()
        }

        //TextView textViewVersionInfo = (TextView) findViewById(R.id.textview_version_info);
        //textViewVersionInfo.setText(String.format("Version name = %s \nVersion code = %d", versionName, versionCode));
    }

    private fun nextActivity() {
        if (SharedPreferencesController.getInstance(applicationContext).isUserLoggedIn) {
            ActivityCompat.finishAffinity(this@KSplashActivity)
            val intent = Intent()
            intent.setClass(this@KSplashActivity, DashboardActivity::class.java)
            intent.putExtra(KBAMConstant.Constants.FINISH, true)
            //intent.putExtra("UserName", "");
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
            startActivity(intent)
        } else {
            ActivityCompat.finishAffinity(this@KSplashActivity)
            val intent = Intent()
            intent.setClass(this@KSplashActivity, KLoginActivity::class.java)
            intent.putExtra(KBAMConstant.Constants.FINISH, true)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
            startActivity(intent)
        }
    }


    private fun launchNextActivity() {
        try {
            //if (SharedPreferencesController.getInstance(BAMApplication.getInstance()).isUserLoggedIn()) {
            if (SharedPreferencesController.getInstance(KBAMApplication.ctx).isUserLoggedIn) {
                // Sarvesh Check Current version user using
                showProgress(KBAMConstant.ProgressDialogTexts.AUTHENTICATING)
                BackgroundExecutor.getInstance().execute(KAppVersionRequester())
                /*ActivityCompat.finishAffinity(SplashActivity.this);
                Intent dashBoard = new Intent();
                dashBoard.setClass(SplashActivity.this, DashboardActivity.class);
                dashBoard.putExtra(Constants.FINISH, true);
                dashBoard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                dashBoard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                dashBoard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dashBoard);*/
            } else {
                ActivityCompat.finishAffinity(this@KSplashActivity)
                val intent = Intent()
                intent.setClass(this@KSplashActivity, KLoginActivity::class.java)
                intent.putExtra(KBAMConstant.Constants.FINISH, true)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
                startActivity(intent)
            }
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }

    }
}
