package com.example.imdb.presentation.poster

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.imdb.domain.api.MoviesInteractor
import com.example.imdb.domain.models.Movie

class DetailsViewModel(private val moviesInteractor: MoviesInteractor) : ViewModel() {

    private val _movieLiveData = MutableLiveData<Movie>()
    val currentMovie: LiveData<Movie> = _movieLiveData

    fun setCurrentMovie(movie: Movie) {
        _movieLiveData.postValue(movie)
    }

    fun toggleFavorite(movie: Movie) {
        if (movie.inFavorite) {
            moviesInteractor.removeMovieFromFavorites(movie)
        } else {
            moviesInteractor.addMovieToFavorites(movie)
        }
        _movieLiveData.postValue(movie.copy(inFavorite = !movie.inFavorite))
    }

    fun toggleFavoriteCurrentMovie() {
        val movie = _movieLiveData.value
        if (movie != null) toggleFavorite(movie)
    }

}