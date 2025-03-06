package com.example.imdb.presentation.movies

import com.example.imdb.ui.movies.SearchMoviesState
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface MoviesView : MvpView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showToast(message: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun render(state: SearchMoviesState)
}