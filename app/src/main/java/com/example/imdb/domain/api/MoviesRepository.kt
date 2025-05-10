package com.example.imdb.domain.api

import com.example.imdb.domain.models.Movie
import com.example.imdb.domain.models.Name
import com.example.imdb.domain.models.MovieCast
import com.example.imdb.domain.models.MovieDetails
import com.example.imdb.util.Resource
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    fun searchMovies(expression: String): Resource<List<Movie>>
    //fun searchNames(expression: String): Resource<List<Name>>
    fun searchNames(expression: String): Flow<Resource<List<Name>>>
    fun searchMovieDetails(movieId: String): Resource<MovieDetails>
    fun searchMovieCast(movieId: String): Resource<MovieCast>

    fun addMovieToFavorites(movieId:String)
    fun removeMovieFromFavorites(movieId:String)
    fun getFavoritesMovies(): Set<String>
}