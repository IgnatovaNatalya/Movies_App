package com.example.imdb.data.dto

class MovieSearchResponse(
    val searchType: String,
    val expression: String,
    val results: List<MovieDto>
) : Response()