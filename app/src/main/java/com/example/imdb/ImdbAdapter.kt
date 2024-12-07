package com.example.imdb

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ImdbAdapter : RecyclerView.Adapter<ImdbViewHolder> () {

    var movies = ArrayList<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImdbViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return ImdbViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImdbViewHolder, position: Int) {
        holder.bind(movies.get(position))
    }

    override fun getItemCount(): Int {
        return movies.size
    }

}