package com.example.myapplication.myapplication.flashcall.ViewModel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.myapplication.myapplication.flashcall.Data.model.APIResponse
import com.example.myapplication.myapplication.flashcall.Data.model.CreateUserResponse
import com.example.myapplication.myapplication.flashcall.Data.model.LinkData
import com.example.myapplication.myapplication.flashcall.Data.model.UpdateUserResponse
import com.example.myapplication.myapplication.flashcall.Data.model.deleteAdditionalLink.DeleteAdditionalLinks
import com.example.myapplication.myapplication.flashcall.Data.model.editAdditionalLink.EditAdditionalLinkRequest
import com.example.myapplication.myapplication.flashcall.Data.model.editAdditionalLink.EditUserLink
import com.example.myapplication.myapplication.flashcall.Data.model.firestore.UserServicesResponse
import com.example.myapplication.myapplication.flashcall.Data.model.spacialization.Profession
import com.example.myapplication.myapplication.flashcall.Data.model.spacialization.SpacializationResponse
import com.example.myapplication.myapplication.flashcall.Data.model.wallet.UserId
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
import java.net.MalformedURLException
import java.net.URL
import java.time.LocalDate
import javax.inject.Inject
import kotlin.math.roundToInt

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

    private val _updateUserState = MutableStateFlow<APIResponse<UpdateUserResponse>>(APIResponse.Empty)
    val updateUserState: StateFlow<APIResponse<UpdateUserResponse>> = _updateUserState


    private val _addProfessionState = MutableStateFlow<APIResponse<UpdateUserResponse>>(APIResponse.Empty)
    val addProfessionState: StateFlow<APIResponse<UpdateUserResponse>> = _addProfessionState

    private val _serviceState =
        MutableStateFlow<APIResponse<UserServicesResponse>>(APIResponse.Loading)
    val serviceState: StateFlow<APIResponse<UserServicesResponse>> = _serviceState

    var addAditionalLinkState by mutableStateOf(AddAditionalLinkState())
        private set


    var todaysWalletBalanceState by mutableStateOf(TodaysWalletBalanceState())
        private set

    var editAdditionalLinkState by mutableStateOf(EditAdditionalLink())
        private set

    val draggableList = listOf(LinkData("","",true)).toMutableStateList()


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
        kyc_status: String?
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
                        videoRate ?: "25",               // Default video rate
                        audioRate ?: "25",               // Default audio rate
                        chatRate ?: "25",                 // Default chat rate
                        gender ?: "Male",                // Default gender
                        dob ?: "01/01/1900",             // Default date of birth
                        bio ?: "This is the bio of my profile", // Default bio
                        kyc_status ?: "InComplete"                 // Default KYC status (if available)
                    ).collect { response ->
                        Log.d("User", "User created successfully")
                        addDataIntoFirestore(response._id,videoRate , audioRate , chatRate)
                        // Get FCM token
                        val fcmToken = Firebase.messaging.token.await()
                        // Create FCM document in Firestore
                        val fcmData = hashMapOf(
                            "token" to fcmToken
                        )
                        firestore.collection("FCMtoken").document(response.phone).set(fcmData)
                            .addOnSuccessListener {
                                Log.d("Firestore", "FCMtoken document created successfully")
                            }.addOnFailureListener { e ->
                                Log.e(
                                    "FirestoreError", "Failed to create FCM document: ${e.message}"
                                )
                            }
//                        storeResponseInPreferences(response)
                        userPreferencesRepository.storeResponseInPreferences(response)
                        _createUserState.value = APIResponse.Success(response)
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
                            Log.w("FirestoreServices", "Error updating document", e)
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
                }
            } catch (e: Exception) {
                Log.e("error", "User update failed: ${e.message}")
                loading(false)
                _createUserState.value = APIResponse.Error(e.message ?: "User update error")
            }
        }
    }

    fun getTodaysWalletBalance() {
        viewModelScope.launch {
            todaysWalletBalanceState = todaysWalletBalanceState.copy(todaysBalance = userPreferencesRepository.getTodaysWalletBalance())
            val userId = userPreferencesRepository.getUser()?._id
            val todaysDate = LocalDate.now()
            try {
                repository.getTodaysWalletBalance(
                    url = "api/v1/transaction/getTodaysEarnings?userId=$userId&date=$todaysDate"
                ).collect { response ->
                    var updatedBalance = 0
                    if(response.transactions != null){
                        for(item in response.transactions!!){
                            if(item?.type.equals("credit") && item?.amount != null && item.amount!! > 0){
                                updatedBalance += item.amount!!.roundToInt()
                            }
                        }
                    }
                    Log.d("WalletBalance", "resp: $userId, $todaysDate, $updatedBalance $response")
                    userPreferencesRepository.saveTodaysWalletBalance(updatedBalance)
                    todaysWalletBalanceState = todaysWalletBalanceState.copy(isLoading = false, todaysBalance = userPreferencesRepository.getTodaysWalletBalance())
                }
            } catch (e: Exception) {
                Log.e("error", "User update failed: ${e.message}")
                _createUserState.value = APIResponse.Error(e.message ?: "User update error")
            }
        }
    }


    fun getAllServiceData() {
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


    fun updateUserLinks(link: LinkData?) {
        val userId = userPreferencesRepository.getUser()?._id?:""
        viewModelScope.launch {
            addAditionalLinkState = addAditionalLinkState.copy(isLoading = true)
            try {
                repository.updateUser(
                    "api/v1/creator/updateUser", // Replace with actual update endpoint
                    userId = userId,
                    link = link,
                ).collect { response ->
                    if(response.updatedUser.links != null){
                        userPreferencesRepository.storeAdditionalLinks(userId, response.updatedUser.links)
                    }else{
                        userPreferencesRepository.storeAdditionalLinks(userId, null)
                    }
                    addAditionalLinkState = addAditionalLinkState.copy(showAddLinkLayout = false,linksList = userPreferencesRepository.retrieveAdditionalLinks(userId), isLoading = false)
                    makeList(userPreferencesRepository.retrieveAdditionalLinks(userId))
                }
            } catch (e: Exception) {

            }
        }
    }

    fun isActiveAdditionalLink(index: Int){
        val userId = userPreferencesRepository.getUser()?._id?:""
        addAditionalLinkState.linksList?.get(index)?.isActive = !addAditionalLinkState.linksList?.get(index)?.isActive!!

        addAditionalLinkState = addAditionalLinkState.copy(linksList = addAditionalLinkState.linksList)

        Log.d("isActiveAdditionalLink", "val: ${addAditionalLinkState.linksList?.get(index)?.isActive!!}")

        val body = EditAdditionalLinkRequest(userId = userId, user = EditUserLink(links = addAditionalLinkState.linksList))
        viewModelScope.launch {
            editAdditionalLinkState = editAdditionalLinkState.copy(isLoading = true)
            try {
                repository.editAdditionalLink(
                    "api/v1/creator/updateUser",
                    body
                ).collect { response ->
                    if(response.updatedUser.links != null){
                        userPreferencesRepository.storeAdditionalLinks(userId, response.updatedUser.links)
                    }else{
                        userPreferencesRepository.storeAdditionalLinks(userId, null)
                    }
                    editAdditionalLinkState = editAdditionalLinkState.copy(isLoading = false ,editingLayout = ShowEditingLayout(showEditingLayout = false, -1))
                    addAditionalLinkState = addAditionalLinkState.copy(showAddLinkLayout = false,linksList = userPreferencesRepository.retrieveAdditionalLinks(userId), isLoading = false)
                    makeList(userPreferencesRepository.retrieveAdditionalLinks(userId))
                }
            } catch (e: Exception) {

            }
        }
    }


    fun editUserLinks(link: LinkData?) {
        val userId = userPreferencesRepository.getUser()?._id?:""

        addAditionalLinkState.linksList?.get(editAdditionalLinkState.editingLayout.index)?.url = link?.url
        addAditionalLinkState.linksList?.get(editAdditionalLinkState.editingLayout.index)?.title = link?.title
        addAditionalLinkState.linksList?.get(editAdditionalLinkState.editingLayout.index)?.isActive = link?.isActive
        val body = EditAdditionalLinkRequest(userId = userId, user = EditUserLink(links = addAditionalLinkState.linksList))

        viewModelScope.launch {
            editAdditionalLinkState = editAdditionalLinkState.copy(isLoading = true)
            try {
                repository.editAdditionalLink(
                    "api/v1/creator/updateUser",
                    body
                ).collect { response ->
                    if(response.updatedUser.links != null){
                        userPreferencesRepository.storeAdditionalLinks(userId, response.updatedUser.links)
                    }else{
                        userPreferencesRepository.storeAdditionalLinks(userId, null)
                    }
                    editAdditionalLinkState = editAdditionalLinkState.copy(isLoading = false ,editingLayout = ShowEditingLayout(showEditingLayout = false, -1))
                    addAditionalLinkState = addAditionalLinkState.copy(showAddLinkLayout = false,linksList = userPreferencesRepository.retrieveAdditionalLinks(userId), isLoading = false)
                    makeList(userPreferencesRepository.retrieveAdditionalLinks(userId))
                }
            } catch (e: Exception) {

            }
        }
    }

    fun isValidUrl(url: String): Boolean {
        return try {
            URL(url)
            true
        } catch (e: MalformedURLException) {
            false
        }
    }

    private fun makeList(linksList: SnapshotStateList<LinkData>?) {
        draggableList.clear()
        if (linksList != null) {
            for (item in linksList){
                draggableList.add(item)
            }
        }
    }

    fun linksPositionReordered(){
        var userId = userPreferencesRepository.getUser()?._id+""
        userPreferencesRepository.storeAdditionalLinks(userId, draggableList)
        addAditionalLinkState = addAditionalLinkState.copy(linksList = userPreferencesRepository.retrieveAdditionalLinks(userId))

        val body = EditAdditionalLinkRequest(userId = userId, user = EditUserLink(links = addAditionalLinkState.linksList))

        viewModelScope.launch {
            try {
                repository.editAdditionalLink(
                    "api/v1/creator/updateUser",
                    body
                ).collect { response ->
                    if(response.updatedUser.links != null){
                        userPreferencesRepository.storeAdditionalLinks(userId, response.updatedUser.links)
                    }else{
                        userPreferencesRepository.storeAdditionalLinks(userId, null)
                    }
                    addAditionalLinkState = addAditionalLinkState.copy(showAddLinkLayout = false,linksList = userPreferencesRepository.retrieveAdditionalLinks(userId), isLoading = false)
                    makeList(userPreferencesRepository.retrieveAdditionalLinks(userId))
                }
            } catch (e: Exception) {

            }
        }
    }


    fun getAddedAdditionalLinks(){
        viewModelScope.launch {
            val userId = userPreferencesRepository.getUser()?._id?:""
            addAditionalLinkState = addAditionalLinkState.copy(linksList = userPreferencesRepository.retrieveAdditionalLinks(userId))
            makeList(addAditionalLinkState.linksList)
            viewModelScope.launch {
                try {
                    repository.getUserAdditionalLinks(
                        "api/v1/creator/getUserById", // Replace with actual update endpoint
                        userId = UserId(userId)
                    ).collect { response ->
                        userPreferencesRepository.storeAdditionalLinks(userId, response.links)
                        makeList(userPreferencesRepository.retrieveAdditionalLinks(userId))
                        addAditionalLinkState = addAditionalLinkState.copy(linksList = userPreferencesRepository.retrieveAdditionalLinks(userId))
                    }
                } catch (e: Exception) {

                }
            }
        }
    }


    fun showLayoutForAddLinks(isShow: Boolean){
        addAditionalLinkState = addAditionalLinkState.copy(showAddLinkLayout = isShow)
    }

    fun changeUserStatus(isOnline: Boolean){
        var phoneNumber = userPreferencesRepository.getUser()?.phone
        Log.d("UpdateServiceStatus","CHange: $isOnline, ${phoneNumber}")

        val updateMap = mutableMapOf<String, Any>()

        if(isOnline){
            updateMap["status"] = "Online"
        }else{
            updateMap["status"] = "Offline"
        }

        // Perform the update if there are fields to update
        if (updateMap.isNotEmpty() && phoneNumber != null) {
            val documentRef = firestore.collection("userStatus").document(phoneNumber)

            // Check if the document exists first
            documentRef.get().addOnSuccessListener { document ->

                if (document.exists()) {
                    // If the document exists, use update
                    documentRef.update(updateMap)
                        .addOnSuccessListener {
                            Log.w("Firestore", "Status Chnaged Successfully")
                        }
                        .addOnFailureListener { e ->
                            Log.w("Firestore", "Error Status Not Changed", e)
                        }
                } else {
                    // If the document doesn't exist, use set to create it

                    documentRef.set(updateMap)
                        .addOnSuccessListener {
                            Log.w("Firestore", "Status Created Successfully")
                        }
                        .addOnFailureListener { e ->
                            Log.w("Firestore", "Error creating status: ", e)
                        }
                }
            }.addOnFailureListener { e ->
                Log.w("Firestore", "Error checking document", e)
            }
        }else{
            Log.w("Firestore", "Error data missing")
        }
    }


    fun deleteAdditionalLinks(item: LinkData){
        viewModelScope.launch {
            val userId = userPreferencesRepository.getUser()?._id?:""
            Log.d("DeletingAdditionalLink","requestBody: $userId")
            viewModelScope.launch {
                try {
                    repository.daleteAdditionalLink(body = DeleteAdditionalLinks(userId = userId, link = LinkData(title = item.title, url = item.url, isActive = item.isActive)))
                    .collect { response ->
                        Log.d("DeletingAdditionalLink","responseBody: $response")
                        userPreferencesRepository.storeAdditionalLinks(userId, response.links)
                        makeList(userPreferencesRepository.retrieveAdditionalLinks(userId))
                        addAditionalLinkState = addAditionalLinkState.copy(linksList = userPreferencesRepository.retrieveAdditionalLinks(userId))
                    }
                } catch (e: Exception) {
                    Log.d("DeletingAdditionalLink","error: ${e.message}")
                }
            }
        }
    }


    fun showEditingAdditionalLayout(isShow: Boolean, index: Int){
        if(isShow && index > -1){
            editAdditionalLinkState = editAdditionalLinkState.copy(editingLayout = ShowEditingLayout(showEditingLayout = isShow, index = index))
        }else{
            editAdditionalLinkState = editAdditionalLinkState.copy(editingLayout = ShowEditingLayout(showEditingLayout = isShow, index = -1))
        }
    }

    var shareLinkState by mutableStateOf(ShareLinkState())

    fun getShareLink(){
        shareLinkState = shareLinkState.copy(shareLink = userPreferencesRepository.getShareLink())
        val userId = userPreferencesRepository.getUser()?._id
        viewModelScope.launch {
            repository.getShareLink(url = "api/v1/creator/creatorLink?userId=$userId").collect{ response ->
                if(response.creatorLink != null){
                    userPreferencesRepository.saveShareLink(response.creatorLink!!)
                    shareLinkState = shareLinkState.copy(shareLink = userPreferencesRepository.getShareLink())
                }
            }

        }
    }

    fun getIsKycRequired(): Boolean{
        return if(userPreferencesRepository.isKyc()){
            false
        }else{
            true
        }
    }

    fun getIsPaymentDetails(): Boolean{
        return if(userPreferencesRepository.getPaymentSettings().isPayment){
            false
        }else{
            true
        }
    }

    var userAssistanceLinkState by mutableStateOf(UserAssistanceLinkState())
    fun getUserAssistanceLink(){
        userAssistanceLinkState = userAssistanceLinkState.copy(linkUrl = userPreferencesRepository.getUserAssistanceLink(), linkDesc = userPreferencesRepository.getUserAssistanceLinkDesc())
        viewModelScope.launch {
            repository.getUserAssistanceLink("https://backend.flashcall.me/api/v1/others/getStaticLink").collect{ response ->
                if(response.link != null && response.description!= null){
                    userPreferencesRepository.saveUserAssistanceLink(response.link!!)
                    userPreferencesRepository.saveUserAssistanceLinkDesc(response.description!!)
                    userAssistanceLinkState = userAssistanceLinkState.copy(linkUrl = userPreferencesRepository.getUserAssistanceLink(), linkDesc = userPreferencesRepository.getUserAssistanceLinkDesc())
                }
                Log.d("UserAssistaceLink","response: $response")
            }
        }
    }

    fun getMyBio(): String{
        return userPreferencesRepository.getStoredUserData(PreferencesKey.Bio.key)+""
    }

    private val _spacializationState = MutableStateFlow<APIResponse<SpacializationResponse>>(APIResponse.Empty)
    val spacializationState: StateFlow<APIResponse<SpacializationResponse>> = _spacializationState
    fun getSpacialization(){
        _spacializationState.value = APIResponse.Loading
        try {
            viewModelScope.launch {
                repository.getSpacializations(url = "https://backend.flashcall.me/api/v1/profession/selectProfession").collect{response ->
                    if (response != null){
                        _spacializationState.value = APIResponse.Success(response)
                    }else{
                        _spacializationState.value = APIResponse.Error("data not found")
                    }
                }
            }
        }catch (e: Exception){
            _spacializationState.value = APIResponse.Error("error: ${e.message}")
        }
    }

    fun addSpacialization(profession: String) {
        val userId = userPreferencesRepository.getUser()?._id+""
        viewModelScope.launch {
            _addProfessionState.value = APIResponse.Loading
            try {
                repository.updateUser(
                    url = "api/v1/creator/updateUser",
                    userId = userId,
                    profession = profession
                ).collect { response ->
                    _addProfessionState.value = APIResponse.Success(response)
                    userPreferencesRepository.storeUpdateUserResponseInPreferences(response)
                }
            } catch (e: Exception) {
                Log.e("error", "User update failed: ${e.message}")
                _addProfessionState.value = APIResponse.Error(e.message ?: "User update error")
            }
        }
    }
}

data class SpacializationState(
    var isLoading: Boolean = false,
    var error: String? = null,
    var success: Boolean = false,
    var list: List<Profession?>? = null
)

data class UserAssistanceLinkState(
    var linkUrl: String? = null,
    var linkDesc: String? = null
)

data class ShareLinkState(
    var shareLink: String = "",
    var isLoading: Boolean = false
)

data class EditAdditionalLink(
    val isLoading: Boolean = false,
    val cancelEdition: Boolean = false,
    val editingLayout: ShowEditingLayout = ShowEditingLayout()
)

data class ShowEditingLayout(
    val showEditingLayout: Boolean = false,
    val index: Int = -1
)
data class AddAditionalLinkState(
    val showAddLinkLayout: Boolean = false,
    val linksList: SnapshotStateList<LinkData>? = null,
    val isLoading: Boolean = false
)
data class TodaysWalletBalanceState(
    val isLoading: Boolean = false,
    val todaysBalance: Int = 0,
)


