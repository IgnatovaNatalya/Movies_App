package com.example.imdb.ui.moviecast

import com.example.imdb.domain.models.MovieCast

sealed interface MovieCastState {
    object Loading : MovieCastState

    data class Content(
        val movieCast: MovieCast
    ) : MovieCastState

    data class Error(
        val errorMessage: String
    ) : MovieCastState
}