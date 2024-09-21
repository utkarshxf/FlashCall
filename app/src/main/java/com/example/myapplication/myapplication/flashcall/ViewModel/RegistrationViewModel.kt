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
import com.example.myapplication.myapplication.flashcall.Data.model.LinkData
import com.example.myapplication.myapplication.flashcall.Data.model.UpdateUserResponse
import com.example.myapplication.myapplication.flashcall.Data.model.firestore.UserServicesResponse
import com.example.myapplication.myapplication.flashcall.repository.CreateRepository
import com.example.myapplication.myapplication.flashcall.repository.UserPreferencesRepository
import com.example.myapplication.myapplication.flashcall.utils.PreferencesKey
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
    private val userPreferencesRepository: UserPreferencesRepository,
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
        username: String,
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
                Log.v("error" , "$username")
                if (isUsernameAvailable) {
                    // Proceed with user creation in the repository
                    repository.createUser(
                        "/api/v1/creator/createUser",
                        username,           // Default value
                        phone ?: "+910000000000",          // Ensure non-null value
                        fullName ?: "fullName",             // Ensure non-null value
                        firstName ?: "firstName",            // Ensure non-null value
                        lastName ?: "lastName",             // Ensure non-null value
                        photo ?: "photo", // Default image URL
                        profession ?: "Android Developer", // Default profession
                        themeSelected ?: "#000000",      // Default theme color
                        videoRate ?: "30",               // Default video rate
                        audioRate ?: "25",               // Default audio rate
                        chatRate ?: "5",                 // Default chat rate
                        gender ?: "Male",                // Default gender
                        dob ?: "01/01/1900",             // Default date of birth
                        bio ?: "This is the bio of my profile", // Default bio
                        kyc_status ?: "InComplete"                 // Default KYC status (if available)
                    ).collect { response ->
                        _createUserState.value = APIResponse.Success(response)
                        Log.d("User", "User created successfully")
                        addDataIntoFirestore(response._id,videoRate , audioRate , chatRate)
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

//                        storeResponseInPreferences(response)
                        userPreferencesRepository.storeResponseInPreferences(response)
                        navController.navigate(ScreenRoutes.LoginDoneScreen.route){
                            popUpTo(ScreenRoutes.RegistrationScreen.route){inclusive = true}
                        }
                    }
                } else {
                    // Username is taken, show error message
                    Log.e("error", "Username is already taken")
                    _createUserState.value = APIResponse.Error("Username is already taken")
                }
            } catch (e: Exception) {
                Log.e("error", "User creation failed: ${e.message}")
                _createUserState.value = APIResponse.Error(e.message ?: "User creation error")
            }
        }
    }

    private fun addDataIntoFirestore(userId: String, videoRate: String?, audioRate: String?, chatRate: String?) {
        try {
            val servicesMap = hashMapOf(
                "prices" to hashMapOf(
                    "videoCall" to videoRate,
                    "audioCall" to audioRate,
                    "chat" to chatRate
                ),
                "services" to hashMapOf(
                    "myServices" to true,
                    "videoCall" to true,
                    "audioCall" to true,
                    "chat" to true
                )
            )

            FirebaseFirestore.getInstance().collection("services").document(userId)
                .set(servicesMap)
                .addOnSuccessListener {
                    Log.d("CreateUser", "Services document created successfully")
                }
                .addOnFailureListener { e ->
                    // Handle failure
                    Log.e("CreateUser", "Error creating services document", e)
                }
        }catch (e:Exception){
                // Handle failure
                Log.e("CreateUser", "Error creating user document", e)
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
        masterToggle: Boolean? = null,
        servicesVideo: Boolean? = null,
        servicesAudio: Boolean? = null,
        servicesChat: Boolean? = null,
        videoRate: String? = null,
        audioRate: String? = null,
        chatRate: String? = null,
    ) {
        val updateMap = mutableMapOf<String, Any>()

        // Only add non-null values to the update map
        audioRate?.let { updateMap["prices.audioCall"] = it }
        chatRate?.let { updateMap["prices.chat"] = it }
        videoRate?.let { updateMap["prices.videoCall"] = it }

        // For services, add non-null values
        servicesAudio?.let { updateMap["services.audioCall"] = it }
        servicesChat?.let { updateMap["services.chat"] = it }
        masterToggle?.let { updateMap["services.myServices"] = it }
        servicesVideo?.let { updateMap["services.videoCall"] = it }

        // Perform the update if there are fields to update
        if (updateMap.isNotEmpty()) {
            val documentRef = firestore.collection("services").document(userId)

            // Check if the document exists first
            documentRef.get().addOnSuccessListener { document ->

                if (document.exists()) {
                    // If the document exists, use update
                    documentRef.update(updateMap)
                        .addOnSuccessListener {
                            updateUserBackend(userId, videoRate, audioRate, chatRate, servicesVideo, servicesAudio, servicesChat)
                        }
                        .addOnFailureListener { e ->
                            Log.w("Firestore", "Error updating document", e)
                        }
                } else {
                    // If the document doesn't exist, use set to create it

                    documentRef.set(updateMap)
                        .addOnSuccessListener {
                            updateUserBackend(userId, videoRate, audioRate, chatRate, servicesVideo, servicesAudio, servicesChat)
                        }
                        .addOnFailureListener { e ->
                            Log.w("Firestore", "Error creating document", e)
                        }
                }
            }.addOnFailureListener { e ->
                Log.w("Firestore", "Error checking document", e)
            }
        }
    }



    private fun updateUserBackend(userId: String, videoRate: String?, audioRate: String?, chatRate: String?
                                  , videoService: Boolean?, audioService: Boolean?, chatService: Boolean?) {
        viewModelScope.launch {
            _updateUserState.value = APIResponse.Loading
            try {
                repository.updateUser(
                    "api/v1/creator/updateUser", // Replace with actual update endpoint
                    userId = userId,
                    videoRate = videoRate,
                    audioRate = audioRate,
                    chatRate = chatRate,
                    videoService = videoService,
                    audioService = audioService,
                    chatService = chatService,
                ).collect { response ->
                    _updateUserState.value = APIResponse.Success(response)
                    userPreferencesRepository.storeUpdateUserResponseInPreferences(response)
                }
            } catch (e: Exception) {
                Log.e("error", "User update failed: ${e.message}")
                _createUserState.value = APIResponse.Error(e.message ?: "User update error")
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
        link: LinkData?=null,
        navController: NavController,
        loading: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            loading(true)
            _updateUserState.value = APIResponse.Loading
            try {
                repository.updateUser(
                    url = "api/v1/creator/updateUser",
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
                    loading(false)
                    userPreferencesRepository.storeUpdateUserResponseInPreferences(response)
//                    navController.navigate(ScreenRoutes.HomeScreen.route)
                    navController.popBackStack()
                }
            } catch (e: Exception) {
                Log.e("error", "User update failed: ${e.message}")
                loading(false)
                _createUserState.value = APIResponse.Error(e.message ?: "User update error")
            }
        }
    }

    fun getAllServiceData(userId: String) {
        viewModelScope.launch {
            _serviceState.value = APIResponse.Loading // Set loading state

            try {
                val getUserID = userPreferencesRepository.getStoredUserData(PreferencesKey.UserId.key)
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

    fun getStoredUserData(preferencesKey: String): String? {
        return userPreferencesRepository.getStoredUserData(preferencesKey)
    }

    fun updateUserLinks(link: LinkData?, loading: (Boolean) -> Unit) {
        val userId = userPreferencesRepository.getUser()?._id?:""
        viewModelScope.launch {
            loading(true)
            _updateUserState.value = APIResponse.Loading
            try {
                repository.updateUser(
                    "api/v1/creator/updateUser", // Replace with actual update endpoint
                    userId = userId,
                    link = link,
                ).collect { response ->
                    loading(false)
                    _updateUserState.value = APIResponse.Success(response)
                    userPreferencesRepository.storeUpdateUserResponseInPreferences(response)
                }
            } catch (e: Exception) {
                loading(false)
                Log.e("error", "User update failed Links: ${e.message}")
                _createUserState.value = APIResponse.Error(e.message ?: "User update error")
            }
        }
    }
}

