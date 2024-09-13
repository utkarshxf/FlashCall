package com.example.myapplication.myapplication.flashcall.ViewModel.wallet

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.myapplication.flashcall.Data.model.UserDetailsResponse
import com.example.myapplication.myapplication.flashcall.Data.model.wallet.Transaction
import com.example.myapplication.myapplication.flashcall.Data.model.wallet.TransactionsResponse
import com.example.myapplication.myapplication.flashcall.Data.model.wallet.UserId
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

    private val _transactions = MutableStateFlow<TransactionsResponse>(TransactionsResponse())
    val transactions: StateFlow<TransactionsResponse> = _transactions
    private val _userDetails = MutableStateFlow<UserDetailsResponse>(UserDetailsResponse())
    val userDetails: StateFlow<UserDetailsResponse> = _userDetails

    fun fetchTransactions(uid: String) {
        viewModelScope.launch {
            try {
                walletRepo.getTransactions("https://flashcall.vercel.app/api/v1/transaction/getUserTransactions?userId=$uid").collect {
                    _transactions.value = it
                    Log.d("TransactionViewModel", "getTransaction: $it")
                }
            } catch (e: Exception) {
                Log.d("TransactionViewModel", "getTransaction: ${e.message}")
            }
        }
    }
    fun getUserDetails(uid: String)
    {
        viewModelScope.launch {
            try {
                walletRepo.userDetails("https://flashcall.me/api/v1/creator/getUserById" , userId = UserId(uid)).collect {
                    _userDetails.value = it
                    Log.d("userDetails", "userDetails: $it")
                }
            }catch (e:Exception)
            {
                Log.d("userDetails", "userDetails: ${e.message}")
            }
        }
    }


//    val uid = _transactions.value.get(0).userId
}

