package com.example.imdb.domain.api

import com.example.imdb.domain.models.SearchResult

interface MoviesInteractor {
    fun searchMovies(expression: String, consumer: MoviesConsumer)

    interface MoviesConsumer {
        fun consume(searchResult: SearchResult)
    }
}