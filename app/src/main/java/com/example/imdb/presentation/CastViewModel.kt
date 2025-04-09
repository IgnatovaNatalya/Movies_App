package com.example.imdb.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.imdb.domain.api.MoviesInteractor
import com.example.imdb.domain.models.MovieCast
import com.example.imdb.ui.moviecast.MovieCastState

class CastViewModel(
    private val movieId: String,
    private val moviesInteractor: MoviesInteractor,
) : ViewModel() {

    private val _stateLiveData = MutableLiveData<MovieCastState>()
    val movieCastState: LiveData<MovieCastState> = _stateLiveData

    init {
        searchMovieCast(movieId)
    }

    fun searchMovieCast(movieId: String) {
        renderState(MovieCastState.Loading)
         moviesInteractor.searchMovieCast(movieId, object : MoviesInteractor.MovieCastConsumer {
            override fun consume(movieCast: MovieCast?, errorMessage: String?) {
                if (movieCast != null)
                    renderState(MovieCastState.Content(movieCast))
                else
                    renderState(MovieCastState.Error(errorMessage.toString()))
            }
        })
    }

    private fun renderState(state: MovieCastState) {
        _stateLiveData.postValue(state)
    }
}