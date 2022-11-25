package com.example.mymovie.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymovie.R
import com.example.mymovie.databinding.FragmentMoviesBinding
import com.example.mymovie.datastore.DataStoreManager
import com.example.mymovie.model.LoginViewModel
import com.example.mymovie.model.ResponsePopularMovieItem
import com.example.mymovie.model.ViewModelFactory
import com.example.mymovie.model.ViewModelPopularMovie
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase


class MoviesFragment : Fragment() {

    companion object {
        const val NUMBER_OF_ITEMS_TO_TRIGGER_PAGINATION = 10

        fun getFragmentTag(): String {
            return "MoviesFragment"
        }
    }

    private lateinit var pref: DataStoreManager
    private lateinit var viewModelLoginPref: LoginViewModel
    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!
    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        analytics = Firebase.analytics

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = DataStoreManager(this.requireActivity())
        viewModelLoginPref = ViewModelProvider(this, ViewModelFactory(pref))[LoginViewModel::class.java]
        viewModelLoginPref.getUser().observe(this.requireActivity(),{
            binding.idWelcome.text = "Welcome, " + it.name
        })
        showDataMoviePopoular()

        binding.btnFavorite.setOnClickListener {
            this.findNavController().navigate(R.id.action_moviesFragment_to_hilt_FavoriteFragment)
        }

        binding.btnProfile.setOnClickListener {
            this.findNavController().navigate(R.id.action_moviesFragment_to_hilt_ProfileFragment)
        }

        binding.btnSave.setOnClickListener {
            throw RuntimeException("Test Crash")
        }

    }


    fun showDataMoviePopoular() {
        val viewModel = ViewModelProvider(this).get(ViewModelPopularMovie::class.java)
        viewModel.callApiPopularMovie{movies: List<ResponsePopularMovieItem> ->
            binding.moviesRecyclerView.adapter = MovieAdapter(movies)
        }
        binding.moviesRecyclerView.layoutManager = LinearLayoutManager(this.requireActivity(),LinearLayoutManager.HORIZONTAL,false)
        binding.moviesRecyclerView.setHasFixedSize(true)
    }

}