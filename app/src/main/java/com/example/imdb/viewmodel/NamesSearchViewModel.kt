package com.example.imdb.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imdb.R
import com.example.imdb.domain.api.MoviesInteractor
import com.example.imdb.domain.models.Name
import com.example.imdb.ui.movies.SingleLiveEvent
import com.example.imdb.ui.names.NamesState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NamesSearchViewModel(
    private val context: Context,
    private val moviesInteractor: MoviesInteractor
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private val _stateLiveData = MutableLiveData<NamesState>()
    val namesState: LiveData<NamesState> = _stateLiveData

    private val showToast = SingleLiveEvent<String?>()
    fun observeShowToast(): LiveData<String?> = showToast

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

            viewModelScope.launch {
                moviesInteractor
                    .searchNames(newQueryText)
                    .collect { pair -> processResult(pair.first, pair.second) }
            }
        }
    }

    private fun processResult(foundNames: List<Name>?, errorMessage: String?) {
        val persons = mutableListOf<Name>()
        if (foundNames != null) {
            persons.addAll(foundNames)
        }

        when {
            errorMessage != null -> {
                renderState(
                    NamesState.Error(
                        errorMessage = context.getString(R.string.something_went_wrong)
                    )
                )
                showToast.postValue(errorMessage)
            }

            persons.isEmpty() -> {
                renderState(NamesState.Empty(message = context.getString(R.string.nothing_found)))
            }

            else -> {
                renderState(NamesState.Content(names = persons))
            }
        }
    }

    private fun renderState(state: NamesState) {
        _stateLiveData.postValue(state)
    }
}