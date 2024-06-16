package com.example.myapplication.myapplication.flashcall.di

import com.example.myapplication.myapplication.flashcall.Data.network.APIService
import com.example.myapplication.myapplication.flashcall.repository.AuthRepository
import com.example.myapplication.myapplication.flashcall.repository.ChatRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object APIModule {

    @Provides
    @Singleton
    fun providesRetrofit() : Retrofit {
        val moshi = GsonConverterFactory.create()
        return Retrofit.Builder()
            .baseUrl("https://flashcall.vercel.app/")
            .client(OkHttpClient())
            .addConverterFactory(moshi)
            .build()

    }

    @Provides
    @Singleton
    fun providesAPIService(retrofit: Retrofit) : APIService {
        return retrofit.create(APIService::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun providesAuthRepository(apiService: APIService) : AuthRepository {
        return AuthRepository(apiService)
    }

    @Provides
    @Singleton
    fun providesFirebaseFirestore() : FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseStorage() : FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun provideChatRepository(
        firestore: FirebaseFirestore,
        storage: FirebaseStorage,
    ) : ChatRepository = ChatRepository(firestore, storage)



}
