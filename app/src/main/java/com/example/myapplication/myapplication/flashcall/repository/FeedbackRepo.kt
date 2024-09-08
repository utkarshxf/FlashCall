package com.example.myapplication.myapplication.flashcall.repository

import android.util.Log
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.ValidateResponse
import com.example.myapplication.myapplication.flashcall.Data.model.feedback.FeedbackResponse
import com.example.myapplication.myapplication.flashcall.Data.model.feedback.UpdateFeedback
import com.example.myapplication.myapplication.flashcall.Data.model.feedback.UpdateFeedbackResponse
import com.example.myapplication.myapplication.flashcall.Data.network.APIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FeedbackRepo @Inject constructor(private val apiService: APIService) : IFeedbackRepo {

    override suspend fun getFeedbacks(url: String): Flow<FeedbackResponse> {

        return flow {
//            https://app.flashcall.me/api/v1/feedback/creator/selected?creatorId
            val response = apiService.getFeedbacks("https://app.flashcall.me/api/v1/feedback/creator/selected?creatorId=66d97acd79d3bf54c8e53be0")
            emit(response)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun updateFeedback(url: String, updateFeedback: UpdateFeedback): Flow<UpdateFeedbackResponse> {
        return flow {
            val response = apiService.updateFeedback("https://app.flashcall.me/api/v1/feedback/creator/setFeedback", updateFeedback)
            emit(response)
        }.flowOn(Dispatchers.IO)
    }
}