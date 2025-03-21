package com.example.imdb.di

import android.content.Context
import com.example.imdb.data.LocalStorage
import com.example.imdb.data.MoviesRepositoryImpl
import com.example.imdb.data.NetworkClient
import com.example.imdb.data.network.ImdbApiService
import com.example.imdb.data.network.RetrofitNetworkClient
import com.example.imdb.domain.api.MoviesInteractor
import com.example.imdb.domain.api.MoviesRepository
import com.example.imdb.domain.impl.MoviesInteractorImpl
import com.example.imdb.presentation.movies.MoviesSearchViewModel
import com.example.imdb.presentation.poster.PosterViewModel
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val repositoryModule = module {
    single<MoviesRepository> { MoviesRepositoryImpl(get(), get()) }

    single<NetworkClient> { RetrofitNetworkClient(androidContext(), get()) }

    //context
    single {
        androidContext()
            .getSharedPreferences("local_storage", Context.MODE_PRIVATE)
    }

    //apiServ
    single<ImdbApiService> {
        Retrofit.Builder()
            .baseUrl("https://tv-api.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ImdbApiService::class.java)
    }

    factory { Gson() }

    single { LocalStorage(get()) }

    //interactor
    single<MoviesInteractor> { MoviesInteractorImpl(get()) }

    //viewmodel
    viewModel { MoviesSearchViewModel(get(), androidContext()) }

    viewModel { PosterViewModel(get()) }

}
