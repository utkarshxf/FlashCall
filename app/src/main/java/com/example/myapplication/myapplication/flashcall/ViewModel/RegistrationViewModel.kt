package com.example.myapplication.myapplication.flashcall.ViewModel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.myapplication.myapplication.flashcall.Data.ScreenRoutes
import com.example.myapplication.myapplication.flashcall.Data.model.APIResponse
import com.example.myapplication.myapplication.flashcall.Data.model.CreateUserResponse
import com.example.myapplication.myapplication.flashcall.Data.model.UpdateUserResponse
import com.example.myapplication.myapplication.flashcall.Data.model.firestore.UserServicesResponse
import com.example.myapplication.myapplication.flashcall.repository.CreateRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.messaging.messaging
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
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

    private val _serviceState =
        MutableStateFlow<APIResponse<UserServicesResponse>>(APIResponse.Loading)
    val serviceState: StateFlow<APIResponse<UserServicesResponse>> = _serviceState

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
                                    "FirestoreError", "Failed to create FCM document: ${e.message}"
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

    fun updateServices(
        userId: String,
        masterToggle: Boolean?=null,
        servicesVideo: Boolean?=null,
        servicesAudio: Boolean?=null,
        servicesChat: Boolean?=null,
        videoRate: String?=null,
        audioRate: String?=null,
        chatRate: String?=null,
    ) {
        val updateMap = mutableMapOf<String, Any>()

        // Only add non-null values to the update map
        audioRate?.let { updateMap["prices.audioCall"] = it }
        chatRate?.let { updateMap["prices.chat"] = it }
        videoRate?.let { updateMap["prices.videoCall"] = it }

        // For services, you might want to update only if a specific condition is met
        // or if you have boolean flags for these. For now, I'll keep them as they were
        servicesAudio?.let { updateMap["services.audioCall"] = it }
        servicesChat?.let { updateMap["services.chat"] = it }
        masterToggle?.let { updateMap["services.myServices"] = it }
        servicesVideo?.let { updateMap["services.videoCall"] = it }

        // Only perform the update if there are fields to update
        if (updateMap.isNotEmpty()) {
            firestore.collection("services").document(userId).update(updateMap)
                .addOnSuccessListener {
                    viewModelScope.launch {
                        _updateUserState.value = APIResponse.Loading
                        try {
                            repository.updateUser(
                                "https://flashcall.vercel.app/api/v1/creator/updateUser", // Replace with actual update endpoint
                                userId = userId,
                                videoRate = videoRate,
                                audioRate = audioRate,
                                chatRate = chatRate,
                            ).collect { response ->
                                _updateUserState.value = APIResponse.Success(response)
                                storeUpdateUserResponseInPreferences(response)
                            }
                        } catch (e: Exception) {
                            Log.e("error", "User update failed: ${e.message}")
                            _createUserState.value =
                                APIResponse.Error(e.message ?: "User update error")
                        }
                    }
                }.addOnFailureListener { e ->
                    Log.w("Firestore", "Error updating document", e)
                }
        }
    }

    fun updateUser(
        userId: String,
        username: String?=null,
        phone: String?=null,
        fullName: String?=null,
        firstName: String?=null,
        lastName: String?=null,
        photo: String?=null,
        profession: String?=null,
        themeSelected: String?=null,
        gender: String?=null,
        dob: String?=null,
        bio: String?=null,
        navController: NavController
    ) {
        viewModelScope.launch {
            _updateUserState.value = APIResponse.Loading
            try {
                repository.updateUser(
                    url = "https://flashcall.vercel.app/api/v1/creator/updateUser", // Replace with actual update endpoint
                    userId = userId,
                    fullName = fullName,
                    firstName = firstName,
                    lastName = lastName,
                    photo = photo,
                    profession = profession,
                    themeSelected = themeSelected,
                    gender = gender,
                    dob = dob,
                    bio = bio,
                    phone = phone,
                    username = username
                ).collect { response ->
                    _updateUserState.value = APIResponse.Success(response)
                    storeUpdateUserResponseInPreferences(response)
                }
            } catch (e: Exception) {
                Log.e("error", "User update failed: ${e.message}")
                _createUserState.value = APIResponse.Error(e.message ?: "User update error")
            }
        }
    }

    fun getAllServiceData(userId: String) {
        viewModelScope.launch {
            _serviceState.value = APIResponse.Loading // Set loading state

            try {
                val getUserID = getStoredUserData("user_id")
                val firestore = FirebaseFirestore.getInstance()
                val documentSnapshot = withContext(Dispatchers.IO) {
                    firestore.collection("services").document(getUserID!!).get().await()
                }
                Log.v("service data" , "Document exists ${documentSnapshot}")
                if (documentSnapshot.exists()) {
                    val serviceData = documentSnapshot.toObject<UserServicesResponse>()
                    Log.v("service data" , "Document exists $serviceData")
                    _serviceState.value = APIResponse.Success(serviceData ?: UserServicesResponse())
                } else {
                    _serviceState.value = APIResponse.Error("Document does not exist")
                    Log.v("Service Data" , "Document does not exist")
                }

            } catch (e: Exception) {
                e.printStackTrace()
                _serviceState.value = APIResponse.Error("Failed to fetch data: ${e.message}")
                Log.v("service data" , "Document does not exist $e")
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

