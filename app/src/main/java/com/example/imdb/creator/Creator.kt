package com.example.imdb.creator


import android.app.Activity
import com.example.imdb.data.MoviesRepositoryImpl
import com.example.imdb.data.network.RetrofitNetworkClient
import com.example.imdb.domain.api.*
import com.example.imdb.domain.impl.MoviesInteractorImpl
import com.example.imdb.presentation.MoviesSearchController
import com.example.imdb.presentation.PosterController
import com.example.imdb.ui.movies.MoviesAdapter

object Creator {
    private fun getMoviesRepository(): MoviesRepository {
        return MoviesRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideMoviesInteractor(): MoviesInteractor {
        return MoviesInteractorImpl(getMoviesRepository())
    }

    fun provideMoviesSearchController(
        activity: Activity,
        adapter: MoviesAdapter
    ): MoviesSearchController {
        return MoviesSearchController(activity, adapter)
    }

    fun providePosterController(activity: Activity): PosterController {
        return PosterController(activity)
    }
}