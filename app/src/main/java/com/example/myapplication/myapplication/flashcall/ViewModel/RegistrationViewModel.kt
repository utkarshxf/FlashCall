package com.example.myapplication.myapplication.flashcall.ViewModel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.Data.model.APIResponse
import com.example.myapplication.myapplication.flashcall.Data.model.CreateUser
import com.example.myapplication.myapplication.flashcall.Data.model.CreateUserResponse
import com.example.myapplication.myapplication.flashcall.Data.model.UpdateUserResponse
import com.example.myapplication.myapplication.flashcall.Data.model.VerifyOTPResponse
import com.example.myapplication.myapplication.flashcall.repository.CreateRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.messaging
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val repository: CreateRepository,
    private val firestore: FirebaseFirestore,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_prefs1", Context.MODE_PRIVATE)

    private val _createUserState =
        MutableStateFlow<APIResponse<CreateUserResponse>>(APIResponse.Empty)

    val createUserState: StateFlow<APIResponse<CreateUserResponse>> = _createUserState

    private val _updateUserState =
        MutableStateFlow<APIResponse<UpdateUserResponse>>(APIResponse.Empty)

    val updateUserState: StateFlow<APIResponse<UpdateUserResponse>> = _updateUserState

    fun createUser(
        username: String?,
        phone: String?,
        fullName: String?,
        firstName: String?,
        lastName: String?,
        photo: String?,
        profession: String?,
        themeSelected: String?,
        videoRate: String?,
        audioRate: String?,
        chatRate: String?,
        gender: String?,
        dob: String?,
        bio: String?,
        kyc_status: String?,
        navController: NavController
    ) {
        viewModelScope.launch {
            _createUserState.value = APIResponse.Loading
            try {
                // Check if username is available before proceeding
                val isUsernameAvailable = checkUsernameAvailability(username ?: "")
                if (isUsernameAvailable) {
                    // Proceed with user creation in the repository
                    repository.createUser(
                        "https://flashcall.vercel.app/api/v1/creator/createUser",
                        username ?: "", // Default value to avoid null
                        phone ?: "",    // Ensure non-null values are sent to the API
                        fullName ?: "",
                        firstName ?: "",
                        lastName ?: "",
                        photo ?: "",
                        profession ?: "",
                        themeSelected ?: "",
                        videoRate ?: "",
                        audioRate ?: "",
                        chatRate ?: "",
                        gender ?: "",
                        dob ?: "",
                        bio ?: "",
                        kyc_status ?: ""
                    ).collect { response ->
                        _createUserState.value = APIResponse.Success(response)
                        Log.d("User", "User created successfully")

                        storeResponseInPreferences(response)

                        // Get FCM token
                        val fcmToken = Firebase.messaging.token.await()

                        // Create FCM document in Firestore
                        val fcmData = hashMapOf(
                            "username" to response.username,
                            "_id" to response._id,
                            "FCMtoken" to fcmToken
                        )

                        firestore.collection("FCMtoken").document(response._id).set(fcmData)
                            .addOnSuccessListener {
                                Log.d("Firestore", "FCMtoken document created successfully")
                            }.addOnFailureListener { e ->
                                Log.e(
                                    "FirestoreError",
                                    "Failed to create FCM document: ${e.message}"
                                )
                            }

                        navController.navigate(ScreenRoutes.SelectSpeciality.route)
                    }
                } else {
                    // Username is taken, show error message
                    _createUserState.value = APIResponse.Error("Username is already taken")
                }
            } catch (e: Exception) {
                Log.e("error", "User creation failed: ${e.message}")
                _createUserState.value = APIResponse.Error(e.message ?: "User creation error")
            }
        }
    }

    private suspend fun checkUsernameAvailability(username: String): Boolean {
        return try {
            val response = repository.checkUsernameAvailability(username)
            response.message != "Username is already taken"
        } catch (e: Exception) {
            Log.e("UsernameCheckError", "Failed to check username: ${e.message}")
            false
        }
    }

    fun updateUser(
        userId: String,
        username: String?,
        phone: String?,
        fullName: String?,
        firstName: String?,
        lastName: String?,
        photo: String?,
        profession: String?,
        themeSelected: String?,
        videoRate: String?,
        audioRate: String?,
        chatRate: String?,
        gender: String?,
        dob: String?,
        bio: String?,
        kyc_status: String?,
        navController: NavController
    ) {
        viewModelScope.launch {
            _updateUserState.value = APIResponse.Loading
            try {
                // Proceed with user update in the repository
                Log.e("userIdUpdate", "$userId")
                Log.e("firstname2", "$firstName")
                repository.updateUser(
                    "https://flashcall.vercel.app/api/v1/creator/updateUser", // Replace with actual update endpoint
                    userId,
                    fullName ?: "",
                    firstName ?: "",
                    lastName ?: "",
                    username ?: "",
                    phone ?: "",
                    photo ?: "",
                    profession ?: "",
                    themeSelected ?: "",
                    videoRate ?: "",
                    audioRate ?: "",
                    chatRate ?: "",
                    gender ?: "",
                    dob ?: "",
                    bio ?: ""
                ).collect { response ->
                    _updateUserState.value = APIResponse.Success(response)
                    Log.d("User", "User updated successfully")

                    // Update the SharedPreferences with the new user data
                    storeUpdateUserResponseInPreferences(response)

//                    navController.navigate(ScreenRoutes.Profile.route) // Navigate to the desired screen after update
                }

            } catch (e: Exception) {
                Log.e("error", "User update failed: ${e.message}")
                _createUserState.value = APIResponse.Error(e.message ?: "User update error")
            }
        }
    }

    private fun storeUpdateUserResponseInPreferences(response: UpdateUserResponse) {
        sharedPreferences.edit().apply {
            putString("username", response.updatedUser.username)
            putString("phone", response.updatedUser.phone)
            putString("fullName", response.updatedUser.fullName)
            putString("profession", response.updatedUser.profession)
            putString("themeSelected", response.updatedUser.themeSelected)
            putString("photo", response.updatedUser.photo)
            putString("phone", response.updatedUser.phone)
            putString("videoRate", response.updatedUser.videoRate)
            putString("audioRate", response.updatedUser.audioRate)
            putString("chatRate", response.updatedUser.chatRate)
            putString("gender", response.updatedUser.gender)
            putString("dob", response.updatedUser.dob)
            putString("bio", response.updatedUser.bio)

            // other fields
            apply()
        }
    }


    private fun storeResponseInPreferences(response: CreateUserResponse) {
        sharedPreferences.edit().apply {
            putString("username", response.username)
            putString("phone", response.phone)
            putString("fullName", response.fullName)
            putString("firstName", response.firstName)
            putString("lastName", response.lastName)
            putString("photo", response.photo)
            putString("profession", response.profession)
            putString("themeSelected", response.themeSelected)
            putString("videoRate", response.videoRate)
            putString("audioRate", response.audioRate)
            putString("chatRate", response.chatRate)
            putString("gender", response.gender)
            putString("dob", response.dob)
            putString("bio", response.bio)
            putString("_id", response._id)
            putString("createdAt", response.createdAt)
            putString("updatedAt", response.updatedAt)
            putString("__v", response.__v)
            apply()
        }
    }

    fun getStoredUserData(key: String): String? {
        return sharedPreferences.getString(key, null)
    }
}

