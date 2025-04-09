package com.example.imdb.domain.models

data class MovieCast(
    val imDbId:String,
    val title: String,
    val directors: CastJob?,
    val writers: CastJob?,
    val actors: List<Actor>,
    val others: List<CastJob>
)

class CastPerson(
    val id: String,
    val name: String,
    val description: String
)

class Actor(
    val id: String,
    val image: String,
    val name: String,
    val asCharacter: String
)

class CastJob(
    val job: String,
    val items: List<CastPerson>
)