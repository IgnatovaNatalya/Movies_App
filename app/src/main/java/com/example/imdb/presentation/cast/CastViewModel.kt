package com.example.imdb.presentation.cast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.imdb.domain.api.MoviesInteractor
import com.example.imdb.domain.models.MovieCast
import com.example.imdb.ui.cast.MovieCastState

class CastViewModel(
    private val movieId: String,
    private val moviesInteractor: MoviesInteractor,
) : ViewModel() {

    private val _stateLiveData = MutableLiveData<MovieCastState>()
    val movieCastState: LiveData<MovieCastState> = _stateLiveData

    init {
        searchMovieCast(movieId)
    }

    fun searchMovieCast(movieId: String) {
        renderState(MovieCastState.Loading)
         moviesInteractor.searchMovieCast(movieId, object : MoviesInteractor.MovieCastConsumer {
            override fun consume(movieCast: MovieCast?, errorMessage: String?) {
                if (movieCast != null)
                    renderState(castToUiStateContent(movieCast))
                    //renderState(MovieCastState.Content(movieCast))
                else
                    renderState(MovieCastState.Error(errorMessage.toString()))
            }
        })
    }

    private fun castToUiStateContent(cast: MovieCast): MovieCastState {

        val items = buildList<MovieCastRVItem> {
            if (cast.directors.isNotEmpty()) {
                this += MovieCastRVItem.HeaderItem("Режиссеры")
                this += cast.directors.map { MovieCastRVItem.PersonItem(it) }
            }

            if (cast.writers.isNotEmpty()) {
                this += MovieCastRVItem.HeaderItem("Сценаристы")
                this += cast.writers.map { MovieCastRVItem.PersonItem(it) }
            }

            if (cast.actors.isNotEmpty()) {
                this += MovieCastRVItem.HeaderItem("Актеры")
                this += cast.actors.map { MovieCastRVItem.PersonItem(it) }
            }

            if (cast.others.isNotEmpty()) {
                this += MovieCastRVItem.HeaderItem("Другие")
                this += cast.others.map { MovieCastRVItem.PersonItem(it) }
            }
        }


        return MovieCastState.Content(
            fullTitle = cast.fullTitle,
            items = items
        )
    }

    private fun renderState(state: MovieCastState) {
        _stateLiveData.postValue(state)
    }
}