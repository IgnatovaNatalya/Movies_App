package com.example.imdb.viewmodel

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.imdb.domain.api.MoviesInteractor
import com.example.imdb.domain.models.Name
import com.example.imdb.ui.movies.ToastState
import com.example.imdb.ui.names.NamesState

class NamesSearchViewModel(
    private val moviesInteractor: MoviesInteractor
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

    private val handler = Handler(Looper.getMainLooper())

    private val _stateLiveData = MutableLiveData<NamesState>()
    val namesState: LiveData<NamesState> = _stateLiveData

    private val toastState = MutableLiveData<ToastState>(ToastState.None)
    fun observeToastState(): LiveData<ToastState> = toastState

    private var latestQueryText: String? = null

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun loadNamesDebounce(changedText: String) {
        if (latestQueryText == changedText) return
        latestQueryText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        val searchRunnable = Runnable { loadNames(changedText) }
        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(searchRunnable, SEARCH_REQUEST_TOKEN, postTime)
    }

    private fun loadNames(newQueryText: String) {
        if (newQueryText.isNotEmpty()) {
            renderState(NamesState.Loading)
            moviesInteractor.searchNames(newQueryText, object : MoviesInteractor.NamesConsumer {
                override fun consume(foundNames: List<Name>?, errorMessage: String?) {

                    if (foundNames != null) {
                        if (foundNames.isNotEmpty())
                            renderState(NamesState.Content(foundNames))
                        else if (errorMessage != null)
                            renderState(NamesState.Empty(errorMessage.toString()))
                    } else {
                        renderState(NamesState.Error("Что-то пошло не так"))
                        showToast(ToastState.Show(errorMessage.toString()))
                    }
                }
            })
        }
    }

    private fun renderState(state: NamesState) {
        _stateLiveData.postValue(state)
    }

    fun showToast(toast: ToastState) {
        toastState.postValue(toast)
    }

    fun toastWasShown() {
        toastState.value = ToastState.None
    }

}