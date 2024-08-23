package com.example.myapplication.myapplication.flashcall.ViewModel.wallet

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.myapplication.flashcall.Data.model.feedback.FeedBackResponse
import com.example.myapplication.myapplication.flashcall.Data.model.wallet.Transaction
import com.example.myapplication.myapplication.flashcall.Data.model.wallet.TransactionResponse
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
) : ViewModel() {

    private val _transactions = MutableStateFlow<TransactionResponse>(TransactionResponse())
    val transactions: StateFlow<TransactionResponse> = _transactions

    fun fetchTransactions(uid: String) {
        viewModelScope.launch {
            try {
                walletRepo.getTransactions("https://flashcall.vercel.app/api/v1/transaction/getUserTransactions?userId=$uid").collect {
                    if (it != null) {
                        _transactions.value = it
                        Log.d("TransactionViewModel", "getTransaction: $it")
                    } else {
                        Log.d("TransactionViewModel", "getTransaction: Feedback Error")
                    }
                }
            } catch (e: Exception) {
                Log.d("TransactionViewModel", "getTransaction: ${e.message}")
            }
        }
    }


//    val uid = _transactions.value.get(0).userId
}

