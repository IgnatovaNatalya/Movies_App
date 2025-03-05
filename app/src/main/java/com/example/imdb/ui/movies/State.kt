package com.example.imdb.ui.movies

import com.example.imdb.domain.models.Movie

sealed interface SearchMoviesState {

    object Loading : SearchMoviesState

    data class Content(
        val movies: List<Movie>
    ) : SearchMoviesState

    data class Error(
        val errorMessage: String
    ) : SearchMoviesState

    data class Empty(
        val message: String
    ) : SearchMoviesState

}