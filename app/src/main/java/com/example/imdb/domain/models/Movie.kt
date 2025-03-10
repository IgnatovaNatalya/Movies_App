package com.example.imdb.domain.models

data class Movie(
    val id:String,
    val image:String,
    val title: String,
    val description: String,
    val inFavorite:Boolean,
)