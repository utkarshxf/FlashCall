package com.example.myapplication.myapplication.flashcall.repository

import com.example.myapplication.myapplication.flashcall.Data.model.wallet.Transaction
import com.example.myapplication.myapplication.flashcall.Data.model.wallet.TransactionResponse
import com.example.myapplication.myapplication.flashcall.Data.network.APIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class WalletRepo @Inject constructor(
    private val apiService: APIService
) : IWalletRepo {

    override suspend fun getTransactions(url: String): Flow<TransactionResponse> {

        return flow {

            val response = apiService.getTransactions(url)

            emit(response)

        }.flowOn(Dispatchers.IO)
    }
}