package com.example.imdb.domain.db

import com.example.imdb.domain.models.Movie
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {

    fun historyMovies(): Flow<List<Movie>>

}