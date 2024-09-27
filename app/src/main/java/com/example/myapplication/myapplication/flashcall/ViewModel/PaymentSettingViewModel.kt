package com.example.myapplication.myapplication.flashcall.ViewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.myapplication.flashcall.Data.model.paymentSetting.AddBankDetailsRequest
import com.example.myapplication.myapplication.flashcall.Data.model.paymentSetting.AddUpiRequest
import com.example.myapplication.myapplication.flashcall.Data.model.paymentSetting.LocalPaymentSetting
import com.example.myapplication.myapplication.flashcall.repository.PaymentSettingRepository
import com.example.myapplication.myapplication.flashcall.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentSettingViewModel @Inject constructor(
    private val repository: PaymentSettingRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val userId = userPreferencesRepository.getUser()?._id

    var paymentSettingState by mutableStateOf(PaymentSettingState())


    fun getPaymentSettings() {
        paymentSettingState =
            paymentSettingState.copy(paymentDetails = userPreferencesRepository.getPaymentSettings())
        viewModelScope.launch {
            repository.getPaymentSetting("api/v1/creator/getPayment?userId=$userId")
                .collect { response ->
                    Log.d("PaymentSettings","response: $response")
                    var model = LocalPaymentSetting(false,"", "", "", "")
                    if (response.success != null && response.success == true) {
                        model.isPayment = true
                        if (response.data?.upiId != null){
                            model.vpa = response.data?.upiId!!
                        }
                        if(response.data?.paymentMode != null){
                            model.paymentMode = response.data?.paymentMode!!
                        }
                        if(response.data?.bankDetails?.accountNumber != null){
                            model.accountNumber = response.data?.bankDetails?.accountNumber!!
                        }
                        if(response.data?.bankDetails?.ifsc != null){
                            model.ifsc = response.data?.bankDetails?.ifsc!!
                        }
                        userPreferencesRepository.savePaymentSettings(model = model)
                        paymentSettingState = paymentSettingState.copy(success = true, paymentDetails = userPreferencesRepository.getPaymentSettings())
                    }
                }
        }
    }

    var addUpiState by mutableStateOf(AddUPIIDState())
    fun addUpiId(upi: String){
        addUpiState = addUpiState.copy(isLoading = true)
        viewModelScope.launch {
            repository.addUPIDetails(url = "api/v1/creator/verifyUpi", body = AddUpiRequest(userId = userId, vpa = upi)).collect{response ->
                if (response.success != null && response.success == true){
                    addUpiState = addUpiState.copy(isLoading = false, varified = true)
                    var model = LocalPaymentSetting(true,"UPI", upi, userPreferencesRepository.getPaymentSettings().accountNumber, userPreferencesRepository.getPaymentSettings().ifsc)
                    userPreferencesRepository.savePaymentSettings(model = model)
                    paymentSettingState = paymentSettingState.copy(paymentDetails = userPreferencesRepository.getPaymentSettings())
                }else{
                    addUpiState = addUpiState.copy(isLoading = false, varified = false, error = response.toString())
                }
            }
        }
    }


    var addBankDetailsState by mutableStateOf(AddBankDetailsState())
    fun addBankDetails(accountNumber: String, ifsc: String){
        addBankDetailsState = addBankDetailsState.copy(isLoading = true)
        viewModelScope.launch {
            repository.addBankDetails(url = "api/v1/creator/verifyBank", body = AddBankDetailsRequest(userId = userId, bank_account = accountNumber, ifsc = ifsc)).collect{ response ->
                if (response.success != null && response.success == true){
                    var model = LocalPaymentSetting(true,"BANK_TRANSFER", userPreferencesRepository.getPaymentSettings().vpa, accountNumber, ifsc)
                    userPreferencesRepository.savePaymentSettings(model = model)
                    paymentSettingState = paymentSettingState.copy(paymentDetails = userPreferencesRepository.getPaymentSettings())
                    addBankDetailsState = addBankDetailsState.copy(isLoading = false, varified = true)
                }else{
                    addBankDetailsState = addBankDetailsState.copy(isLoading = false, varified = false, error = response.toString())
                }
            }
        }
    }


    data class AddUPIIDState(
        var isLoading: Boolean = false,
        var varified: Boolean = false,
        var error: String? = null
    )

    data class AddBankDetailsState(
        var isLoading: Boolean = false,
        var varified: Boolean = false,
        var error: String? = null
    )

    data class PaymentSettingState(
        var isLoading: Boolean = false,
        var success: Boolean = false,
        var paymentDetails: LocalPaymentSetting = LocalPaymentSetting(isPayment = false, paymentMode = "",vpa = "", accountNumber = "", ifsc = "")
    )
}