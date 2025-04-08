package com.example.imdb.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.imdb.domain.api.MoviesInteractor
import com.example.imdb.domain.models.MovieDetails
import com.example.imdb.ui.details.MovieDetailsState
import com.example.imdb.ui.movies.ToastState

class AboutViewModel(
    private val movieId: String,
    private val moviesInteractor: MoviesInteractor,
) : ViewModel() {

    private val _stateLiveData = MutableLiveData<MovieDetailsState>()
    val movieDetailsState: LiveData<MovieDetailsState> = _stateLiveData

    private val _toastState = MutableLiveData<ToastState>(ToastState.None)
    val toastState: LiveData<ToastState> = _toastState

//    private val _currentMovieId = MutableLiveData<String>(movieId)
//    val currentMovieId: LiveData<String> = _currentMovieId

    fun searchMovieDetails(movieId: String) {
        if (movieId.isNotEmpty()) {
            renderState(MovieDetailsState.Loading)
            moviesInteractor.searchMovieDetails(
                movieId,
                object : MoviesInteractor.MovieDetailsConsumer {
                    override fun consume(movieDetails: MovieDetails?, errorMessage: String?) {
                        if (movieDetails != null)
                            renderState(MovieDetailsState.Content(movieDetails))
                        else {
                            renderState(MovieDetailsState.Error("Что-то пошло не так"))
                            showToast(ToastState.Show(errorMessage.toString()))
                        }
                    }
                })
        }
    }

    init {
        searchMovieDetails(movieId)
    }

    private fun renderState(state: MovieDetailsState) {
        _stateLiveData.postValue(state)
    }

    fun showToast(toast: ToastState) {
        _toastState.postValue(toast)
    }

    fun toastWasShown() {
        _toastState.value = ToastState.None
    }

    fun toggleFavoriteCurrentMovie() {
        if ((_stateLiveData.value as MovieDetailsState.Content).movieDetails.inFavorite)
            moviesInteractor.removeMovieFromFavorites(movieId)
        else
            moviesInteractor.addMovieToFavorites(movieId)
        renderInFavoriteClick()
    }

    private fun renderInFavoriteClick() {
        val movieDetails = (_stateLiveData.value as MovieDetailsState.Content).movieDetails
        val newMovieDetails = movieDetails.copy(inFavorite = !movieDetails.inFavorite)
        renderState(MovieDetailsState.Content(newMovieDetails))
    }

}