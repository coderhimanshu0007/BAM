package com.teamcomputers.bam.Activities

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import com.teamcomputers.bam.Interface.BAMConstant
import com.teamcomputers.bam.Interface.KBAMConstant
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.R
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.*

abstract class KBaseActivity : AppCompatActivity(), KBAMConstant {
    val SHOW_TOAST = 0
    internal var mActivity: Activity? = null
    protected var alertDialogUpdateApp: AlertDialog? = null
    protected var builderUpdateApp: AlertDialog.Builder? = null
    protected var errorAlert: AlertDialog.Builder? = null
    private var progressDialog: ProgressDialog? = null
    var appUrl: String? = null
    val pauseHandler: PauseHandler? = null

    @LayoutRes
    protected abstract fun getLayout(): Int

    @Subscribe
    open abstract fun onEvent(eventObject: EventObject)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())

        injectViews()
        initUpdateApp()
        showError()
        EventBus.getDefault().register(this)
        initProgressDialog(BAMConstant.ProgressDialogTexts.AUTHENTICATING)
        // startScrollActivity(BaseActivity.class);
    }

    private fun initProgressDialog(message: String) {
        progressDialog = ProgressDialog(this)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage(message)
    }

    private fun startScrollActivity(activity: Class<*>) {
        startActivity(Intent(this, activity))
    }

    private fun injectViews() {
        ButterKnife.bind(this)
    }

    fun showProgress(msg: String) {
        progressDialog!!.setMessage(msg)
        progressDialog!!.show()
    }

    fun dismissProgress() {
        progressDialog!!.dismiss()
    }

    fun showToast(toastMessage: String) {
        sendMessageToHandler(SHOW_TOAST, -1, 1, toastMessage)
    }

    private fun sendMessageToHandler(what: Int, arg1: Int, arg2: Int, response: String) {
        val message = pauseHandler?.obtainMessage()
        message?.obj = response
        message?.what = what
        message?.arg1 = arg1
        message?.arg2 = arg2
        pauseHandler?.sendMessage(message)
    }

    abstract inner class PauseHandler : Handler() {

        /**
         * Message Queue Buffer
         */
        val messageQueueBuffer = Vector<Message>()
        abstract var baseActivity: KBaseActivity
        /**
         * Flag indicating the pause state
         */
        internal var isPaused: Boolean = false
            private set

        /**
         * Resume the handler
         */
        internal fun resume() {
            isPaused = false

            while (messageQueueBuffer.size > 0) {
                val msg = messageQueueBuffer.elementAt(0)
                messageQueueBuffer.removeElementAt(0)
                sendMessage(msg)
            }
        }

        /**
         * Pause the handler
         */
        internal fun pause() {
            isPaused = true
        }

        internal fun setBaseActivity(baseActivity: KBaseActivity) {
            this.baseActivity = baseActivity
        }

        internal fun storeMessage(message: Message): Boolean {
            return true
        }


        override fun handleMessage(msg: Message) {
            if (isPaused) {
                if (storeMessage(msg)) {
                    val msgCopy = Message()
                    msgCopy.copyFrom(msg)
                    messageQueueBuffer.add(msgCopy)
                }
            } else {
                processMessage(msg)
            }
        }
    }

    fun processMessage(message: Message) {
        when (message.what) {
            SHOW_TOAST -> {
                val msg = message.obj as String
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun showVersionCheck() {
        if (alertDialogUpdateApp == null) {
            alertDialogUpdateApp = builderUpdateApp!!.show()
        } else if (!alertDialogUpdateApp!!.isShowing) {
            builderUpdateApp!!.show()
        }
    }

    private fun initUpdateApp() {
        if (builderUpdateApp == null) {
            builderUpdateApp = AlertDialog.Builder(this)
            builderUpdateApp!!.setCancelable(false)
            builderUpdateApp!!.setMessage(BAMConstant.ToastTexts.YOU_ARE_USING_OLDER_VERSION_OF_TEAM_WORKS_APP_PLEASE_USE_LATEST_VERSION)
            builderUpdateApp!!.setTitle(BAMConstant.ToastTexts.INFORMATION)
            builderUpdateApp!!.setNegativeButton(BAMConstant.ToastTexts.CANCEL) { dialog, which -> alertDialogUpdateApp!!.dismiss() }
            builderUpdateApp!!.setPositiveButton(BAMConstant.ToastTexts.UPDATE) { dialog, which ->
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(appUrl))
                startActivity(browserIntent)
            }
        }
    }

    fun showDialog(mAct: Activity) {
        mActivity = mAct;
        if (alertDialogUpdateApp == null) {
            alertDialogUpdateApp = errorAlert?.show()
        } else if (!alertDialogUpdateApp!!.isShowing()) {
            errorAlert?.show()
        }
    }

    private fun showError() {
        if (errorAlert == null) {
            errorAlert = AlertDialog.Builder(this)
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.error_dialog, null)
            errorAlert!!.setView(dialogView)
            errorAlert!!.setCancelable(false)

            val btnOk = dialogView.findViewById<View>(R.id.btnOk) as Button
            btnOk.setOnClickListener {
                alertDialogUpdateApp!!.dismiss()
                mActivity?.finish()
            }
        }
    }
}