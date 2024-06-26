package com.example.myapplication.myapplication.flashcall.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.myapplication.myapplication.flashcall.Data.network.APIService
import com.example.myapplication.myapplication.flashcall.preferenceStore.PreferenceStore
import com.example.myapplication.myapplication.flashcall.preferenceStore.userPref
import com.example.myapplication.myapplication.flashcall.repository.AuthRepository
import com.example.myapplication.myapplication.flashcall.repository.ChatRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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


    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ), produceFile = {
                context.preferencesDataStoreFile("user_data")
            }
        )
    }

    @Provides
    fun providesUserPref(dataStore: DataStore<Preferences>)
            : userPref = PreferenceStore(dataStore)

}
