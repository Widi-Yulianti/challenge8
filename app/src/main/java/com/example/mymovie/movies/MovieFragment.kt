package com.example.mymovie.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.bumptech.glide.Glide
import com.example.mymovie.databinding.FragmentMovieBinding
import com.example.mymovie.model.FavoriteViewModel
import com.example.mymovie.model.ResponsePopularMovieItem
import com.example.mymovie.utils.GlideBlurTransformation
import com.example.mymovie.utils.UtilsPicture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieFragment : Fragment() {

    companion object {

        fun getFragmentTag(): String {
            return "MovieFragment"
        }
    }

    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FavoriteViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this
        ).get(FavoriteViewModel::class.java)
        toggleFavorite()

        if (arguments?.getSerializable("datadetail") != null) {
            val getDetailMovie = arguments?.getSerializable("datadetail") as ResponsePopularMovieItem
            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500" + getDetailMovie.moviePosterPath)
                .into(binding.moviePoster)
            binding.movieTitle.text = getDetailMovie.movieName
            binding.movieDescription.text = getDetailMovie.overview
            binding.movieRatingLabel.text = getDetailMovie.voteAverage.toString()
        }

    }

    fun toggleFavorite(){
        if (arguments?.getSerializable("datadetail") != null) {
            val getMovie = arguments?.getSerializable("datadetail") as ResponsePopularMovieItem
            val id = getMovie.id
            val movieName = getMovie.movieName
            val moviePosterPath = getMovie.moviePosterPath

            var _isChecked = false
            CoroutineScope(Dispatchers.IO).launch {
                val count = viewModel.checkUser(id)
                withContext(Dispatchers.Main){
                    if (count != null){
                        if (count > 0){
                            binding.btnFavorite.isChecked = true
                            _isChecked = true
                        } else {
                            binding.btnFavorite.isChecked = false
                            _isChecked = false
                        }
                    }
                }
            }

            binding.btnFavorite.setOnClickListener{
                _isChecked = !_isChecked
                if (_isChecked) {
                    viewModel.addToFavorite(id, movieName, moviePosterPath.toString())
                } else {
                    viewModel.removeFromFavorite(id, movieName, moviePosterPath.toString())
                }
                binding.btnFavorite.isChecked = _isChecked
            }
        }

    }
}