package com.teamcomputers.bam.webservice

import android.util.Log
import com.teamcomputers.bam.Interface.BAMConstant
import retrofit2.Call
import java.io.IOException

public class KConnectionUtils : BAMConstant{

    private val TAG = KConnectionUtils::class.java.name

    fun <T> execute(call: Call<*>): ApiResponse<*>? {
        try {
            val response = call.execute()
            return ApiResponse(response.code(), response.body(), response.headers())
        } catch (e: IOException) {
            Log.d(TAG, "Error in execute api request")
        } catch (ex: Exception) {
            Log.d(TAG, "Error in execute api" + ex.message)
        }

        return null

    }
}