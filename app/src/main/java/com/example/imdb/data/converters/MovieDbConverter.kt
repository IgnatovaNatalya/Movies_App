package com.example.imdb.data.converters

import com.example.imdb.data.db.entity.MovieEntity
import com.example.imdb.data.dto.MovieDto
import com.example.imdb.domain.models.Movie

class MovieDbConvertor {

    fun map(movie: MovieDto): MovieEntity {
        return MovieEntity(movie.id, movie.resultType, movie.image, movie.title, movie.description)
    }

    fun map(movie: MovieEntity): Movie {
        return Movie(movie.id, movie.image, movie.title, movie.description, false)
    }
}