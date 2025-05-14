package com.example.imdb.presentation.names

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imdb.R
import com.example.imdb.databinding.ItemNameBinding
import com.example.imdb.domain.models.Name

class NameViewHolder(
    private val parent: ViewGroup,
    private val binding: ItemNameBinding
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(name: Name) {
        binding.title.text = name.title
        binding.description.text = name.description

        Glide.with(binding.root)
            .load(name.image)
            .placeholder(R.drawable.cover_blank)
            .centerInside()
            .circleCrop()
            .into(binding.image)
    }
}