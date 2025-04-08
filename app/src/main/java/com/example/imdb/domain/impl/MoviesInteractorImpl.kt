package com.example.imdb.domain.impl

import com.example.imdb.domain.api.MoviesInteractor
import com.example.imdb.domain.api.MoviesRepository
import com.example.imdb.util.Resource
import java.util.concurrent.Executors

class MoviesInteractorImpl(private val repository: MoviesRepository) : MoviesInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchMovies(expression: String, consumer: MoviesInteractor.MoviesConsumer) {
        executor.execute {
            //здесь можно осортировать и отфильтровать результаты если надо
            when (val resource = repository.searchMovies(expression)) {
                is Resource.Success -> {
                    consumer.consume(resource.data, null)
                }

                is Resource.Error -> {
                    consumer.consume(null, resource.message)
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