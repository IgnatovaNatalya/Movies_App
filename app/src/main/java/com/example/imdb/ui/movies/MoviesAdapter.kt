package com.example.imdb.ui.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.domain.models.Movie
import com.example.imdb.databinding.ItemMovieBinding

class MoviesAdapter(val clickListener: MovieClickListener) : RecyclerView.Adapter<MovieViewHolder> () {

    var movies = listOf<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return MovieViewHolder(ItemMovieBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies.get(position))
        holder.itemView.setOnClickListener { clickListener.onMovieClick(movies[position]) }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun interface MovieClickListener {
        fun onMovieClick(movie: Movie)
    }
}