package com.teamcomputers.bam.Service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.teamcomputers.bam.BAMApplication
import com.teamcomputers.bam.Interface.BAMConstant
import com.teamcomputers.bam.Models.ActiveEmployeeAccessModel
import com.teamcomputers.bam.Models.SessionDataModel
import com.teamcomputers.bam.Models.SessionDetailsModel
import com.teamcomputers.bam.Requesters.KSaveSessionDetailRequester
import com.teamcomputers.bam.Utils.BackgroundExecutor
import com.teamcomputers.bam.Utils.KBAMUtils
import com.teamcomputers.bam.controllers.SharedPreferencesController
import java.util.*


class OnClearFromRecentService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d("ClearFromRecentService", "Service Started")
        return Service.START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ClearFromRecentService", "Service Destroyed")
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        Log.e("ClearFromRecentService", "END")
        //Code here
        var activeEmployeeAccessModel: ActiveEmployeeAccessModel = SharedPreferencesController.getInstance(BAMApplication.getInstance()).activeEmployeeAccess
        var currentDate: Date = Calendar.getInstance().time
        var logOutTime: String = KBAMUtils.getFormattedDate(BAMConstant.DateFormat.SESSION_DATE_FORMAT, currentDate)

        val sessionDetailsModel = SessionDetailsModel()
        sessionDetailsModel.setSessionId(activeEmployeeAccessModel.getData()!!.sessionId)
        var sessionDataModelList: MutableList<SessionDataModel>? = ArrayList()
        if (null != SharedPreferencesController.getInstance(baseContext).sessionDetail) {
            sessionDataModelList = SharedPreferencesController.getInstance(baseContext).sessionDetail.getData() as MutableList<SessionDataModel>?
        }
        var sessionData: SessionDataModel = SharedPreferencesController.getInstance(BAMApplication.getInstance()).sessionData
        sessionData.logOutTimeStamp = logOutTime
        sessionData.os = "Android"
        sessionData.sharing = SharedPreferencesController.getInstance(BAMApplication.getInstance()).sharing
        sessionDataModelList?.add(sessionData)
        sessionDataModelList?.let { sessionDetailsModel.setData(it) }
        SharedPreferencesController.getInstance(baseContext).sessionDetail = sessionDetailsModel

        uploadData()
        stopSelf()
    }

    private fun uploadData() {
        val sessionDetailsModel = SharedPreferencesController.getInstance(baseContext).sessionDetail
        if (null != sessionDetailsModel) {
            var session = "{\n\t\"SessionId\":" + sessionDetailsModel.getSessionId() + ",\n\t\"Data\":[\n\t\t"
            for (i in 0 until sessionDetailsModel.getData()!!.size) {
                val data = "{\n\t\t\t\"Module\":\"" + sessionDetailsModel.getData()!![i].module + "\"," +
                        "\n\t\t\t\"Page\":\"" + sessionDetailsModel.getData()!![i].page + "\"," +
                        "\n\t\t\t\"LogInTimeStamp\":\"" + sessionDetailsModel.getData()!![i].logInTimeStamp + "\"," +
                        "\n\t\t\t\"LogOutTimeStamp\":\"" + sessionDetailsModel.getData()!![i].logOutTimeStamp + "\"," +
                        "\n\t\t\t\"OS\":\"" + sessionDetailsModel.getData()!![i].os + "\"," +
                        "\n\t\t\t\"Sharing\":" + sessionDetailsModel.getData()!![i].sharing + "\n\t\t}"
                if (i > 0) {
                    session = "$session,$data"
                } else {
                    session = session + data
                }
            }
            session = "$session\n\t]\n}"
            BackgroundExecutor.getInstance().execute(KSaveSessionDetailRequester(session))
        }
    }
}