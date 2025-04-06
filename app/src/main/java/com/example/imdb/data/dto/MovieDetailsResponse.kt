package com.example.imdb.data.dto

import com.example.imdb.domain.models.MovieDetails


class MovieDetailsResponse(
    val searchType: String,
    val expression: String,
    val results: MovieDetails
) : Response()
