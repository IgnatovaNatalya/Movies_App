package com.example.imdb.domain.api

import com.example.imdb.domain.models.Movie
import com.example.imdb.domain.models.MovieCast
import com.example.imdb.domain.models.MovieDetails
import com.example.imdb.util.Resource

interface MoviesRepository {
    fun searchMovies(expression: String): Resource<List<Movie>>
    fun searchMovieDetails(movie_id: String): Resource<MovieDetails>
    fun searchMovieCast(movie_id: String): Resource<MovieCast>

    fun addMovieToFavorites(movieId:String)
    fun removeMovieFromFavorites(movieId:String)
    fun getFavoritesMovies(): Set<String>
}