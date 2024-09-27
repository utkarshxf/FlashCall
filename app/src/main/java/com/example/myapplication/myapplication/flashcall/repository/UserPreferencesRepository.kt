package com.example.myapplication.myapplication.flashcall.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.myapplication.myapplication.flashcall.Data.model.CreateUserResponse
import com.example.myapplication.myapplication.flashcall.Data.model.IsUserCreatedResponse
import com.example.myapplication.myapplication.flashcall.Data.model.LinkData
import com.example.myapplication.myapplication.flashcall.Data.model.UpdateUserResponse
import com.example.myapplication.myapplication.flashcall.Data.model.paymentSetting.LocalPaymentSetting
import com.example.myapplication.myapplication.flashcall.Data.model.wallet.Transaction
import com.example.myapplication.myapplication.flashcall.Data.model.wallet.TransactionGroup
import com.example.myapplication.myapplication.flashcall.Data.model.wallet.TransactionsResponse
import com.example.myapplication.myapplication.flashcall.Data.model.wallet.groupByDate
import com.example.myapplication.myapplication.flashcall.utils.PreferencesKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_prefs1", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun storeTransactions(userId: String, transactionsResponse: TransactionsResponse) {
        val jsonString = gson.toJson(transactionsResponse)
        sharedPreferences.edit().apply {
            putString("transactions_$userId", jsonString)
            apply()
        }
    }

    fun retrieveTransactions(userId: String): TransactionsResponse {
        val jsonString = sharedPreferences.getString("transactions_$userId", null)
        return if (jsonString != null) {
            gson.fromJson(jsonString, TransactionsResponse::class.java)
        } else {
            TransactionsResponse()
        }
    }


    fun storeAdditionalLinks(userId: String, additionalLinksList: MutableList<LinkData>?) {
        if(additionalLinksList != null){
            val jsonString = gson.toJson(additionalLinksList)
            sharedPreferences.edit().apply {
                putString("additional_links_$userId", jsonString)
                apply()
            }
        }else{
            sharedPreferences.edit().apply {
                putString("additional_links_$userId", "")
                apply()
            }
        }
    }

    fun retrieveAdditionalLinks(userId: String): MutableList<LinkData>? {
        val additionalLinksStr = sharedPreferences.getString("additional_links_$userId", "") ?:""
        if(additionalLinksStr.isNotEmpty()){
            return convertingStringIntoList(additionalLinksStr)
        }else{
            return null
        }
    }
    private fun convertingStringIntoList(stringList: String): MutableList<LinkData>? {
        val gson = Gson()
        val listType = object : TypeToken<MutableList<LinkData>>() {}.type
        val list: MutableList<LinkData> = gson.fromJson(stringList, listType)
        return list
    }





    fun updateTransactions(userId: String, newTransactions: List<Transaction>) {
        val existingTransactions = retrieveTransactions(userId)
        val updatedTransactions = (existingTransactions.transactions ?: emptyList()) + newTransactions
        val updatedResponse = TransactionsResponse(updatedTransactions.distinctBy { it._id })
        storeTransactions(userId, updatedResponse)
    }

    fun getTransactionGroups(userId: String): List<TransactionGroup> {
        val transactions = retrieveTransactions(userId).transactions ?: emptyList()
        return transactions.groupByDate()
    }

    private fun List<Transaction>.groupByDate(): List<TransactionGroup> {
        return groupBy {
            LocalDateTime.parse(it.createdAt, DateTimeFormatter.ISO_DATE_TIME).toLocalDate()
        }.map { (date, transactions) -> TransactionGroup(date, transactions) }
    }

    fun getStoredUserData(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun saveUser(userData: IsUserCreatedResponse) {
        sharedPreferences.edit().apply {
            putString(PreferencesKey.UserId.key, userData._id)
            putString(PreferencesKey.Username.key, userData.username)
            putString(PreferencesKey.Phone.key, userData.phone)
            putString(PreferencesKey.FullName.key, userData.fullName)
            putString(PreferencesKey.FirstName.key, userData.firstName)
            putString(PreferencesKey.LastName.key, userData.lastName)
            putString(PreferencesKey.Photo.key, userData.photo)
            putString(PreferencesKey.Profession.key, userData.profession)
            putString(PreferencesKey.ThemeSelected.key, userData.themeSelected)
            putString(PreferencesKey.Bio.key, userData.bio)
            putString(PreferencesKey.Dob.key, userData.dob)
            putString(PreferencesKey.Gender.key, userData.gender)
            putFloat(PreferencesKey.WalletBalance.key, userData.walletBalance?.toFloat() ?: 0f)
            putBoolean(PreferencesKey.AudioAllowed.key, userData.audioAllowed ?: false)
            putBoolean(PreferencesKey.ChatAllowed.key, userData.chatAllowed ?: false)
            putBoolean(PreferencesKey.VideoAllowed.key, userData.videoAllowed ?: false)
            putString(PreferencesKey.UserType.key, userData.userType)
            putString(PreferencesKey.Message.key, userData.message)
            apply()
        }
    }

    fun getUser(): IsUserCreatedResponse? {
        val userId = sharedPreferences.getString(PreferencesKey.UserId.key, null) ?: return null
        val userData = IsUserCreatedResponse(
            _id = userId,
            username = sharedPreferences.getString(PreferencesKey.Username.key, null),
            phone = sharedPreferences.getString(PreferencesKey.Phone.key, null),
            fullName = sharedPreferences.getString(PreferencesKey.FullName.key, null),
            firstName = sharedPreferences.getString(PreferencesKey.FirstName.key, null),
            lastName = sharedPreferences.getString(PreferencesKey.LastName.key, null),
            photo = sharedPreferences.getString(PreferencesKey.Photo.key, null),
            profession = sharedPreferences.getString(PreferencesKey.Profession.key, null),
            themeSelected = sharedPreferences.getString(PreferencesKey.ThemeSelected.key, null),
            gender = sharedPreferences.getString(PreferencesKey.Gender.key, null),
            dob = sharedPreferences.getString(PreferencesKey.Dob.key, null),
            bio = sharedPreferences.getString(PreferencesKey.Bio.key, null),
            walletBalance = sharedPreferences.getFloat(PreferencesKey.WalletBalance.key, 0f).toDouble(),
            audioAllowed = sharedPreferences.getBoolean(PreferencesKey.AudioAllowed.key, false),
            chatAllowed = sharedPreferences.getBoolean(PreferencesKey.ChatAllowed.key, false),
            videoAllowed = sharedPreferences.getBoolean(PreferencesKey.VideoAllowed.key, false),
            userType = sharedPreferences.getString(PreferencesKey.UserType.key, null),
            message = sharedPreferences.getString(PreferencesKey.Message.key, null)
        )
        return userData
    }

    fun storeUpdateUserResponseInPreferences(response: UpdateUserResponse) {
        sharedPreferences.edit().apply {
            putString(PreferencesKey.Username.key, response.updatedUser.username)
            putString(PreferencesKey.Phone.key, response.updatedUser.phone)
            putString(PreferencesKey.FullName.key, response.updatedUser.fullName)
            putString(PreferencesKey.Profession.key, response.updatedUser.profession)
            putString(PreferencesKey.ThemeSelected.key, response.updatedUser.themeSelected)
            putString(PreferencesKey.Photo.key, response.updatedUser.photo)
            putString(PreferencesKey.VideoRate.key, response.updatedUser.videoRate)
            putString(PreferencesKey.AudioRate.key, response.updatedUser.audioRate)
            putString(PreferencesKey.ChatRate.key, response.updatedUser.chatRate)
            putString(PreferencesKey.Gender.key, response.updatedUser.gender)
            putString(PreferencesKey.Dob.key, response.updatedUser.dob)
            putString(PreferencesKey.Bio.key, response.updatedUser.bio)
            apply()
        }
    }

    fun storeResponseInPreferences(response: CreateUserResponse) {
        sharedPreferences.edit().apply {
            putString(PreferencesKey.UserId.key, response._id)
            putString(PreferencesKey.Username.key, response.username)
            putString(PreferencesKey.Phone.key, response.phone)
            putString(PreferencesKey.FullName.key, response.fullName)
            putString(PreferencesKey.FirstName.key, response.firstName)
            putString(PreferencesKey.LastName.key, response.lastName)
            putString(PreferencesKey.Photo.key, response.photo)
            putString(PreferencesKey.Profession.key, response.profession)
            putString(PreferencesKey.ThemeSelected.key, response.themeSelected)
            putString(PreferencesKey.VideoRate.key, response.videoRate)
            putString(PreferencesKey.AudioRate.key, response.audioRate)
            putString(PreferencesKey.ChatRate.key, response.chatRate)
            putString(PreferencesKey.Gender.key, response.gender)
            putString(PreferencesKey.Dob.key, response.dob)
            putString(PreferencesKey.Bio.key, response.bio)
            putString(PreferencesKey.CreatedAt.key, response.createdAt)
            putString(PreferencesKey.UpdatedAt.key, response.updatedAt)
            putString(PreferencesKey.V.key, response.__v)
            apply()
        }
    }

    fun saveKyc(kyc: Boolean){
        sharedPreferences.edit().apply {
            putBoolean(PreferencesKey.KYC.key, kyc)
            apply()
        }
    }
    fun isKyc(): Boolean{
        return sharedPreferences.getBoolean(PreferencesKey.KYC.key, false)
    }

    fun saveTodaysWalletBalance(balance: Int){
        sharedPreferences.edit().apply {
            putInt(PreferencesKey.TodaysWalletBalance.key,balance)
            apply()
        }
    }

    fun getTodaysWalletBalance(): Int{
        return sharedPreferences.getInt(PreferencesKey.TodaysWalletBalance.key,0)
    }

    fun getShareLink(): String{
        return sharedPreferences.getString(PreferencesKey.ShareLink.key, "")+""
    }
    fun saveShareLink(shareLink: String){
        sharedPreferences.edit().apply{
            putString(PreferencesKey.ShareLink.key,shareLink)
            apply()
        }
    }

    fun getUserAssistanceLink(): String{
        return sharedPreferences.getString(PreferencesKey.UserAssistanceLink.key, "")+""
    }
    fun saveUserAssistanceLink(shareLink: String){
        sharedPreferences.edit().apply{
            putString(PreferencesKey.UserAssistanceLink.key,shareLink)
            apply()
        }
    }
    fun getUserAssistanceLinkDesc(): String{
        return sharedPreferences.getString(PreferencesKey.UserAssistanceLinkDesc.key, "")+""
    }
    fun saveUserAssistanceLinkDesc(shareLink: String){
        sharedPreferences.edit().apply{
            putString(PreferencesKey.UserAssistanceLinkDesc.key,shareLink)
            apply()
        }
    }

    fun savePaymentSettings(model: LocalPaymentSetting){
        sharedPreferences.edit().apply {
            putBoolean(PreferencesKey.IsPayment.key, model.isPayment)
            putString(PreferencesKey.PaymentMode.key, model.paymentMode)
            putString(PreferencesKey.VPA.key, model.vpa)
            putString(PreferencesKey.AccountNumber.key, model.accountNumber)
            putString(PreferencesKey.IFSC.key, model.ifsc)
            apply()
        }
    }

    fun getPaymentSettings(): LocalPaymentSetting{
        var model = LocalPaymentSetting(
            isPayment = sharedPreferences.getBoolean(PreferencesKey.IsPayment.key, false),
            paymentMode = sharedPreferences.getString(PreferencesKey.PaymentMode.key,"")+""
            , vpa = sharedPreferences.getString(PreferencesKey.VPA.key,"")+""
            , accountNumber = sharedPreferences.getString(PreferencesKey.AccountNumber.key,"")+""
            , ifsc = sharedPreferences.getString(PreferencesKey.IFSC.key,"")+""
        )
        return model

    }
}
