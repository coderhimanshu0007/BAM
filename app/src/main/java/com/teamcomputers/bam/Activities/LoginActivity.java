package com.teamcomputers.bam.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.teamcomputers.bam.Models.AppVersionResponse;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.KAppVersionRequester;
import com.teamcomputers.bam.Requesters.KLoginRequester;
import com.teamcomputers.bam.Requesters.LoginRequester;
import com.teamcomputers.bam.Utils.BackgroundExecutor;
import com.teamcomputers.bam.Utils.KBAMUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity {
    @BindView(R.id.txtUserName)
    EditText txtUserName;
    @BindView(R.id.txtPassword)
    EditText txtPassword;
    @BindView(R.id.iviShowPassword)
    ImageView iviShowPassword;
    @BindView(R.id.iviSaveUserId)
    ImageView iviSaveUserId;
    @BindView(R.id.tviCurrentVersion)
    TextView tviCurrentVersion;
    boolean save = false, show = false;

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Subscribe
    @Override
    public void onEvent(EventObject eventObject) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (eventObject.getId()) {
                    case Events.LOGIN_SUCCESSFUL:
                        dismissProgress();
                        ActivityCompat.finishAffinity(LoginActivity.this);
                        Intent dashBoard = new Intent();
                        dashBoard.setClass(LoginActivity.this, DashboardActivity.class);
                        dashBoard.putExtra(Constants.FINISH, true);
                        dashBoard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        dashBoard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        dashBoard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                        startActivity(dashBoard);
                        break;
                    case Events.WRONG_LOGIN_CREDENTIALS_USED:
                        dismissProgress();
                        showToast(ToastTexts.WRONG_LOGIN_CREDENTIALS_USED);
                        break;
                    case Events.YOU_ARE_USING_OLDER_VERSION_OF_APP:
                        dismissProgress();
                        getVersionInfo((AppVersionResponse) eventObject.getObject());
                        //appUrl = ((AppVersionResponse) eventObject.getObject()).getAppVersionUrl();
                        //showVersionCheck();
                        break;
                    case Events.YOU_ARE_USING_CURRENT_VERSION_OF_APP:
                        //showProgress(ProgressDialogTexts.AUTHENTICATING);
                        BackgroundExecutor.getInstance().execute(new LoginRequester(txtUserName.getText().toString(), txtPassword.getText().toString()));
                        break;
                    case Events.NO_INTERNET_CONNECTION:
                        dismissProgress();
                        showToast(ToastTexts.NO_INTERNET_CONNECTION);
                        break;
                    case Events.LOGIN_UN_SUCCESSFUL:
                        dismissProgress();
                        showToast(ToastTexts.LOGIN_UNSUCCESSFULL);
                        break;
                    case Events.OOPS_MESSAGE:
                        dismissProgress();
                        showToast(ToastTexts.OOPS_MESSAGE);
                        break;
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        save = false;
        show = false;
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();

        try {
            PackageInfo pInfo = LoginActivity.this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            //showToast(version);
            tviCurrentVersion.setText("Current version " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.iviSaveUserId)
    public void saveUserId() {
        /*if (!save) {
            //SharedPreferencesController.getInstance(LoginActivity.this).setUserId();
            save = true;
            iviSaveUserId.setColorFilter(ContextCompat.getColor(LoginActivity.this, R.color.end_header_color_bg), android.graphics.PorterDuff.Mode.SRC_IN);
        } else if (save) {
            save = false;
            iviSaveUserId.setColorFilter(ContextCompat.getColor(LoginActivity.this, R.color.text_color_login), android.graphics.PorterDuff.Mode.SRC_IN);
        }*/
    }

    @OnClick(R.id.iviShowPassword)
    public void showPassword() {
        /*if (!show) {
            txtPassword.setTransformationMethod(null);
            show = true;
            iviShowPassword.setColorFilter(ContextCompat.getColor(LoginActivity.this, R.color.end_header_color_bg), android.graphics.PorterDuff.Mode.SRC_IN);
        } else if (show) {
            txtPassword.setTransformationMethod(new PasswordTransformationMethod());
            show = false;
            iviShowPassword.setColorFilter(ContextCompat.getColor(LoginActivity.this, R.color.text_color_login), android.graphics.PorterDuff.Mode.SRC_IN);
        }*/
    }

    @OnClick(R.id.btn_login)
    public void submit() {
        new KBAMUtils().hideSoftKeyboard(this);
        if (!validate()) {
            return;
        }
        showProgress(ProgressDialogTexts.AUTHENTICATING);
        BackgroundExecutor.getInstance().execute(new KAppVersionRequester());
        //BackgroundExecutor.getInstance().execute(new AppVersionRequester());
        //showProgress(ProgressDialogTexts.AUTHENTICATING);
        //BackgroundExecutor.getInstance().execute(new KLoginRequester(txtUserName.getText().toString(), txtPassword.getText().toString()));
    }

    public boolean validate() {
        boolean valid = true;

        String userName = txtUserName.getText().toString();
        String password = txtPassword.getText().toString();

        if (userName.isEmpty()) {
            txtUserName.setError("Enter a valid TMC");
            valid = false;
        } else {
            txtUserName.setError(null);
        }

        if (password.isEmpty()) {
            txtPassword.setError("Enter your password");
            valid = false;
        } else {
            txtPassword.setError(null);
        }

        return valid;
    }

    private void getVersionInfo(AppVersionResponse object) {
        String versionName = "";
        int versionCode = -1;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (Float.parseFloat(object.getAppVersionID()) > Float.parseFloat(versionName)) {
            showVersionCheck();
        } else {
            BackgroundExecutor.getInstance().execute(new KLoginRequester(txtUserName.getText().toString(), txtPassword.getText().toString()));
        }

        //TextView textViewVersionInfo = (TextView) findViewById(R.id.textview_version_info);
        //textViewVersionInfo.setText(String.format("Version name = %s \nVersion code = %d", versionName, versionCode));
    }


}
