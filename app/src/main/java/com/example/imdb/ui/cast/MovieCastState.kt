package com.example.imdb.ui.cast

import com.example.imdb.ui.core.RVItem

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