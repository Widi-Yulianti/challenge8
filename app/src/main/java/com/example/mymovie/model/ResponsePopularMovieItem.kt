package com.example.mymovie.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*


data class ResponsePopularMovieItem(
    val id: Int,
    @SerializedName("poster_path") val moviePosterPath: String?,
    @SerializedName("name") val movieName: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("vote_count") val voteCount: Int,
    @SerializedName("vote_average") val voteAverage: Float,
    @SerializedName("first_air_date") val firstAirDate: Date?
): Serializable