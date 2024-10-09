package com.example.myapplication.myapplication.flashcall.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.myapplication.flashcall.Data.model.orderHistory.block.request.BlockUnblockRequestBody
import com.example.myapplication.myapplication.flashcall.Data.model.orderHistory.report.reportRequest.ReportRequestBody
import com.example.myapplication.myapplication.flashcall.repository.OrderHistoryRepository
import com.example.myapplication.myapplication.flashcall.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val repository: OrderHistoryRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    @ApplicationContext private val context: Context,
): ViewModel() {


    fun getOrderHistory(){
        val userId = userPreferencesRepository.getUser()?._id+""
        //66fd37a1735a8e07837d5d99
        viewModelScope.launch {
            repository.getOrderHistory(url = "https://backend.flashcall.me/api/v1/calls/getUserCalls?userId=$userId&all=true")
                .collect{

            }
        }
    }

    fun reportUser(body: ReportRequestBody){
        viewModelScope.launch {
            repository.reportUser(url = "https://backend.flashcall.me/api/v1/reports/register", body = body)
                .collect{

            }
        }
    }

    fun blockUser(body: BlockUnblockRequestBody){
        //"66ab32f4321d76e89d77464b"
        val userId = userPreferencesRepository.getUser()?._id+""
        viewModelScope.launch {
            repository.blockUser(url = "https://backend.flashcall.me/api/v1/creator/blockUser/$userId", body = body)
                .collect{

                }
        }
    }


}