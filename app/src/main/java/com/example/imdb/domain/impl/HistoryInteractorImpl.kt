package com.example.imdb.domain.impl

import com.example.imdb.domain.history.HistoryInteractor
import com.example.imdb.domain.history.HistoryRepository
import com.example.imdb.domain.models.Movie
import kotlinx.coroutines.flow.Flow

class HistoryInteractorImpl(
    private val historyRepository: HistoryRepository
) : HistoryInteractor {

    override fun historyMovies(): Flow<List<Movie>> {
        return historyRepository.historyMovies()
    }
}