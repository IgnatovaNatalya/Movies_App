package com.example.imdb.presentation.movies

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.imdb.R
import com.example.imdb.domain.api.MoviesInteractor
import com.example.imdb.domain.models.Movie
import com.example.imdb.ui.movies.MoviesState
import com.example.imdb.ui.movies.ToastState
import com.example.imdb.util.Creator

class MoviesSearchViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MoviesSearchViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }

    private var moviesInteractor = Creator.provideMoviesInteractor(getApplication())
    private val handler = Handler(Looper.getMainLooper())

    private val stateLiveData = MutableLiveData<MoviesState>()
    fun observeState(): LiveData<MoviesState> = stateLiveData

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
                            renderState(
                                MoviesState.Error(
                                    getApplication<Application>().getString(R.string.something_went_wrong)
                                )
                            )
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


}