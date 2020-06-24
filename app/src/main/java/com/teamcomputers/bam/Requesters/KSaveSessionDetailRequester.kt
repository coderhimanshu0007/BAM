package com.teamcomputers.bam.Requesters

import android.util.Log
import com.teamcomputers.bam.BAMApplication
import com.teamcomputers.bam.Interface.KBAMConstant
import com.teamcomputers.bam.Interface.KBaseRequester
import com.teamcomputers.bam.Models.SessionDetailsModel
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.controllers.SharedPreferencesController
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import java.net.HttpURLConnection


class KSaveSessionDetailRequester(val session: String) : KBaseRequester {

    var client = OkHttpClient().newBuilder()
            .build()
    var mediaType = MediaType.parse("application/json")
    var body = RequestBody.create(mediaType, session)
    /*var body = RequestBody.create(mediaType, "{\n\t\"SessionId\":" + session.getSessionId() + ",\n\t\"Data\":[\n\t\t" +

            "{\n\t\t\t\"Module\":\"" + session?.getData()?.get(0)?.module + "\"," +
            "\n\t\t\t\"Page\":\"" + session?.getData()?.get(0)?.page + "\"," +
            "\n\t\t\t\"LogInTimeStamp\":\"" + session?.getData()?.get(0)?.logInTimeStamp + "\"," +
            "\n\t\t\t\"LogOutTimeStamp\":\"" + session?.getData()?.get(0)?.logOutTimeStamp + "\"," +
            "\n\t\t\t\"OS\":\"" + session?.getData()?.get(0)?.os + "\"," +
            "\n\t\t\t\"Sharing\":" + session?.getData()?.get(0)?.sharing + "\n\t\t}" +

            "{\n\t\t\t\"Module\":\"" + session?.getData()?.get(0)?.module + "\"," +
            "\n\t\t\t\"Page\":\"" + session?.getData()?.get(0)?.page + "\"," +
            "\n\t\t\t\"LogInTimeStamp\":\"" + session?.getData()?.get(0)?.logInTimeStamp + "\"," +
            "\n\t\t\t\"LogOutTimeStamp\":\"" + session?.getData()?.get(0)?.logOutTimeStamp + "\"," +
            "\n\t\t\t\"OS\":\"" + session?.getData()?.get(0)?.os + "\"," +
            "\n\t\t\t\"Sharing\":" + session?.getData()?.get(0)?.sharing + "\n\t\t}" +
            "\n\t]\n}")*/
    var request = Request.Builder()
            .url("http://bam.teamcomputers.com:5558/api/SaveSessionDetails")
            .method("POST", body)
            .addHeader("Content-Type", "application/json")
            .build()

    override fun run() {
        var apiResponse = client.newCall(request).execute()
        //val apiResponse = KHTTPOperationController().saveSessionDetail(saveSessionDetailsModel)
        if (apiResponse != null) {
            if (apiResponse.code() == HttpURLConnection.HTTP_OK) {
                if (apiResponse != null) {
                    SharedPreferencesController.getInstance(BAMApplication.getInstance()).sessionDetail = null
                    //EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_SAVE_SESSION_DETAIL_SUCCESSFUL, null))
                    Log.d("Session", "Saved");
                } else {
                    //EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_SAVE_SESSION_DETAIL_UNSUCCESSFUL, null))
                    Log.d("Session", "Failed");
                }
            }
        } else {
            EventBus.getDefault().post(EventObject(KBAMConstant.Events.NO_INTERNET_CONNECTION, null))
        }
    }
}