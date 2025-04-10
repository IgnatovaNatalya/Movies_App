package com.example.imdb.presentation.cast

import com.example.imdb.domain.models.MovieCastPerson

sealed interface MovieCastRVItem {

    data class HeaderItem(
        val headerText: String,
    ) : MovieCastRVItem

    data class PersonItem(
        val data: MovieCastPerson,
    ) : MovieCastRVItem

}