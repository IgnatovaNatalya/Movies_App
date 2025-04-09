package com.example.imdb.presentation

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.imdb.domain.api.MoviesInteractor
import com.example.imdb.domain.models.Movie
import com.example.imdb.ui.movies.MoviesState
import com.example.imdb.ui.movies.ToastState

class MoviesSearchViewModel(
    private val moviesInteractor: MoviesInteractor
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

    private val handler = Handler(Looper.getMainLooper())

    private val stateLiveData = MutableLiveData<MoviesState>()

    private val mediatorStateLiveData = MediatorLiveData<MoviesState>().also { liveData ->
        liveData.addSource(stateLiveData) { movieState ->
            liveData.value = when (movieState) {
                is MoviesState.Content -> MoviesState.Content(movieState.movies.sortedByDescending { it.inFavorite })
                is MoviesState.Empty -> movieState
                is MoviesState.Error -> movieState
                is MoviesState.Loading -> movieState
            }
        }
    }

    fun observeState(): LiveData<MoviesState> = mediatorStateLiveData

    private val toastState = MutableLiveData<ToastState>(ToastState.None)
    fun observeToastState(): LiveData<ToastState> = toastState

    private var latestQueryText: String? = null

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun loadMoviesDebounce(changedText: String) {
        if (latestQueryText == changedText) return
        latestQueryText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        val searchRunnable = Runnable { loadMovies(changedText) }
        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(searchRunnable, SEARCH_REQUEST_TOKEN, postTime)
    }

    private fun loadMovies(newQueryText: String) {
        if (newQueryText.isNotEmpty()) {
            renderState(MoviesState.Loading)
            moviesInteractor.searchMovies(newQueryText, object : MoviesInteractor.MoviesConsumer {
                override fun consume(foundMovies: List<Movie>?, errorMessage: String?) {

                    if (foundMovies != null) {
                        if (foundMovies.isNotEmpty())
                            renderState(MoviesState.Content(foundMovies))
                        else if (errorMessage != null)
                            renderState(MoviesState.Empty(errorMessage.toString()))
                    } else {
                        renderState(MoviesState.Error("Что-то пошло не так"))
                        showToast(ToastState.Show(errorMessage.toString()))
                    }
                }
            })
        }
    }

    private fun renderState(state: MoviesState) {
        stateLiveData.postValue(state)
    }

    fun showToast(toast: ToastState) {
        toastState.postValue(toast)
    }

    fun toastWasShown() {
        toastState.value = ToastState.None
    }

    fun toggleFavorite(movie: Movie) {

        if (movie.inFavorite) {
            moviesInteractor.removeMovieFromFavorites(movie.id)
        } else {
            moviesInteractor.addMovieToFavorites(movie.id)
        }
        updateMovieContent(movie.id, movie.copy(inFavorite = !movie.inFavorite))
    }

    private fun updateMovieContent(movieId: String, newMovie: Movie) {
        val currentState = stateLiveData.value

        if (currentState is MoviesState.Content) {
            val movieIndex = currentState.movies.indexOfFirst { it.id == movieId }

            if (movieIndex != -1) {
                stateLiveData.value = MoviesState.Content(
                    currentState.movies.toMutableList().also {
                        it[movieIndex] = newMovie
                    }
                )
            }
        }
    }

    fun refreshFavoriteMovies() {
        val currentState = stateLiveData.value
        if (currentState is MoviesState.Content) {
            val favoritesSet = moviesInteractor.getFavoritesMovies()
            for (movie in currentState.movies) {
                if (movie.id in favoritesSet) updateMovieContent(
                    movie.id,
                    movie.copy(inFavorite = true)
                )
                else updateMovieContent(movie.id, movie.copy(inFavorite = false))
            }
        }
    }
}