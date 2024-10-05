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
import com.example.myapplication.myapplication.flashcall.Data.model.userFeedbacks.FeedbackResponseItem
import com.example.myapplication.myapplication.flashcall.repository.FeedbackRepo
import com.example.myapplication.myapplication.flashcall.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val feedbackRepo: FeedbackRepo,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    data class UserFeedbackState(
        var list: List<FeedbackResponseItem>? = null,
        var isLoading: Boolean = false,
        var error: String? = null
    )

    var userFeedbackState by mutableStateOf(UserFeedbackState())
        private set



    val originalFeedbackList = listOf(FeedbackResponseItem(null, null,null,null)).toMutableStateList()

    //66fd37a1735a8e07837d5d99
    fun getFeedbacks(uid: String) {
        makeDragList(userPreferencesRepository.getUserFeedbacks(uid))
        userFeedbackState = userFeedbackState.copy(list = userPreferencesRepository.getUserFeedbacks(uid))
        try {
            viewModelScope.launch {
                feedbackRepo.getFeedbacks("https://backend.flashcall.me/api/v1/feedback/call/getFeedbacks?creatorId=$uid")
                    .collect {response ->
                        Log.d("FeedBackViewModel", "getFeedbacks: ${response}")
                        makeDragList(response)
                        userPreferencesRepository.saveUserFeedbacks(uid, response)
                        userFeedbackState = userFeedbackState.copy(isLoading = false, list = response, error = null)

                    }
            }
        } catch (e: Exception){
            Log.d("FeedBackViewModel", "getFeedbacks: ${e.message}")
        }
    }

    private fun makeDragList(model: List<FeedbackResponseItem>?){
        originalFeedbackList.clear()
        if(!model.isNullOrEmpty()){
            for (item in model){
                originalFeedbackList.add(item)
            }
        }
        Log.d("originalListSize","size: ${originalFeedbackList.size}")
    }

     fun updateFeedback(updateFeedback: UpdateFeedback){
         Log.d("updateFeedback", "requestBody: ${updateFeedback}")
        viewModelScope.launch {
            try {
                feedbackRepo.updateFeedback(
                    "https://backend.flashcall.me/api/v1/feedback/creator/setFeedback",
                    updateFeedback
                ).collect {
                    Log.d("updateFeedback", "getFeedbacks: ${it}")
                }
            } catch (e: Exception) {
                Log.d("updateFeedbackError", "getFeedbacks: ${e.message}")
            }
        }
    }

}