package com.example.imdb.ui.moviecast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.databinding.ItemCastBinding
import com.example.imdb.domain.models.CastPerson

class CastPersonAdapter: RecyclerView.Adapter<CastPersonViewHolder>() {

    var persons = listOf<CastPerson>()

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): CastPersonViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCastBinding.inflate(inflater, parent, false)
        return CastPersonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CastPersonViewHolder, position: Int) {
        holder.bind(persons[position])
    }

    override fun getItemCount(): Int = persons.size
}