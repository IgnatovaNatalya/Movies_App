package com.example.imdb.ui.details

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.example.imdb.R
import com.example.imdb.databinding.FragmentAboutBinding
import com.example.imdb.domain.models.Movie
import com.example.imdb.presentation.poster.DetailsViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import kotlin.getValue

class AboutFragment : Fragment() {

    val viewModel by activityViewModel<DetailsViewModel>()

    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentMovie.observe(viewLifecycleOwner) { renderAbout(it) }
        binding.inFavoriteToggle.setOnClickListener { onFavoriteToggleClick() }
    }

    private fun renderAbout(movie: Movie) {
        setTitle(movie.title)
        setYear(movie.description)
        setInFavorite(movie.inFavorite)
    }

    private fun setTitle (title:String) {
        binding.title.text = title
    }
    private fun setYear(year:String) {
        binding.year.text = year
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

    fun onFavoriteToggleClick() {
        viewModel.toggleFavoriteCurrentMovie()
    }

}