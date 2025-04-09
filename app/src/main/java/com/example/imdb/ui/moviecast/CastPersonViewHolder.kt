package com.example.imdb.ui.moviecast

import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.databinding.ItemCastBinding
import com.example.imdb.domain.models.MovieCastPerson

class CastPersonViewHolder(
    private val binding: ItemCastBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(person: MovieCastPerson) {
        binding.name.text = person.name.toString()
        binding.description.text = person.description.toString()
    }
}