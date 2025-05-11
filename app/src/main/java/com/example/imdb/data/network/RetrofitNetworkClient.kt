package com.example.imdb.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.imdb.data.NetworkClient
import com.example.imdb.data.dto.MovieCastRequest
import com.example.imdb.data.dto.MovieDetailsRequest
import com.example.imdb.data.dto.MovieSearchRequest
import com.example.imdb.data.dto.NamesSearchRequest
import com.example.imdb.data.dto.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(private val context: Context, private val apiService: ImdbApiService) :
    NetworkClient {

    override fun doRequest(dto: Any): Response {
        if (!isConnected()) return Response().apply { resultCode = -1 }

        val resp = when (dto) {
            is MovieDetailsRequest -> apiService.getMovieDetails(dto.movieId).execute()
            else -> apiService.getMovieCast((dto as MovieCastRequest).movieId).execute()
        }

        val body = resp.body()

        return body?.apply { resultCode = resp.code() } ?: Response().apply {
            resultCode = resp.code()
        }
    }

    override suspend fun doRequestSuspend(dto: Any): Response {
        if (!isConnected()) return Response().apply { resultCode = -1 }

        return withContext(Dispatchers.IO) {
            try {
                val response = when (dto) {
                    is MovieSearchRequest -> apiService.getMovies(dto.expression)
                    is NamesSearchRequest -> apiService.getNames(dto.expression)
                    else -> null
                }
                response?.apply { resultCode = 200 } ?: Response().apply { resultCode = 500 }
            } catch (e: Throwable) {
                Response().apply { resultCode = 500 }
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}