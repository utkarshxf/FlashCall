package com.example.myapplication.myapplication.flashcall.repository

import com.example.myapplication.myapplication.flashcall.Data.model.feedback.UpdateFeedbackCallRequest
import com.example.myapplication.myapplication.flashcall.Data.model.feedback.UpdateFeedbackCreatorRequest
import com.example.myapplication.myapplication.flashcall.Data.model.feedback.UpdateFeedbackResponse
import com.example.myapplication.myapplication.flashcall.Data.model.userFeedbacks.FeedbackResponseItem
import com.example.myapplication.myapplication.flashcall.Data.network.APIService
import com.example.myapplication.myapplication.flashcall.utils.SafeApiRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FeedbackRepo @Inject constructor(private val apiService: APIService) : IFeedbackRepo,SafeApiRequest() {

    override suspend fun getFeedbacks(url: String): Flow<List<FeedbackResponseItem>> {

        return flow {
//            https://app.flashcall.me/api/v1/feedback/creator/selected?creatorId
            val response = apiService.getFeedbacks(url)
            emit(safeApiRequest {response})
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun updateFeedbackCreator(url: String, updateFeedback: UpdateFeedbackCreatorRequest): Flow<UpdateFeedbackResponse> {
        return flow {
            val response = apiService.updateFeedbackCreator(url = url, updateFeedback)
            emit(safeApiRequest { response } )
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun updateFeedbackCall(url: String, updateFeedback: UpdateFeedbackCallRequest): Flow<UpdateFeedbackResponse> {
        return flow {
            val response = apiService.updateFeedbackCall(url = url, updateFeedback)
            emit(safeApiRequest { response } )
        }.flowOn(Dispatchers.IO)
    }
}