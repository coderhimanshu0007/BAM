package com.teamcomputers.bam.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.core.app.ActivityCompat;

import com.teamcomputers.bam.Models.AppVersionResponse;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Requesters.AppVersionRequester;
import com.teamcomputers.bam.Requesters.LoginRequester;
import com.teamcomputers.bam.Utils.BackgroundExecutor;
import com.teamcomputers.bam.Utils.BAMUtil;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity {
    @BindView(R.id.txtUserName)
    EditText txtUserName;
    @BindView(R.id.txtPassword)
    EditText txtPassword;

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
                        startActivity(dashBoard);
                        break;
                    case Events.WRONG_LOGIN_CREDENTIALS_USED:
                        dismissProgress();
                        showToast(ToastTexts.WRONG_LOGIN_CREDENTIALS_USED);
                        break;
                    case Events.YOU_ARE_USING_OLDER_VERSION_OF_APP:
                        dismissProgress();
                        appUrl = ((AppVersionResponse) eventObject.getObject()).getAppVersionUrl();
                        showVersionCheck();
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
    }

    @OnClick(R.id.btn_login)
    public void submit() {
        BAMUtil.hideSoftKeyboard(this);
        if (!validate()) {
            return;
        }
        showProgress(ProgressDialogTexts.AUTHENTICATING);
        BackgroundExecutor.getInstance().execute(new AppVersionRequester());
        //showProgress(ProgressDialogTexts.AUTHENTICATING);
        //BackgroundExecutor.getInstance().execute(new LoginRequester(txtUserName.getText().toString(), txtPassword.getText().toString()));
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
}
