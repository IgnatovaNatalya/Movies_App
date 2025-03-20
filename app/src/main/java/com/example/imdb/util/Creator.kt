package com.example.imdb.util

import android.content.Context
import android.content.SharedPreferences
import com.example.imdb.data.LocalStorage
import com.example.imdb.data.MoviesRepositoryImpl
import com.example.imdb.data.network.ImdbApiService
import com.example.imdb.data.network.RetrofitNetworkClient
import com.example.imdb.domain.api.*
import com.example.imdb.domain.impl.MoviesInteractorImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Creator {

    private var sharedPrefs: SharedPreferences? = null

    private fun getSharedPreferences(context: Context): SharedPreferences {
        if (sharedPrefs == null)
            sharedPrefs = context.getSharedPreferences("local_storage", Context.MODE_PRIVATE)
        return sharedPrefs!!
    }

    private fun getMoviesRepository(context: Context): MoviesRepository {
        return MoviesRepositoryImpl(getNetworkClient(context), getLocalStorage(context))
    }

    private fun getLocalStorage(context: Context): LocalStorage {
        return LocalStorage(getSharedPreferences(context))
    }

    private fun getNetworkClient(context: Context): RetrofitNetworkClient {
        return RetrofitNetworkClient(context, getApiService())
    }

    private fun getApiService(): ImdbApiService {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://tv-api.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ImdbApiService::class.java)
    }

    fun provideMoviesInteractor(context: Context): MoviesInteractor {
        return MoviesInteractorImpl(getMoviesRepository(context))
    }
}