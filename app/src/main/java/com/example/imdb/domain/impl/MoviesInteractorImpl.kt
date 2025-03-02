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
}