package com.example.imdb.domain.impl

import com.example.imdb.domain.search.MoviesInteractor
import com.example.imdb.domain.search.MoviesRepository
import com.example.imdb.domain.models.Movie
import com.example.imdb.domain.models.Name
import com.example.imdb.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.Executors

class MoviesInteractorImpl(private val repository: MoviesRepository) : MoviesInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchMovies(expression: String): Flow<Pair<List<Movie>?, String?>> {
        return repository.searchMovies (expression).map { result ->
            //здесь можно осортировать и отфильтровать результаты если надо
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }

    override fun searchNames(expression: String): Flow<Pair<List<Name>?, String?>> {
        return repository.searchNames(expression).map { result ->
            when(result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }

    override fun searchMovieDetails(
        movieId: String,
        consumer: MoviesInteractor.MovieDetailsConsumer
    ) {
        executor.execute {
            when (val resource = repository.searchMovieDetails(movieId)) {
                is Resource.Success -> {
                    consumer.consume(resource.data, null)
                }

                is Resource.Error -> {
                    consumer.consume(null, resource.message)
                }
            }
        }
    }

    override fun searchMovieCast(
        movieId: String,
        consumer: MoviesInteractor.MovieCastConsumer
    ) {
        executor.execute {
            when (val resource = repository.searchMovieCast(movieId)) {
                is Resource.Success -> {
                    consumer.consume(resource.data, null)
                }

                is Resource.Error -> {
                    consumer.consume(null, resource.message)
                }
            }
        }
    }


    override fun addMovieToFavorites(movieId:String) {
        repository.addMovieToFavorites(movieId)
    }

    override fun removeMovieFromFavorites(movieId:String) {
        repository.removeMovieFromFavorites(movieId)
    }

    override fun getFavoritesMovies(): Set<String> {
        return repository.getFavoritesMovies()
    }

}