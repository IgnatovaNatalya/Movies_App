package com.example.imdb.data

import com.example.imdb.data.dto.MovieSearchRequest
import com.example.imdb.data.dto.MovieSearchResponse
import com.example.imdb.domain.api.MoviesRepository
import com.example.imdb.domain.models.Movie
import com.example.imdb.util.Resource

class MoviesRepositoryImpl(
    private val networkClient: NetworkClient,
    private val localStorage: LocalStorage
) : MoviesRepository {

    override fun searchMovies(expression: String): Resource<List<Movie>> {
        val response = networkClient.doRequest(MovieSearchRequest(expression))
        //можно тут трансформировать данные для передачи в domain-слой если надо

        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Отсутствет подключение к интернету")
            }

            200 -> {
                val stored = localStorage.getSavedFavorites()
                val foundMovies = (response as MovieSearchResponse).results
                if (foundMovies.isNotEmpty()) {
                    Resource.Success(foundMovies.map {
                        Movie(it.id, it.image, it.title,
                            it.description,
                            stored.contains(it.id)
                        )
                    })
                } else Resource.Error("Ничего не найдено")
            }

            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }

    override fun addMovieToFavorites(movie: Movie) {
        localStorage.addToFavorites(movie.id)
    }

    override fun removeMovieFromFavorites(movie: Movie) {
        localStorage.removeFromFavorites(movie.id)
    }

    override fun getFavoritesMovies(): Set<String> {
        return localStorage.getSavedFavorites()
    }
}