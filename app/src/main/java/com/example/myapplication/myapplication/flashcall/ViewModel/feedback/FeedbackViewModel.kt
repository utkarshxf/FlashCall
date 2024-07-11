package com.example.myapplication.myapplication.flashcall.ViewModel.feedback

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.myapplication.myapplication.flashcall.Data.model.APIResponse
import com.example.myapplication.myapplication.flashcall.Data.model.feedback.FeedBackResponse
import com.example.myapplication.myapplication.flashcall.repository.FeedbackRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val feedbackRepo: FeedbackRepo
) : ViewModel() {

    private val _feedbacks = MutableStateFlow<List<FeedBackResponse>>(emptyList())
    val feedbacks: StateFlow<List<FeedBackResponse>> = _feedbacks

    suspend fun getFeedbacks() {

        try {
            feedbackRepo.getFeedbacks("https://flashcall.vercel.app/api/v1/feedback/call/getFeedbacks?creatorId=6687f5eeb51cc5626f5db5ea").collect {


                if (it != null) {
                    _feedbacks.value = it
                    Log.d("FeedBackViewModel", "getFeedbacks: $it")
                } else {
                    Log.d("FeedBackViewModel", "getFeedbacks: Feedback Error")
                }
            }

        } catch (e: Exception){
            Log.d("FeedBackViewModel", "getFeedbacks: ${e.message}")
        }
    }

}