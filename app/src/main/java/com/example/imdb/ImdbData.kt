package com.example.imdb

data class MovieResponse(
    val searchType:String,
    val expression:String,
    val results: ArrayList<Movie>,
    val errorMessage: String
)

data class Movie(
    val id:String,
    val resultType : String,
    val image:String,
    val title: String,
    val description: String
)