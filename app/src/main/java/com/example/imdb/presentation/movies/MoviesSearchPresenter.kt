package com.example.imdb.presentation.movies

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import com.example.imdb.R
import com.example.imdb.domain.api.MoviesInteractor
import com.example.imdb.domain.models.Movie
import com.example.imdb.ui.movies.SearchMoviesState
import com.example.imdb.util.Creator
import moxy.MvpPresenter

class MoviesSearchPresenter(
    private val context: Context
) : MvpPresenter<MoviesView>() {

    private var latestQueryText: String? = null

    private var moviesInteractor = Creator.provideMoviesInteractor(context)

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

    private val handler = Handler(Looper.getMainLooper())


    fun loadMoviesDebounce(changedText: String) {

        if (latestQueryText == changedText) return

        latestQueryText = changedText

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
            renderState(SearchMoviesState.Loading)

            moviesInteractor.searchMovies(
                newQueryText,
                object : MoviesInteractor.MoviesConsumer {
                    override fun consume(foundMovies: List<Movie>?, errorMessage: String?) {
                        handler.post {
                            if (foundMovies != null) {
                                if (foundMovies.isNotEmpty())
                                    renderState(SearchMoviesState.Content(foundMovies))
                                else if (errorMessage != null)
                                    renderState(SearchMoviesState.Empty(errorMessage.toString()))
                            } else {
                                renderState(SearchMoviesState.Error(context.getString(R.string.something_went_wrong)))
                                viewState?.showToast(errorMessage.toString())
                            }
                        }
                    }
                })
        }
    }

    private fun renderState(state: SearchMoviesState) {
        viewState.render(state)
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }
}