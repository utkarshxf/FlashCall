package com.example.myapplication.myapplication.flashcall.repository

import com.example.myapplication.myapplication.flashcall.Data.model.feedback.FeedBackResponse
import kotlinx.coroutines.flow.Flow

interface IFeedbackRepo {

    suspend fun getFeedbacks(url : String) : Flow<List<FeedBackResponse>>
}