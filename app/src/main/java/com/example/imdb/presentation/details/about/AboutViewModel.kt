package com.example.imdb.presentation.details.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.imdb.domain.api.MoviesInteractor
import com.example.imdb.domain.models.MovieDetails
import com.example.imdb.presentation.details.MovieDetailsState

class AboutViewModel(
    private val movieId: String,
    private val moviesInteractor: MoviesInteractor,
) : ViewModel() {

    private val _stateLiveData = MutableLiveData<MovieDetailsState>()
    val movieDetailsState: LiveData<MovieDetailsState> = _stateLiveData

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
                            //showToast(ToastState.Show(errorMessage.toString()))
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