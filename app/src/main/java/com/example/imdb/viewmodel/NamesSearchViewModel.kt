package com.example.imdb.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.imdb.domain.api.MoviesInteractor
import com.example.imdb.domain.models.Name
import com.example.imdb.ui.movies.ToastState
import com.example.imdb.ui.names.NamesState
import kotlinx.coroutines.Job

class NamesSearchViewModel(
    private val moviesInteractor: MoviesInteractor
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private val _stateLiveData = MutableLiveData<NamesState>()
    val namesState: LiveData<NamesState> = _stateLiveData

    private val toastState = MutableLiveData<ToastState>(ToastState.None)
    fun observeToastState(): LiveData<ToastState> = toastState

    private var latestQueryText: String? = null
    private var searchJob: Job? = null

    fun loadNamesDebounce(changedText: String) {
        if (latestQueryText == changedText) return

        latestQueryText = changedText
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            loadNames(changedText)
        }
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