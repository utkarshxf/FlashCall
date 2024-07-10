package com.example.myapplication.myapplication.flashcall.repository

import com.example.myapplication.myapplication.flashcall.Data.model.feedback.FeedBackResponse
import com.example.myapplication.myapplication.flashcall.Data.network.APIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FeedbackRepo @Inject constructor(private val apiService: APIService) : IFeedbackRepo {

    override suspend fun getFeedbacks(url: String): Flow<List<FeedBackResponse>> {

        return flow {

            val response = apiService.getFeedbacks(url)

            emit(response)

        }.flowOn(Dispatchers.IO)
    }


}