package com.example.imdb.ui.details

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.imdb.R
import com.example.imdb.databinding.FragmentPosterBinding
import com.example.imdb.domain.models.Movie
import com.example.imdb.presentation.poster.DetailsViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import kotlin.getValue

class PosterFragment : Fragment() {


    val viewModel by activityViewModel<DetailsViewModel>()

    private lateinit var binding: FragmentPosterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPosterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentMovie.observe(viewLifecycleOwner) { render(it) }
        binding.inFavoriteToggle.setOnClickListener { onFavoriteToggleClick() }
    }

    private fun render(movie: Movie) {
        setImagePoster(movie.image)
        setInFavorite(movie.inFavorite)
    }

    private fun setImagePoster(url: String) {
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.cover_blank)
            .fitCenter()
            .into(binding.poster)
    }

    private fun setInFavorite(favorite: Boolean) {
        binding.inFavoriteToggle.setImageDrawable(getFavoriteToggleDrawable(favorite))
    }

    private fun getFavoriteToggleDrawable(inFavorite: Boolean): Drawable? {
        return AppCompatResources.getDrawable(
            requireActivity(),
            if (inFavorite) R.drawable.star_on else R.drawable.star_off
        )
    }

//    fun onFavoriteToggleClick(movie: Movie) {
//        viewModel.toggleFavorite(movie)
//    }

    fun onFavoriteToggleClick() {
        viewModel.toggleFavoriteCurrentMovie()
    }

}