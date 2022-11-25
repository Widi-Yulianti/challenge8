package com.example.mymovie.network

import com.example.mymovie.model.ResponsePopularMovie
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("/3/movie/popular?api_key=936f5f6c2a6d04c4cbf7d4b54e9e5657")
    fun getPopularMovie(): Call<ResponsePopularMovie>

}