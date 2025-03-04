package com.example.imdb.presentation.movies

import com.example.imdb.domain.models.Movie

interface MoviesView {
    // Сюда мы будем добавлять методы
    // для взаимодействия View и Presenter

    fun showPlaceholderMessage(isVisible: Boolean)

    fun setPlaceHolderMessage(message:String)

    fun showMoviesList(isVisible: Boolean)

    fun showProgressBar(isVisible: Boolean)

    fun updateMoviesList(newMoviesList: List<Movie>)

    fun showToast(message: String)

}