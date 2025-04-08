package com.example.imdb.data.network

import com.example.imdb.data.dto.MovieCastResponse
import com.example.imdb.data.dto.MovieDetailsResponse
import com.example.imdb.data.dto.MovieSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ImdbApiService {
    @GET("en/API/SearchMovie/k_zcuw1ytf/{request}")
    fun getMovies(@Path("request") request: String): Call<MovieSearchResponse>

    @GET("/en/API/Title/k_zcuw1ytf/{movie_id}")
    fun getMovieDetails(@Path("movie_id") movieId: String): Call<MovieDetailsResponse>

    @GET("/en/API/FullCast/k_zcuw1ytf/{movie_id")
    fun getMovieCast(@Path("movie_id") movieId: String): Call<MovieCastResponse>
}