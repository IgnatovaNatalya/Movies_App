package com.example.imdb.util


import android.app.Activity
import android.content.Context
import com.example.imdb.data.MoviesRepositoryImpl
import com.example.imdb.data.network.RetrofitNetworkClient
import com.example.imdb.domain.api.*
import com.example.imdb.domain.impl.MoviesInteractorImpl
import com.example.imdb.presentation.MoviesSearchController
import com.example.imdb.presentation.PosterController
import com.example.imdb.ui.movies.MoviesAdapter

object Creator {
    private fun getMoviesRepository(context: Context): MoviesRepository {
        return MoviesRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideMoviesInteractor(context: Context): MoviesInteractor {
        return MoviesInteractorImpl(getMoviesRepository(context))
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