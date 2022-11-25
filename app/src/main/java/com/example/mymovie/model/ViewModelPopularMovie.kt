package com.example.mymovie.model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mymovie.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelPopularMovie: ViewModel() {

    fun callApiPopularMovie(callback: (List<ResponsePopularMovieItem>) -> Unit) {
        ApiClient.instance.getPopularMovie()
            .enqueue(object : Callback<ResponsePopularMovie> {
                override fun onResponse(
                    call: Call<ResponsePopularMovie>,
                    response: Response<ResponsePopularMovie>
                ) {
                    if (response.isSuccessful) {
                        return callback(response.body()!!.results)
                    }
                }

                override fun onFailure(call: Call<ResponsePopularMovie>, t: Throwable) {
                    Log.e("Movie", "onFailure: ${t.message}", )
                }

            })
    }

}