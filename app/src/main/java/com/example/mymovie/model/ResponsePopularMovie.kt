package com.example.mymovie.model


import com.google.gson.annotations.SerializedName
import java.util.*


data class ResponsePopularMovie(
    @SerializedName("results")
    val results: List<ResponsePopularMovieItem>
)