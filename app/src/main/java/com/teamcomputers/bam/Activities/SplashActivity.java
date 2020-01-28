package com.teamcomputers.bam.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.core.app.ActivityCompat;

import com.teamcomputers.bam.BAMApplication;
import com.teamcomputers.bam.Models.AppVersionResponse;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.AppVersionRequester;
import com.teamcomputers.bam.Utils.BackgroundExecutor;
import com.teamcomputers.bam.controllers.SharedPreferencesController;

import org.greenrobot.eventbus.Subscribe;


public class SplashActivity extends BaseActivity {
    Handler splashHandler;
    private Runnable splashRunnable = new Runnable() {
        @Override
        public void run() {
            launchNextActivity();
        }
    };

    @Override
    protected int getLayout() {
        return R.layout.activity_splash;
    }

    @Subscribe
    @Override
    public void onEvent(EventObject eventObject) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissProgress();
                switch (eventObject.getId()) {
                    case Events.YOU_ARE_USING_OLDER_VERSION_OF_APP:
                        appUrl = ((AppVersionResponse) eventObject.getObject()).getAppVersionUrl();
                        showVersionCheck();
                        break;
                    case Events.YOU_ARE_USING_CURRENT_VERSION_OF_APP:
                        if (SharedPreferencesController.getInstance(getApplicationContext()).isUserLoggedIn()) {
                            ActivityCompat.finishAffinity(SplashActivity.this);
                            Intent intent = new Intent();
                            intent.setClass(SplashActivity.this, DashboardActivity.class);
                            intent.putExtra(Constants.FINISH, true);
                            //intent.putExtra("UserName", "");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                            startActivity(intent);
                        } else {
                            ActivityCompat.finishAffinity(SplashActivity.this);
                            Intent intent = new Intent();
                            intent.setClass(SplashActivity.this, LoginActivity.class);
                            intent.putExtra(Constants.FINISH, true);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                            startActivity(intent);
                        }
                        break;
                    case Events.OOPS_MESSAGE:
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                    case Events.NO_INTERNET_CONNECTION:
                        showToast(ToastTexts.NO_INTERNET_CONNECTION);
                        break;
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashHandler = new Handler();
        splashHandler.postDelayed(splashRunnable, DELAY_MILLIS);
    }

    private void launchNextActivity() {
        try {
            if (SharedPreferencesController.getInstance(BAMApplication.getInstance()).isUserLoggedIn()) {
                // Sarvesh Check Current version user using
                showProgress(ProgressDialogTexts.AUTHENTICATING);
                BackgroundExecutor.getInstance().execute(new AppVersionRequester());
                /*ActivityCompat.finishAffinity(SplashActivity.this);
                Intent dashBoard = new Intent();
                dashBoard.setClass(SplashActivity.this, DashboardActivity.class);
                dashBoard.putExtra(Constants.FINISH, true);
                dashBoard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                dashBoard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                dashBoard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dashBoard);*/
            } else {
                ActivityCompat.finishAffinity(SplashActivity.this);
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, LoginActivity.class);
                intent.putExtra(Constants.FINISH, true);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                startActivity(intent);
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }
}
