package com.example.imdb.ui.moviecast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.R
import com.example.imdb.databinding.ItemCastHeaderBinding
import com.example.imdb.databinding.ItemCastBinding
import com.example.imdb.presentation.cast.MovieCastRVItem

class MoviesCastAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items = emptyList<MovieCastRVItem>()

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is MovieCastRVItem.HeaderItem -> R.layout.item_cast_header
            is MovieCastRVItem.PersonItem -> R.layout.item_cast
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        when (viewType) {
            R.layout.item_cast_header -> {
                val binding = ItemCastHeaderBinding.inflate(inflater, parent, false)
                return MovieCastHeaderViewHolder(binding)
            }

            R.layout.item_cast -> {
                val binding = ItemCastBinding.inflate(inflater, parent, false)
                return MovieCastViewHolder(binding)
            }
        }
        return error("Unknown viewType create [$viewType]")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            R.layout.item_cast_header -> {
                val headerHolder = holder as MovieCastHeaderViewHolder
                headerHolder.bind(items[position] as MovieCastRVItem.HeaderItem)
            }

            R.layout.item_cast -> {
                val castHolder = holder as MovieCastViewHolder
                castHolder.bind(items[position] as MovieCastRVItem.PersonItem)
            }
            else -> error("Unknown viewType bind [${holder.itemViewType}]")
        }
    }

    override fun getItemCount(): Int = items.size

}
