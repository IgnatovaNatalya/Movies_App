package com.example.imdb.presentation.details

import com.example.imdb.domain.models.MovieDetails

sealed interface MovieDetailsState {

    object Loading : MovieDetailsState

    data class Content(
        val movieDetails: MovieDetails
    ) : MovieDetailsState

    data class Error(
        val errorMessage: String
    ) : MovieDetailsState
}
