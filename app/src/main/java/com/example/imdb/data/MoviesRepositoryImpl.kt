package com.example.imdb.data

import com.example.imdb.data.dto.MovieSearchRequest
import com.example.imdb.data.dto.MovieSearchResponse
import com.example.imdb.domain.api.MoviesRepository
import com.example.imdb.domain.models.Movie
import com.example.imdb.domain.models.SearchResult

class MoviesRepositoryImpl(private val networkClient: NetworkClient) : MoviesRepository {

    override fun searchMovies(expression: String): SearchResult {
        val response = networkClient.doRequest(MovieSearchRequest(expression))
        if (response.resultCode == 200) {
            val listMovies =  (response as MovieSearchResponse).results.map { //можно тут трансформировать данные для передачи в domain-слой если надо
                Movie(it.id, it.resultType, it.image, it.title, it.description) }
            return SearchResult(resultCode = 200, results = listMovies)
        } else {
            return SearchResult(resultCode = response.resultCode, results = listOf())
        }
    }
}