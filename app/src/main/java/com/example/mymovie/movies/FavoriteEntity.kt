package com.example.mymovie.movies

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class FavoriteEntity(
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey
    var id: Int,

    @field:ColumnInfo(name = "name")
    val movieName: String,

    @field:ColumnInfo(name = "poster_path")
    val moviePosterPath: String,

): Parcelable
