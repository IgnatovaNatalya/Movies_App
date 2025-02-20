package com.example.imdb.data.network

import com.example.imdb.data.NetworkClient
import com.example.imdb.data.dto.MovieSearchRequest
import com.example.imdb.data.dto.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient:NetworkClient {

    private val BASE_URL = "https://tv-api.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ImdbApiService::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is MovieSearchRequest) {
            val resp = apiService.getMovies(dto.expression).execute()

            val body = resp.body() ?: Response()

            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }

}