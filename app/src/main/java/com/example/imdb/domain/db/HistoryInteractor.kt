package com.example.imdb.domain.db

import com.example.imdb.domain.models.Movie
import kotlinx.coroutines.flow.Flow

interface HistoryInteractor {

    fun historyMovies(): Flow<List<Movie>>
}