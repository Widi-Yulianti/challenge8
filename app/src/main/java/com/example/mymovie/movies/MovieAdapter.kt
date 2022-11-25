package com.example.mymovie.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymovie.R
import com.example.mymovie.databinding.ItemViewMovieBinding
import com.example.mymovie.model.ResponsePopularMovieItem

class MovieAdapter(var listMovie: List<ResponsePopularMovieItem>):
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    class ViewHolder(var binding: ItemViewMovieBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view =ItemViewMovieBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.movieContainer.setOnClickListener {
            var bundle = Bundle()
            bundle.putSerializable("datadetail",listMovie[position])
//            it.findNavController().navigate(R.id.action_moviesFragment_to_movieFragment, bundle)

        }
        holder.binding.movieTitle.text = listMovie[position].movieName
        Glide.with(holder.itemView)
            .load("https://image.tmdb.org/t/p/w500"+listMovie[position].moviePosterPath)
            .into(holder.binding.moviePoster)
        holder.binding.movieRatingLabel.text = listMovie[position].voteAverage.toString()
    }

    override fun getItemCount(): Int {
        return listMovie.size
    }

}