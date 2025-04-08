package com.example.imdb.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PosterViewModel(private val posterUrl: String) : ViewModel() {

    private val _posterLiveData = MutableLiveData<String>()

    init {
        _posterLiveData.postValue(posterUrl)
    }

    val posterLiveData: LiveData<String> = _posterLiveData

}