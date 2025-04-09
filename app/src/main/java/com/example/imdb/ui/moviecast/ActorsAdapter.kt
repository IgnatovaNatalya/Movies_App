package com.example.imdb.ui.moviecast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.databinding.ItemActorBinding
import com.example.imdb.domain.models.Actor

class ActorsAdapter: RecyclerView.Adapter<ActorViewHolder>() {

    var actors = listOf<Actor>()

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): ActorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemActorBinding.inflate(inflater, parent, false)
        return ActorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        holder.bind(actors[position])
    }

    override fun getItemCount(): Int = actors.size
}