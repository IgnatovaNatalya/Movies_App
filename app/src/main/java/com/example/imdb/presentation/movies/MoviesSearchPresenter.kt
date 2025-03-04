package com.example.imdb.presentation.movies

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.widget.Toast
import com.example.imdb.domain.api.MoviesInteractor
import com.example.imdb.domain.models.Movie
import com.example.imdb.util.Creator

class MoviesSearchPresenter(
    private val view: MoviesView,
    private val context: Context
) {

    private var moviesInteractor = Creator.provideMoviesInteractor(context)

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

    private val handler = Handler(Looper.getMainLooper())

    fun loadMoviesDebounce(changedText: String) {

        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { loadMovies(changedText) }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            handler.postDelayed(
                searchRunnable,
                SEARCH_REQUEST_TOKEN,
                SEARCH_DEBOUNCE_DELAY
            )
        } else {
            val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
            handler.postAtTime(
                searchRunnable,
                SEARCH_REQUEST_TOKEN,
                postTime,
            )
        }
    }

    private fun loadMovies(newQueryText: String) {
        if (newQueryText.isNotEmpty()) {
            view.showPlaceholderMessage(false)
            view.showMoviesList(false)
            view.showProgressBar(true)

            moviesInteractor.searchMovies(
                newQueryText,
                object : MoviesInteractor.MoviesConsumer {
                    override fun consume(foundMovies: List<Movie>?, errorMessage: String?) {
                        handler.post {
                            view.showProgressBar(false)
                            if (foundMovies != null) {
                                view.updateMoviesList(foundMovies)
                                view.showMoviesList(true)
                            } else {
                                if (errorMessage != null) {
                                    showMessage(errorMessage, errorMessage)
                                }
                            }
                        }
                    }
                })
        }
    }

    fun onDestroy() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    private fun showMessage(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) {
            view.showPlaceholderMessage(true)
            view.showMoviesList(false)
            view.setPlaceHolderMessage(text)

            if (additionalMessage.isNotEmpty()) view.showToast(additionalMessage)

        } else {
            view.showPlaceholderMessage(false)
            view.showMoviesList(true)
        }
    }

    private fun hideMessage() {
        view.showPlaceholderMessage(false)
        view.showMoviesList(true)
    }
}