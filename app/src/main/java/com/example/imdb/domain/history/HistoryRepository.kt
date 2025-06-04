package com.example.imdb.domain.history

import com.example.imdb.domain.models.Movie
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {

    fun historyMovies(): Flow<List<Movie>>

}