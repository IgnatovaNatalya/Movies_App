package com.example.imdb.presentation.movies

import com.example.imdb.domain.models.Movie
import com.example.imdb.ui.movies.SearchMoviesState

interface MoviesView {

    fun updateMoviesList(newMoviesList: List<Movie>)

    fun showToast(message: String)

    fun render(state: SearchMoviesState)
}