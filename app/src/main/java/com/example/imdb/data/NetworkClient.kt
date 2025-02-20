package com.example.imdb.data

import com.example.imdb.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response

}