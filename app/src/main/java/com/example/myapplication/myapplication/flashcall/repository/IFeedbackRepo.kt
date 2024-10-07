package com.example.myapplication.myapplication.flashcall.repository
import com.example.myapplication.myapplication.flashcall.Data.model.feedback.UpdateFeedbackCallRequest
import com.example.myapplication.myapplication.flashcall.Data.model.feedback.UpdateFeedbackCreatorRequest
import com.example.myapplication.myapplication.flashcall.Data.model.feedback.UpdateFeedbackResponse
import com.example.myapplication.myapplication.flashcall.Data.model.userFeedbacks.FeedbackResponseItem
import kotlinx.coroutines.flow.Flow

interface IFeedbackRepo {
    suspend fun getFeedbacks(url : String) : Flow<List<FeedbackResponseItem>>
    suspend fun updateFeedbackCreator(url : String, updateFeedback: UpdateFeedbackCreatorRequest) : Flow<UpdateFeedbackResponse>
    suspend fun updateFeedbackCall(url : String, updateFeedback: UpdateFeedbackCallRequest) : Flow<UpdateFeedbackResponse>
}