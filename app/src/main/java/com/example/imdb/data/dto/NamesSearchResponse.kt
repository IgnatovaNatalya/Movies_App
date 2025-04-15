package com.example.imdb.data.dto

class NamesSearchResponse(
    val searchType: String,
    val expression: String,
    val results: List<NamesDto>
) : Response()