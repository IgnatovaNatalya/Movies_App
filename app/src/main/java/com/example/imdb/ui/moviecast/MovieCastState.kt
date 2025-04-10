package com.example.imdb.ui.moviecast

import com.example.imdb.presentation.cast.MovieCastRVItem

sealed interface MovieCastState {
    object Loading : MovieCastState

    data class Content(
        val fullTitle: String,
        val items: List<MovieCastRVItem>,
    ) : MovieCastState

    data class Error(
        val errorMessage: String
    ) : MovieCastState
}