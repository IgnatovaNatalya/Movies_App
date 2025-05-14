package com.example.imdb.presentation.cast

import com.example.imdb.util.RVItem

sealed interface MovieCastState {
    object Loading : MovieCastState

    data class Content(
        val fullTitle: String,
        val items: List<RVItem>,
    ) : MovieCastState

    data class Error(
        val errorMessage: String
    ) : MovieCastState
}