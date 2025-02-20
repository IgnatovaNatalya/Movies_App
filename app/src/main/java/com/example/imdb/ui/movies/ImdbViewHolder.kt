package com.example.imdb.ui.movies

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.imdb.domain.models.Movie
import com.example.imdb.R

class ImdbViewHolder  (itemView: View): RecyclerView.ViewHolder(itemView) {

    private val tfTitle: TextView = itemView.findViewById(R.id.title)
    private val ivCover:ImageView = itemView.findViewById(R.id.cover)
    private val tfDesc:TextView =  itemView.findViewById(R.id.description)

    fun bind(movie: Movie) {
        tfTitle.text = movie.title
        tfDesc.text = movie.description

        Glide.with(itemView)
            .load(movie.image)
            //.apply(RequestOptions().override(150, 300))
            .placeholder(R.drawable.cover_blank)
            //.into(ivCover)

            .centerCrop()
            .transform(RoundedCorners(20))
            .into(ivCover)
    }
}