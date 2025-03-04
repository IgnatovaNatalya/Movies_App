package com.example.imdb.presentation.poster

class PosterPresenter(
    private val view: PosterView
) {

    fun onCreate() {
        val posterString = view.getPosterUrl()
        view.setImagePoster(posterString)
    }
}