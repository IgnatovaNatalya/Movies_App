package com.example.imdb.data.dto

import com.example.imdb.domain.models.CastJob
import com.example.imdb.domain.models.StarPerson

class MovieCastResponse(
    val title: String,
    val directors: CastJob,
    val writer: CastJob,
    val actors: List<StarPerson>,
    val others: List<CastJob>

) : Response()

