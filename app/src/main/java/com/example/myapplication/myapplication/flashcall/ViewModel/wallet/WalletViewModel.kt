package com.example.myapplication.myapplication.flashcall.ViewModel.wallet

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.myapplication.flashcall.Data.model.CreateUserResponse
import com.example.myapplication.myapplication.flashcall.Data.model.RequestWithdraw
import com.example.myapplication.myapplication.flashcall.Data.model.UserDetailsResponse
import com.example.myapplication.myapplication.flashcall.Data.model.wallet.Transaction
import com.example.myapplication.myapplication.flashcall.Data.model.wallet.TransactionsResponse
import com.example.myapplication.myapplication.flashcall.Data.model.wallet.UserId
import com.example.myapplication.myapplication.flashcall.repository.UserPreferencesRepository
import com.example.myapplication.myapplication.flashcall.repository.WalletRepo
import com.google.api.Context
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val walletRepo: WalletRepo,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val _transactions = MutableStateFlow<TransactionsResponse>(TransactionsResponse())
    val transactions: StateFlow<TransactionsResponse> = _transactions
    val userId = userPreferencesRepository.getUser()?._id
    init {
        if (userId != null) {
            fetchTransactions(userId)
        }
    }

    fun fetchTransactions(uid: String) {
        viewModelScope.launch {
            val localTransactions = getLocalTransactions(uid)
            _transactions.value = localTransactions

            try {
                walletRepo.getTransactions("https://flashcall.vercel.app/api/v1/transaction/getUserTransactions?userId=$uid").collect {
                    _transactions.value = it
                    saveLocalTransactions(uid, it)
                    Log.d("TransactionViewModel", "getTransaction: $it")
                }
            } catch (e: Exception) {
                Log.d("TransactionViewModel", "getTransaction: ${e.message}")
            }
        }
    }

    var requestWithdrawState by mutableStateOf(RequestWithdrawStates())
        private set

    fun withdrawRequest(){
        requestWithdrawState = requestWithdrawState.copy(isLoading = true)
        val body = RequestWithdraw(userId+"", userPreferencesRepository.getUser()?.phone+"")
        Log.d("withdrawRequest","requestBody: $body")
        viewModelScope.launch {
            try {
                walletRepo.sendWithdrawRequest("https://flashcall.vercel.app/api/v1/transaction/withdrowBalance", body = body)
                    .collect { response ->
                        Log.d("TransactionViewModel", "getTransaction: $response")
                        if(response.success != null && response.success){
                            requestWithdrawState = requestWithdrawState.copy(isLoading = false, success = true, message = response.message)
                        }else{
                            requestWithdrawState = requestWithdrawState.copy(isLoading = false, error = response.message ,success = false)
                        }
                }
            } catch (e: Exception) {
                Log.d("TransactionViewModel", "getTransaction: ${e.message}")
                requestWithdrawState = requestWithdrawState.copy(isLoading = false, error = e.message ,success = false)

            }
        }
    }

    fun isWithdrawable(): Boolean{
        if(userPreferencesRepository.getPaymentSettings().isPayment && userPreferencesRepository.isKyc()){
            return true
        }else{
            return false
        }
    }

    private fun saveLocalTransactions(uid: String, transactions: TransactionsResponse) {
        userPreferencesRepository.storeTransactions(uid , transactions)
    }
    private fun getLocalTransactions(uid: String): TransactionsResponse {
        return userPreferencesRepository.retrieveTransactions(uid)
    }


    data class RequestWithdrawStates(
        var isLoading: Boolean = false,
        var error: String? = null,
        var success: Boolean = false,
        var message: String? = null
    )
}

