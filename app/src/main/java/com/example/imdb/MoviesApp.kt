package com.example.imdb

import android.app.Application
import com.example.imdb.presentation.movies.MoviesSearchPresenter

class MoviesApp : Application() {

    var moviesSearchPresenter : MoviesSearchPresenter? = null

}