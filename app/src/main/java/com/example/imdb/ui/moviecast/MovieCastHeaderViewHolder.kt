package com.example.imdb.ui.moviecast

import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.databinding.ItemCastHeaderBinding
import com.example.imdb.presentation.cast.MovieCastRVItem

class MovieCastHeaderViewHolder(private val binding: ItemCastHeaderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: MovieCastRVItem.HeaderItem) {
        binding.header.text = item.headerText
    }

}