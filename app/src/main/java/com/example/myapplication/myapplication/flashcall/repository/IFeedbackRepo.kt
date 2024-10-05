package com.example.myapplication.myapplication.flashcall.repository

import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.ValidateResponse
//import com.example.myapplication.myapplication.flashcall.Data.model.feedback.FeedbackResponse
import com.example.myapplication.myapplication.flashcall.Data.model.feedback.UpdateFeedback
import com.example.myapplication.myapplication.flashcall.Data.model.feedback.UpdateFeedbackResponse
import com.example.myapplication.myapplication.flashcall.Data.model.userFeedbacks.FeedbackResponseItem
import kotlinx.coroutines.flow.Flow

interface IFeedbackRepo {
    suspend fun getFeedbacks(url : String) : Flow<List<FeedbackResponseItem>>
    suspend fun updateFeedback(url : String, updateFeedback: UpdateFeedback) : Flow<UpdateFeedbackResponse>
}