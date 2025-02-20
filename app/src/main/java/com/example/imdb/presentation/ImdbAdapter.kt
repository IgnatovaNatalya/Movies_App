package com.example.imdb.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.R
import com.example.imdb.domain.models.Movie

class ImdbAdapter(val clickListener: MovieClickListener) : RecyclerView.Adapter<ImdbViewHolder> () {

    var movies = ArrayList<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImdbViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return ImdbViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImdbViewHolder, position: Int) {
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