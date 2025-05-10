package com.example.imdb.domain.api

import com.example.imdb.domain.models.Movie
import com.example.imdb.domain.models.MovieCast
import com.example.imdb.domain.models.MovieDetails
import com.example.imdb.domain.models.Name
import kotlinx.coroutines.flow.Flow

interface MoviesInteractor {
    fun searchMovies(expression: String, consumer: MoviesConsumer)

    interface MoviesConsumer {
        fun consume(foundMovies: List<Movie>?, errorMessage: String?)
    }

    fun addMovieToFavorites(movieId:String)
    fun removeMovieFromFavorites(movieId:String)
    fun getFavoritesMovies():Set<String>


    fun searchMovieDetails(movieId:String, consumer: MovieDetailsConsumer)

    interface MovieDetailsConsumer {
        fun consume(movieDetails: MovieDetails?, errorMessage: String?)
    }


    fun searchMovieCast(movieId:String, consumer: MovieCastConsumer)

    interface MovieCastConsumer {
        fun consume(movieCast: MovieCast?, errorMessage: String?)
    }

//
//    fun searchNames(expression: String, consumer: NamesConsumer)
//
//    interface NamesConsumer {
//        fun consume(foundNames: List<Name>?, errorMessage: String?)
//    }

    fun searchNames(expression: String): Flow<Pair<List<Name>?, String?>>

}