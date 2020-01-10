package com.teamcomputers.bam.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;


import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

import com.teamcomputers.bam.Interface.BAMConstant;
import com.teamcomputers.bam.Models.common.EventObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Vector;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements BAMConstant {

    public final int SHOW_TOAST = 0;
    protected AlertDialog alertDialogUpdateApp;
    protected AlertDialog.Builder builderUpdateApp;
    private ProgressDialog progressDialog;
    public String appUrl;
    private PauseHandler pauseHandler = new PauseHandler();

    protected abstract
    @LayoutRes
    int getLayout();

    @Subscribe
    public abstract void onEvent(EventObject eventObject);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());

        injectViews();
        initUpdateApp();
        EventBus.getDefault().register(this);
        initProgressDialog(ProgressDialogTexts.AUTHENTICATING);
       // startScrollActivity(BaseActivity.class);
    }

    private void initProgressDialog(String message) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage(message);
    }

    private void startScrollActivity(Class<?> activity) {
        startActivity(new Intent(this, activity));
    }

    private void injectViews() {
        ButterKnife.bind(this);
    }

    public void showProgress(String msg) {
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    public void dismissProgress() {
        progressDialog.dismiss();
    }

    public void showToast(String toastMessage) {
        sendMessageToHandler(SHOW_TOAST, -1, 1, toastMessage);
    }

    private void sendMessageToHandler(int what, int arg1, int arg2, String response) {
        Message message = pauseHandler.obtainMessage();
        message.obj = response;
        message.what = what;
        message.arg1 = arg1;
        message.arg2 = arg2;
        pauseHandler.sendMessage(message);
    }

    private class PauseHandler extends Handler {

        /**
         * Message Queue Buffer
         */
        final Vector<Message> messageQueueBuffer = new Vector<>();
        BaseActivity baseActivity;
        /**
         * Flag indicating the pause state
         */
        private boolean paused;

        /**
         * Resume the handler
         */
        final void resume() {
            paused = false;

            while (messageQueueBuffer.size() > 0) {
                final Message msg = messageQueueBuffer.elementAt(0);
                messageQueueBuffer.removeElementAt(0);
                sendMessage(msg);
            }
        }

        /**
         * Pause the handler
         */
        final void pause() {
            paused = true;
        }

        boolean isPaused() {
            return paused;
        }

        final void setBaseActivity(BaseActivity baseActivity) {
            this.baseActivity = baseActivity;
        }

        boolean storeMessage(Message message) {
            return true;
        }


        @Override
        final public void handleMessage(Message msg) {
            if (paused) {
                if (storeMessage(msg)) {
                    Message msgCopy = new Message();
                    msgCopy.copyFrom(msg);
                    messageQueueBuffer.add(msgCopy);
                }
            } else {
                processMessage(msg);
            }
        }
    }

    public void processMessage(Message message) {
        switch (message.what) {
            case SHOW_TOAST:
                String msg = (String) message.obj;
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void showVersionCheck() {
        if (alertDialogUpdateApp == null) {
            alertDialogUpdateApp = builderUpdateApp.show();
        } else if (!alertDialogUpdateApp.isShowing()) {
            builderUpdateApp.show();
        }
    }

    private void initUpdateApp() {
        if (builderUpdateApp == null) {
            builderUpdateApp = new AlertDialog.Builder(this);
            builderUpdateApp.setCancelable(false);
            builderUpdateApp.setMessage(ToastTexts.YOU_ARE_USING_OLDER_VERSION_OF_TEAM_WORKS_APP_PLEASE_USE_LATEST_VERSION);
            builderUpdateApp.setTitle(ToastTexts.INFORMATION);
            builderUpdateApp.setNegativeButton(ToastTexts.CANCEL, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    alertDialogUpdateApp.dismiss();
                }
            });
            builderUpdateApp.setPositiveButton(ToastTexts.UPDATE, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(appUrl));
                    startActivity(browserIntent);
                }
            });
        }
    }
}
