package com.example.mymovie.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ResponseUserItem(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("age")
    val age: String,
    @SerializedName("address")
    val address: String
) : Serializable