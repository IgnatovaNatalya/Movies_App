package com.example.imdb.domain.api

import com.example.imdb.domain.models.Movie

interface MoviesRepository {
    fun searchMovies(expression: String): List<Movie>
}