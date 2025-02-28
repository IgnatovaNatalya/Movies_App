package com.example.imdb.domain.api

import com.example.imdb.domain.models.SearchResult

interface MoviesRepository {
    fun searchMovies(expression: String): SearchResult
}