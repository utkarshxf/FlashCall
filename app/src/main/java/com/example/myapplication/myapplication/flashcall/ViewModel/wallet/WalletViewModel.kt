package com.example.myapplication.myapplication.flashcall.ViewModel.wallet

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.myapplication.flashcall.Data.model.CreateUserResponse
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

    fun fetchTransactions(uid: String) {

        viewModelScope.launch {
            val localTransactions = getLocalTransactions(uid)
            _transactions.value = localTransactions

            try {
                walletRepo.getTransactions("https://flashcall.vercel.app/api/v1/transaction/getUserTransactions?userId=$uid").collect {
                    Log.d("UserId","UserId: ${uid}")
                    _transactions.value = it
                    saveLocalTransactions(uid, it)
                    Log.d("TransactionViewModel", "getTransaction: $it")
                }
            } catch (e: Exception) {
                Log.d("TransactionViewModel", "getTransaction: ${e.message}")
            }
        }
    }
    private fun saveLocalTransactions(uid: String, transactions: TransactionsResponse) {
        userPreferencesRepository.storeTransactions(uid , transactions)
    }
    private fun getLocalTransactions(uid: String): TransactionsResponse {
        return userPreferencesRepository.retrieveTransactions(uid)
    }
}

