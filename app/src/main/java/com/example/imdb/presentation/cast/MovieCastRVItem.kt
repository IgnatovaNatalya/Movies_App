package com.example.imdb.presentation.cast

import com.example.imdb.domain.models.MovieCastPerson
import com.example.imdb.ui.core.RVItem

sealed interface MovieCastRVItem: RVItem {

    data class HeaderItem(
        val headerText: String,
    ) : MovieCastRVItem

    data class PersonItem(
        val data: MovieCastPerson,
    ) : MovieCastRVItem

}