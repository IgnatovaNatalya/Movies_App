package com.example.imdb.di

import android.content.Context
import com.example.imdb.data.LocalStorage
import com.example.imdb.data.MoviesRepositoryImpl
import com.example.imdb.data.NetworkClient
import com.example.imdb.data.converter.MovieCastConverter
import com.example.imdb.data.network.ImdbApiService
import com.example.imdb.data.network.RetrofitNetworkClient
import com.example.imdb.domain.api.MoviesInteractor
import com.example.imdb.domain.api.MoviesRepository
import com.example.imdb.domain.impl.MoviesInteractorImpl
import com.example.imdb.presentation.details.AboutViewModel
import com.example.imdb.presentation.cast.CastViewModel
import com.example.imdb.presentation.MoviesSearchViewModel
import com.example.imdb.presentation.details.PosterViewModel
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val koinModule = module {
    factory<MoviesRepository> { MoviesRepositoryImpl(get(), get(), get()) }

    factory<NetworkClient> { RetrofitNetworkClient(androidContext(), get()) }

    //context
    factory {
        androidContext()
            .getSharedPreferences("local_storage", Context.MODE_PRIVATE)
    }

    //apiService
    factory<ImdbApiService> {
        Retrofit.Builder()
            .baseUrl("https://tv-api.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ImdbApiService::class.java)
    }

    factory { Gson() }

    factory { LocalStorage(get()) }

    //interactor
    factory<MoviesInteractor> { MoviesInteractorImpl(get()) }
    factory<MovieCastConverter> { MovieCastConverter() }

    //viewmodel
    viewModel { MoviesSearchViewModel(get()) }
    viewModel { (movieId: String) -> AboutViewModel(movieId, get()) }
    viewModel { (posterUrl: String) -> PosterViewModel(posterUrl) }
    viewModel { (movieId: String) -> CastViewModel(movieId, get()) }

}
