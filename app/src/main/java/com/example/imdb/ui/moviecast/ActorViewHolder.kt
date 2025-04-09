package com.example.imdb.ui.moviecast

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.imdb.R
import com.example.imdb.databinding.ItemActorBinding
import com.example.imdb.domain.models.Actor

class ActorViewHolder(
    private val binding: ItemActorBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(actor: Actor) {
        binding.name.text = actor.name.toString()
        binding.asCharacter.text = actor.asCharacter.toString()

        Glide.with(binding.root)
            .load(actor.image)
            .placeholder(R.drawable.cover_blank)
            .centerInside()
            //.centerCrop()
            .transform(RoundedCorners(20))
            .into(binding.image)
    }
}