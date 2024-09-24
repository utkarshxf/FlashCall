package com.example.myapplication.myapplication.flashcall.repository

import android.util.Log
import com.example.myapplication.myapplication.flashcall.Data.model.paymentSetting.AddBankDetailsRequest
import com.example.myapplication.myapplication.flashcall.Data.model.paymentSetting.AddUpiRequest
import com.example.myapplication.myapplication.flashcall.Data.model.paymentSetting.PaymentSettingResponse
import com.example.myapplication.myapplication.flashcall.Data.network.APIService
import com.example.myapplication.myapplication.flashcall.utils.SafeApiRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class PaymentSettingRepository @Inject constructor(private val apiService: APIService): SafeApiRequest() {

    suspend fun getPaymentSetting(url: String): Flow<PaymentSettingResponse> {
        return flow {
            try {
                val response = apiService.getPaymentSettings(url)
                emit(safeApiRequest { response })
            }catch (e: Exception){
                Log.d("PaymentException","error: ${e.message}")
            }
        }
    }
    suspend fun addUPIDetails(url: String, body: AddUpiRequest): Flow<PaymentSettingResponse> {
        return flow {
            val response = apiService.addUpiDetails(url, body)
            emit(safeApiRequest { response })
        }
    }
    suspend fun addBankDetails(url: String, body: AddBankDetailsRequest): Flow<PaymentSettingResponse> {
        return flow {
            val response = apiService.addBankDetails(url, body)
            emit(safeApiRequest { response })
        }
    }
}