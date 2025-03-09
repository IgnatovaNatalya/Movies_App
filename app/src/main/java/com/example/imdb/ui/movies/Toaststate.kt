package com.example.imdb.ui.movies

sealed interface ToastState {
    object None: ToastState
    data class Show(val additionalMessage: String): ToastState
}