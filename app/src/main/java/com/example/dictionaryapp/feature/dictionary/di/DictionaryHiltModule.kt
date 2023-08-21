package com.example.dictionaryapp.feature.dictionary.di

import android.app.Application
import androidx.room.Room
import com.example.dictionaryapp.core.connectivity.AndroidNetworkConnectivityChecker
import com.example.dictionaryapp.core.connectivity.NetworkConnectivityChecker
import com.example.dictionaryapp.core.local.database.Converters
import com.example.dictionaryapp.core.local.database.DictionaryDatabase
import com.example.dictionaryapp.core.parser.GsonParser
import com.example.dictionaryapp.core.parser.JsonParser
import com.example.dictionaryapp.core.util.SafeApiCall
import com.example.dictionaryapp.feature.dictionary.data.remote.DictionaryApi
import com.example.dictionaryapp.feature.dictionary.data.repository.WordInfoRepositoryImpl
import com.example.dictionaryapp.feature.dictionary.domain.repository.WordInfoRepository
import com.example.dictionaryapp.feature.dictionary.domain.usecases.GetWordInfoUseCase
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DictionaryHiltModule {

    @Provides
    @Singleton
    fun provideGetWordInfoUseCase(repository: WordInfoRepository) = GetWordInfoUseCase(repository)

    @Provides
    @Singleton
    fun providesWordInfoRepository(
        api: DictionaryApi,
        db: DictionaryDatabase,
        safeApiCall: SafeApiCall
    ): WordInfoRepository = WordInfoRepositoryImpl(
        api = api,
        dao = db.wordInfoDao,
        safeApiCall = safeApiCall
    )

    @Provides
    @Singleton
    fun providesDictionaryDatabase(app: Application, jsonParser: JsonParser): DictionaryDatabase {
        return Room.databaseBuilder(
            app,
            klass = DictionaryDatabase::class.java,
            name = "dictionary-db"
        ).addTypeConverter(Converters(jsonParser)).build()
    }

    @Provides
    @Singleton
    fun providesDictionaryApi(retrofit: Retrofit): DictionaryApi {
        return retrofit.create(DictionaryApi::class.java)
    }

    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(DictionaryApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesSafeApiCall(jsonParser: JsonParser): SafeApiCall =
        SafeApiCall(jsonParser = jsonParser)


    @Provides
    @Singleton
    fun providesJsonParser(): JsonParser =
        GsonParser(Gson())

    @Provides
    @Singleton
    fun provideNetworkConnectivityChecker(application: Application): NetworkConnectivityChecker {
        return AndroidNetworkConnectivityChecker(application)
    }
}