package com.example.imdb.data.network

import com.example.imdb.data.dto.MovieSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ImdbApiService {
    @GET("en/API/SearchMovie/k_zcuw1ytf/{request}")
    fun getMovies(@Path("request") request: String): Call<MovieSearchResponse>
}