package com.example.imdb.util

import android.content.Context
import com.example.imdb.data.MoviesRepositoryImpl
import com.example.imdb.data.network.RetrofitNetworkClient
import com.example.imdb.domain.api.*
import com.example.imdb.domain.impl.MoviesInteractorImpl
import com.example.imdb.presentation.movies.MoviesSearchPresenter
import com.example.imdb.presentation.poster.PosterPresenter
import com.example.imdb.presentation.poster.PosterView

object Creator {
    private fun getMoviesRepository(context: Context): MoviesRepository {
        return MoviesRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideMoviesInteractor(context: Context): MoviesInteractor {
        return MoviesInteractorImpl(getMoviesRepository(context))
    }

    fun provideMoviesSearchPresenter( context: Context ): MoviesSearchPresenter {
        return MoviesSearchPresenter(context)
    }

    fun providePosterPresenter(posterView: PosterView): PosterPresenter {
        return PosterPresenter(posterView,)
    }
}