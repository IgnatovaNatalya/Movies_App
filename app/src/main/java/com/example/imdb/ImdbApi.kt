package com.example.imdb

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ImdbApi {
    @GET("en/API/SearchMovie/k_zcuw1ytf/{request}")
    fun getMovies(@Path("request") request: String): Call<MovieResponse>
}