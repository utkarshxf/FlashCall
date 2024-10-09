package com.example.myapplication.myapplication.flashcall.ViewModel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.example.myapplication.myapplication.flashcall.Data.model.orderHistory.block.request.BlockUnblockRequestBody
import com.example.myapplication.myapplication.flashcall.Data.model.orderHistory.report.reportRequest.ReportRequestBody
import com.example.myapplication.myapplication.flashcall.repository.OrderHistoryRepository
import com.example.myapplication.myapplication.flashcall.repository.UserPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.myapplication.flashcall.Data.model.orderHistory.OrderHistoryResponse
import com.example.myapplication.myapplication.flashcall.Data.model.orderHistory.block.response.BlockUnblockResponse
import com.example.myapplication.myapplication.flashcall.Data.model.orderHistory.report.responseBody.ReportResponseBody
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val orderHistoryRepository: OrderHistoryRepository
) : ViewModel() {

    // Flow to hold order history results
    private val _orderHistory = MutableStateFlow<OrderHistoryResponse?>(null)
    val orderHistory: StateFlow<OrderHistoryResponse?> = _orderHistory

    // Flow to hold report user response
    private val _reportUserResponse = MutableStateFlow<ReportResponseBody?>(null)
    val reportUserResponse: StateFlow<ReportResponseBody?> = _reportUserResponse

    // Flow to hold block user response
    private val _blockUserResponse = MutableStateFlow<BlockUnblockResponse?>(null)
    val blockUserResponse: StateFlow<BlockUnblockResponse?> = _blockUserResponse

    // Function to get order history
    //  66fd37a1735a8e07837d5d99
    fun getOrderHistory(userId: String, page: Int, limit: Int) {
        viewModelScope.launch {
            val url = "https://backend.flashcall.me/api/v1/calls/getUserCalls?userId=$userId&page=$page&limit=$limit"
            orderHistoryRepository.getOrderHistory(url)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    // Handle error if needed
                    _orderHistory.value = null
                }
                .collect { result ->
                    _orderHistory.value = result
                }
        }

    }

    // Function to report a user
    fun reportUser(body: ReportRequestBody) {
        viewModelScope.launch {
            val url = "https://backend.flashcall.me/api/v1/reports/register"
            orderHistoryRepository.reportUser(url, body)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    // Handle error if needed
                    _reportUserResponse.value = null
                }
                .collect { result ->
                    _reportUserResponse.value = result
                }
        }
    }

    // Function to block a user
    fun blockUser(body: BlockUnblockRequestBody) {
        viewModelScope.launch {
            val url = "https://backend.flashcall.me/api/v1/user/block"
            orderHistoryRepository.blockUser(url, body)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    // Handle error if needed
                    _blockUserResponse.value = null
                }
                .collect { result ->
                    _blockUserResponse.value = result
                }
        }
    }
}