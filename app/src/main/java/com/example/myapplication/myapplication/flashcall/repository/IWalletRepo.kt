package com.example.myapplication.myapplication.flashcall.repository

import com.example.myapplication.myapplication.flashcall.Data.model.wallet.Transaction
import com.example.myapplication.myapplication.flashcall.Data.model.wallet.TransactionResponse
import kotlinx.coroutines.flow.Flow

interface IWalletRepo {

    suspend fun getTransactions(url : String) : Flow<TransactionResponse>
}