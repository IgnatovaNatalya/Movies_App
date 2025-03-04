package com.example.imdb.presentation.movies

import com.example.imdb.domain.models.Movie

interface MoviesView {

    fun updateMoviesList(newMoviesList: List<Movie>)

    fun showToast(message: String)

    fun showLoading()
    fun showError(errorMessage: String)
    fun showEmpty(emptyMessage: String)
    fun showContent(movies: List<Movie>)

}