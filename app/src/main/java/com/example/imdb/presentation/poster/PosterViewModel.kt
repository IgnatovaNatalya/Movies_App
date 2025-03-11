package com.example.imdb.presentation.poster

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.imdb.domain.models.Movie
import com.example.imdb.util.Creator

class PosterViewModel(application: Application) : AndroidViewModel(application) {

    private var moviesInteractor = Creator.provideMoviesInteractor(getApplication())

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PosterViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }

    private val movieLiveData = MutableLiveData<Movie>()


    fun observeMovie(): LiveData<Movie> = movieLiveData

    fun renderMovie(movie:Movie) {
        movieLiveData.postValue(movie)
    }

    fun toggleFavorite(movie: Movie) {
        if (movie.inFavorite) {
            moviesInteractor.removeMovieFromFavorites(movie)
        } else {
            moviesInteractor.addMovieToFavorites(movie)
        }
        movieLiveData.postValue(movie.copy(inFavorite = !movie.inFavorite))
    }

}