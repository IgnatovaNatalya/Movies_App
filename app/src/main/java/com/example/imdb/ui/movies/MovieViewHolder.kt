package com.example.imdb.ui.movies

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.imdb.R
import com.example.imdb.databinding.ItemMovieBinding
import com.example.imdb.domain.models.Movie

class MovieViewHolder(
    private val parent: ViewGroup,
    private val clickListener: MoviesAdapter.MovieClickListener,
    private val binding: ItemMovieBinding
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: Movie) {
        binding.title.text = movie.title
        binding.description.text = movie.description

        Glide.with(binding.root)
            .load(movie.image)
            .placeholder(R.drawable.cover_blank)
            .centerInside()
            //.centerCrop()
            .transform(RoundedCorners(20))
            .into(binding.cover)

        binding.inFavoriteToggle.setImageDrawable(getFavoriteToggleDrawable(movie.inFavorite))

        itemView.setOnClickListener { clickListener.onMovieClick(movie) }
        binding.inFavoriteToggle.setOnClickListener { clickListener.onFavoriteToggleClick(movie)}
    }

    private fun getFavoriteToggleDrawable(inFavorite: Boolean): Drawable? {
        return parent.context.getDrawable(
            if (inFavorite) R.drawable.star_on else R.drawable.star_off
        )
    }

}