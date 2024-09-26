package com.example.myapplication.myapplication.flashcall.repository

import android.util.Log
import com.example.myapplication.myapplication.flashcall.Data.model.RequestWithdraw
import com.example.myapplication.myapplication.flashcall.Data.model.RequestWithdrawResponse
import com.example.myapplication.myapplication.flashcall.Data.model.UserDetailsResponse
import com.example.myapplication.myapplication.flashcall.Data.model.wallet.Transaction
import com.example.myapplication.myapplication.flashcall.Data.model.wallet.TransactionsResponse
import com.example.myapplication.myapplication.flashcall.Data.model.wallet.UserId
import com.example.myapplication.myapplication.flashcall.Data.network.APIService
import com.example.myapplication.myapplication.flashcall.utils.SafeApiRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class WalletRepo @Inject constructor(
    private val apiService: APIService
) : IWalletRepo,SafeApiRequest() {

    override suspend fun getTransactions(url: String): Flow<TransactionsResponse> {
        return flow {
            val response = apiService.getTransactions(url)
            emit( safeApiRequest { response } )

        }.flowOn(Dispatchers.IO)
    }

    suspend fun sendWithdrawRequest(url: String, body: RequestWithdraw): Flow<RequestWithdrawResponse> {
        return flow {
            val response = apiService.withdrawBalance(url, body)
            emit( safeApiRequest { response } )
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun userDetails(url: String , userId: UserId): Flow<UserDetailsResponse> {
        return flow {
            val response = apiService.getUserById(url ,userId )
            emit(safeApiRequest { response } )
        }
    }
}