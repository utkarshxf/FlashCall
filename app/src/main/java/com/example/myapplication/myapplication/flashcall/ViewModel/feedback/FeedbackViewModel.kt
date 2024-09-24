package com.example.myapplication.myapplication.flashcall.ViewModel.feedback

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.myapplication.flashcall.Data.model.APIResponse
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.ValidateResponse
//import com.example.myapplication.myapplication.flashcall.Data.model.feedback.Feedback
//import com.example.myapplication.myapplication.flashcall.Data.model.feedback.FeedbackResponse
import com.example.myapplication.myapplication.flashcall.Data.model.feedback.UpdateFeedback
import com.example.myapplication.myapplication.flashcall.Data.model.feedback.UpdateFeedbackResponse
import com.example.myapplication.myapplication.flashcall.Data.model.userFeedbacks.Feedback
import com.example.myapplication.myapplication.flashcall.Data.model.userFeedbacks.UserFeedbaks
import com.example.myapplication.myapplication.flashcall.repository.FeedbackRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val feedbackRepo: FeedbackRepo
) : ViewModel() {

//    private val _feedbacks = MutableStateFlow<UserFeedbaks>
//    val feedbacks: StateFlow<List<Feedback>> = _feedbacks

    data class UserFeedbackState(
        var list: UserFeedbaks? = null,
        var isLoading: Boolean = false,
        var error: String? = null
    )

    var userFeedbackState by mutableStateOf(UserFeedbackState())
        private set

    private val _updateFeedback = MutableStateFlow<UpdateFeedbackResponse?>(null)
    val updateFeedback: StateFlow<UpdateFeedbackResponse?> = _updateFeedback

    fun getFeedbacks(uid: String) {
        userFeedbackState = userFeedbackState.copy(isLoading = true)
        viewModelScope.launch {
            feedbackRepo.getFeedbacks("https://flashcall.vercel.app/api/v1/feedback/call/getFeedbacks?creatorId=$uid")
                .collect {response ->
                Log.d("FeedBackViewModel", "getFeedbacks: ${response.feedbacks}")
                userFeedbackState = userFeedbackState.copy(isLoading = false, list = response, error = null)
                //_feedbacks.value = it.feedbacks

            }
        }

        try {


        } catch (e: Exception){
            Log.d("FeedBackViewModel", "getFeedbacks: ${e.message}")
        }
    }

     fun updateFeedback(updateFeedback: UpdateFeedback){
        viewModelScope.launch {
            try {
                feedbackRepo.updateFeedback(
                    "https://flashcall.me/api/v1/feedback/creator/setFeedback",
                    updateFeedback
                ).collect {
                    _updateFeedback.value = it
                    Log.d("updateFeedback", "getFeedbacks: ${it}")
                }
            } catch (e: Exception) {
                Log.d("updateFeedbackError", "getFeedbacks: ${e.message}")
            }
        }
    }

}