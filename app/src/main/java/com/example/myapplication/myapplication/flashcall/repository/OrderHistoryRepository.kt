package com.example.myapplication.myapplication.flashcall.repository

import com.example.myapplication.myapplication.flashcall.Data.model.VerifyPanRequest
import com.example.myapplication.myapplication.flashcall.Data.model.livelinessResponse.KycResponse
import com.example.myapplication.myapplication.flashcall.Data.model.orderHistory.OrderHistoryResponse
import com.example.myapplication.myapplication.flashcall.Data.model.orderHistory.block.request.BlockUnblockRequestBody
import com.example.myapplication.myapplication.flashcall.Data.model.orderHistory.block.response.BlockUnblockResponse
import com.example.myapplication.myapplication.flashcall.Data.model.orderHistory.report.reportRequest.ReportRequestBody
import com.example.myapplication.myapplication.flashcall.Data.model.orderHistory.report.responseBody.ReportResponseBody
import com.example.myapplication.myapplication.flashcall.Data.network.APIService
import com.example.myapplication.myapplication.flashcall.utils.SafeApiRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OrderHistoryRepository @Inject constructor(private val apiService: APIService): SafeApiRequest() {

    //"https://backend.flashcall.me/api/v1/calls/getUserCalls?userId=66fd37a1735a8e07837d5d99&page=1&limit=10"
    suspend fun getOrderHistory(url: String): Flow<OrderHistoryResponse?> {
        return flow {
            val response1 = safeApiRequest{ apiService.getOrderHistory(url)}
            emit(response1)
        }
    }

    //"https://backend.flashcall.me/api/v1/reports/register"
    suspend fun reportUser(url: String, body: ReportRequestBody): Flow<ReportResponseBody?> {
        return flow {
            val response1 = safeApiRequest{ apiService.reportUser(url = url, body = body)}
            emit(response1)
        }
    }


    suspend fun blockUser(url: String, body: BlockUnblockRequestBody): Flow<BlockUnblockResponse?> {
        return flow {
            val response1 = safeApiRequest{ apiService.blockUser(url = url, body = body)}
            emit(response1)
        }
    }
}