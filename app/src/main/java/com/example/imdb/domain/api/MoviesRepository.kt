package com.example.imdb.domain.api

import com.example.imdb.domain.models.Movie
import com.example.imdb.util.Resource

interface MoviesRepository {
    fun searchMovies(expression: String): Resource<List<Movie>>
}