package com.example.myapplication.myapplication.flashcall.utils

import android.util.Log
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

abstract class SafeApiRequest {
    suspend fun <T : Any> safeApiRequest(call: suspend () -> Response<T>): T {
        val response = call.invoke()
        if (response.isSuccessful) {
            if(response.body() != null){
                return response.body()!!
            }else{
                throw Exception("Base Url Error")
            }

        } else {
            val responseErr = response.errorBody()?.string()
            val message = StringBuilder()
            responseErr.let {
                try {
                    message.append(JSONObject(it).getString("error"))
                } catch (e: JSONException) {
                }
            }
            Log.d("TAG", "safeApiRequest: ${message.toString()}")
            throw Exception(message.toString())
        }
    }

}