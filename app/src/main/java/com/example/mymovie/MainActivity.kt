package com.example.mymovie

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mymovie.movies.MovieFragment
import com.example.mymovie.movies.MoviesFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @SuppressLint("all")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MoviesFragment())
                .commit()
        }

//        Toast.makeText(this, BuildConfig.BASE_URL, Toast.LENGTH_LONG).show()

    }


    fun navigateMovieView() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, MovieFragment())
            .addToBackStack(MovieFragment.getFragmentTag())
            .commitAllowingStateLoss()
    }

}