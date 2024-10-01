package com.example.myapplication.myapplication.flashcall.ViewModel.feedback

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.myapplication.flashcall.Data.model.APIResponse
import com.example.myapplication.myapplication.flashcall.Data.model.chatDataModel.ValidateResponse
//import com.example.myapplication.myapplication.flashcall.Data.model.feedback.Feedback
//import com.example.myapplication.myapplication.flashcall.Data.model.feedback.FeedbackResponse
import com.example.myapplication.myapplication.flashcall.Data.model.feedback.UpdateFeedback
import com.example.myapplication.myapplication.flashcall.Data.model.feedback.UpdateFeedbackResponse
import com.example.myapplication.myapplication.flashcall.Data.model.userFeedbacks.Feedback
import com.example.myapplication.myapplication.flashcall.Data.model.userFeedbacks.FeedbackX
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


    val originalFeedbackList = listOf(Feedback(null,null,null,null)).toMutableStateList()

    //66ab32f4321d76e89d77464b
    fun getFeedbacks(uid: String) {
        userFeedbackState = userFeedbackState.copy(isLoading = true)
        try {
            viewModelScope.launch {
                feedbackRepo.getFeedbacks("https://flashcall.vercel.app/api/v1/feedback/call/getFeedbacks?creatorId=$uid")
                    .collect {response ->
                        Log.d("FeedBackViewModel", "getFeedbacks: ${response.feedbacks}")
                        makeDragList(response)
                        userFeedbackState = userFeedbackState.copy(isLoading = false, list = response, error = null)

                    }
            }
        } catch (e: Exception){
            Log.d("FeedBackViewModel", "getFeedbacks: ${e.message}")
        }
    }

    fun makeDragList(model: UserFeedbaks?){
        originalFeedbackList.clear()
        if(!model?.feedbacks.isNullOrEmpty()){
            for (item in model?.feedbacks!!){
                originalFeedbackList.add(item)
            }
        }
        Log.d("originalListSize","size: ${originalFeedbackList.size}")
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