package com.example.imdb.domain.models

data class MovieCast(
    val title: String,
    val directors: CastJob,
    val writer: CastJob,
    val actors: List<StarPerson>,
    val others: List<CastJob>
)

class CastPerson(
    val id: String,
    val name: String,
    val description: String
)

class StarPerson(
    val id: String,
    val image: String,
    val name: String,
    val asCharacter: String
)

class CastJob(
    val job: String,
    val items: List<CastPerson>
)