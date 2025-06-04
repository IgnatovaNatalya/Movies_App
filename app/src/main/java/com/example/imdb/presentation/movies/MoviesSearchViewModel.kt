package com.example.imdb.presentation.movies

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imdb.R
import com.example.imdb.domain.search.MoviesInteractor
import com.example.imdb.domain.models.Movie
import com.example.imdb.util.debounce
import kotlinx.coroutines.launch

class MoviesSearchViewModel(
    private val context: Context,
    private val moviesInteractor: MoviesInteractor
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

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

    private val showToast = SingleLiveEvent<String?>()

    fun observeShowToast(): LiveData<String?> = showToast

    fun observeState(): LiveData<MoviesState> = mediatorStateLiveData

    private var latestQueryText: String? = null

    private val movieSearchDebounce = debounce<String>(
        SEARCH_DEBOUNCE_DELAY,
        viewModelScope,
        true
    ) { request -> loadMovies(request) }

    fun loadMoviesDebounce(changedText: String) {

        if (latestQueryText == changedText) return

        latestQueryText = changedText
        movieSearchDebounce(changedText)
    }

    private fun loadMovies(newQueryText: String) {
        if (newQueryText.isNotEmpty()) {
            renderState(MoviesState.Loading)
            viewModelScope.launch {
                moviesInteractor
                    .searchMovies(newQueryText)
                    .collect { pair -> processResult(pair.first, pair.second) }
            }
        }
    }

    private fun processResult(foundMovies: List<Movie>?, errorMessage: String?) {
        val movies = mutableListOf<Movie>()

        if (foundMovies != null) {
            movies.addAll(foundMovies)
        }

        when {
            errorMessage != null -> {
                renderState(
                    MoviesState.Error(
                        errorMessage = context.getString(R.string.something_went_wrong)
                    )
                )
                showToast.postValue(errorMessage)
            }

            movies.isEmpty() -> {
                renderState(MoviesState.Empty(message = context.getString(R.string.nothing_found)))
            }

            else -> {
                renderState(MoviesState.Content(movies = movies))
            }
        }
    }

    private fun renderState(state: MoviesState) {
        stateLiveData.postValue(state)
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