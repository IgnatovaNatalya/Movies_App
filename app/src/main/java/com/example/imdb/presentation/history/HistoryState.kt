package com.example.imdb.presentation.history

import com.example.imdb.domain.models.Movie

sealed interface HistoryState {

    object Loading : HistoryState

    data class Content(
        val movies: List<Movie>
    ) : HistoryState

    data class Empty(
        val message: String
    ) : HistoryState
}