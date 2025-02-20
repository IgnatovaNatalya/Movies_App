package com.example.imdb.creator


import com.example.imdb.data.MoviesRepositoryImpl
import com.example.imdb.data.network.RetrofitNetworkClient
import com.example.imdb.domain.api.*
import com.example.imdb.domain.impl.MoviesInteractorImpl

object Creator {
    private fun getMoviesRepository(): MoviesRepository {
        return MoviesRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideMoviesInteractor(): MoviesInteractor {
        return MoviesInteractorImpl(getMoviesRepository())
    }
}