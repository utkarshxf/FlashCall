package com.example.myapplication.myapplication.flashcall.di

import android.app.Application
import android.app.Notification
import android.content.Context
import android.content.SharedPreferences
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
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.core.StreamVideoBuilder
import io.getstream.video.android.model.User
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

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context = context

    @Provides
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences("user_prefs1", Context.MODE_PRIVATE)
    }

    @Provides
    fun provideStreamVideoBuilder(
        @ApplicationContext context: Context,
        sharedPreferences: SharedPreferences
    ): StreamVideo {
        val userId = sharedPreferences.getString("user_id", "user_Id") ?: "user_Id"
        val userName = sharedPreferences.getString("full_name", "Unknown User") ?: "Unknown User"
        val profileImage = sharedPreferences.getString("photo", "") ?: ""
        return StreamVideoBuilder(
            context = context,
            apiKey = "9jpqevnvhfzv",
            token = StreamVideo.devToken(userId),
            user = User(
                id = userId,
                name = userName,
                image = profileImage.ifEmpty { "null" },
                role = "admin"
            ),
            ringNotification = { call -> Notification.Builder(context).build() }
        ).build()
    }
}
