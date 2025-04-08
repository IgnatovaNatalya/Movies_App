package com.example.imdb.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.imdb.ui.cast.MovieCastState

class CastViewModel( private val movieId: String): ViewModel() {

    private val _stateLiveData = MutableLiveData<MovieCastState>()
    val movieCastState: LiveData<MovieCastState> = _stateLiveData
}