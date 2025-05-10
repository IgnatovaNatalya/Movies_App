package com.example.imdb.data

import com.example.imdb.data.converter.MovieCastConverter
import com.example.imdb.data.dto.MovieCastRequest
import com.example.imdb.data.dto.MovieCastResponse
import com.example.imdb.data.dto.MovieDetailsRequest
import com.example.imdb.data.dto.MovieDetailsResponse
import com.example.imdb.data.dto.MovieSearchRequest
import com.example.imdb.data.dto.MovieSearchResponse
import com.example.imdb.data.dto.NamesSearchRequest
import com.example.imdb.data.dto.NamesSearchResponse
import com.example.imdb.domain.api.MoviesRepository
import com.example.imdb.domain.models.Movie
import com.example.imdb.domain.models.MovieCast
import com.example.imdb.domain.models.MovieDetails
import com.example.imdb.domain.models.Name
import com.example.imdb.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MoviesRepositoryImpl(
    private val networkClient: NetworkClient,
    private val localStorage: LocalStorage,
    private val movieCastConverter: MovieCastConverter,
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
                        Movie(
                            it.id, it.image, it.title,
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

    override fun searchNames(expression: String): Flow<Resource<List<Name>>> = flow {
        val response = networkClient.doRequest(NamesSearchRequest(expression))

        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }

            200 -> {
                with(response as NamesSearchResponse) {
                    val data = results.map {
                        Name(
                            it.id, it.image,
                            it.title,
                            it.description
                        )
                    }
                    emit(Resource.Success(data))
                }
            }

            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }

    override fun searchMovieDetails(movieId: String): Resource<MovieDetails> {
        val response = networkClient.doRequest(MovieDetailsRequest(movieId))

        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Отсутствет подключение к интернету")
            }

            200 -> {
                val stored = localStorage.getSavedFavorites()

                with((response as MovieDetailsResponse)) {
                    Resource.Success(
                        MovieDetails(
                            id = id,
                            title = title,
                            imDbRating = imDbRating,
                            year = year,
                            countries = countries,
                            genres = genres,
                            directors = directors,
                            writers = writers,
                            stars = stars,
                            plot = plot,
                            inFavorite = stored.contains(id)
                        )
                    )
                }
            }

            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }

    override fun searchMovieCast(movieId: String): Resource<MovieCast> {
        val response = networkClient.doRequest(MovieCastRequest(movieId))

        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Отсутствет подключение к интернету")
            }

            200 -> {
                    Resource.Success(
                        data = movieCastConverter.convert(response as MovieCastResponse)
                    )
            }

            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }

    override fun addMovieToFavorites(movieId: String) {
        localStorage.addToFavorites(movieId)
    }

    override fun removeMovieFromFavorites(movieId: String) {
        localStorage.removeFromFavorites(movieId)
    }

    override fun getFavoritesMovies(): Set<String> {
        return localStorage.getSavedFavorites()
    }
}