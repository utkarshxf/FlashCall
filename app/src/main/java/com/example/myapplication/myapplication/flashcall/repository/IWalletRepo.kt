package com.example.myapplication.myapplication.flashcall.repository

import com.example.myapplication.myapplication.flashcall.Data.model.UserDetailsResponse
import com.example.myapplication.myapplication.flashcall.Data.model.wallet.Transaction
import com.example.myapplication.myapplication.flashcall.Data.model.wallet.TransactionsResponse
import com.example.myapplication.myapplication.flashcall.Data.model.wallet.UserId
import kotlinx.coroutines.flow.Flow

interface IWalletRepo {

    suspend fun getTransactions(url : String) : Flow<TransactionsResponse>
    suspend fun userDetails(url : String,userId: UserId) : Flow<UserDetailsResponse>
}