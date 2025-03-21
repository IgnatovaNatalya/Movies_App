package com.example.imdb.presentation.poster

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.imdb.domain.api.MoviesInteractor
import com.example.imdb.domain.models.Movie

class PosterViewModel(private val moviesInteractor: MoviesInteractor) : ViewModel() {

    private val movieLiveData = MutableLiveData<Movie>()

    fun observeMovie(): LiveData<Movie> = movieLiveData

    fun renderMovie(movie: Movie) {
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