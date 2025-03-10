package com.example.imdb.util

import android.content.Context
import com.example.imdb.data.LocalStorage
import com.example.imdb.data.MoviesRepositoryImpl
import com.example.imdb.data.network.RetrofitNetworkClient
import com.example.imdb.domain.api.*
import com.example.imdb.domain.impl.MoviesInteractorImpl

object Creator {
    private fun getMoviesRepository(context: Context): MoviesRepository {
        return MoviesRepositoryImpl(
            networkClient = RetrofitNetworkClient(context),
            localStorage = LocalStorage(
                context.getSharedPreferences(
                    "local_storage",
                    Context.MODE_PRIVATE
                )
            )
        )
    }

    fun provideMoviesInteractor(context: Context): MoviesInteractor {
        return MoviesInteractorImpl(getMoviesRepository(context))
    }

//    fun provideMoviesSearchPresenter( context: Context ): MoviesSearchViewModel {
//        return MoviesSearchViewModel(context)
//    }

  //  fun providePosterPresenter(posterView: PosterView): PosterViewModel {
  //      return PosterViewModel(posterView)
  //  }
}