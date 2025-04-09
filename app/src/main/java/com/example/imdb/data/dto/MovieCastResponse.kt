package com.example.imdb.data.dto

import com.example.imdb.domain.models.CastJob
import com.example.imdb.domain.models.Actor

class MovieCastResponse(
    val imDbId:String,
    val title: String,
    val directors: CastJob,
    val writers: CastJob,
    val actors: List<Actor>,
    val others: List<CastJob>
) : Response()

