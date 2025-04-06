package com.example.imdb.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.imdb.domain.api.MoviesInteractor
import com.example.imdb.domain.models.MovieDetails
import com.example.imdb.ui.details.MovieDetailsState
import com.example.imdb.ui.movies.ToastState

class AboutViewModel(private val movieId: String,
                     private val moviesInteractor: MoviesInteractor, ) : ViewModel() {

    private val _stateLiveData = MutableLiveData<MovieDetailsState>()
    val movieDetailsState: LiveData<MovieDetailsState> = _stateLiveData

    private val _toastState = MutableLiveData<ToastState>(ToastState.None)
    val toastState:LiveData<ToastState> = _toastState

    init {
        searchMovieDetails(movieId)
    }

    fun searchMovieDetails(movieId: String) {
        if (movieId.isNotEmpty()) {
            renderState(MovieDetailsState.Loading)
            moviesInteractor.searchMovieDetails(movieId, object: MoviesInteractor.MovieDetailsConsumer {
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

    private fun renderState(state: MovieDetailsState) {
        _stateLiveData.postValue(state)
    }

    fun showToast(toast: ToastState) {
        _toastState.postValue(toast)
    }

    fun toastWasShown() {
        _toastState.value = ToastState.None
    }

    fun toggleFavorite(movieDetails: MovieDetails) {
        if (movieDetails.inFavorite) {
            moviesInteractor.removeMovieFromFavorites(movieDetails.id)
            renderState(MovieDetailsState.Content(movieDetails.copy(inFavorite = false)))
        } else {
            moviesInteractor.addMovieToFavorites(movieDetails.id)
            renderState(MovieDetailsState.Content(movieDetails.copy(inFavorite = true)))
        }
    }

}