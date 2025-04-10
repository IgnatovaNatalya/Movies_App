package com.example.imdb.ui.moviecast

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imdb.databinding.ItemCastBinding
import com.example.imdb.presentation.cast.MovieCastRVItem

class MovieCastViewHolder(private val binding: ItemCastBinding) :
    RecyclerView.ViewHolder(binding.root
    ) {

    fun bind(movieCastPerson: MovieCastRVItem.PersonItem) {
        if (movieCastPerson.data.image == null) {
            binding.image.isVisible = false
        } else {
            Glide.with(itemView)
                .load(movieCastPerson.data.image)
                .into(binding.image)
            binding.image.isVisible = true
        }

        binding.name.text = movieCastPerson.data.name
        binding.description.text = movieCastPerson.data.description
    }
}