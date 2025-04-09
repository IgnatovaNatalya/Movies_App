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
import com.example.imdb.domain.models.MovieDetails
import com.example.imdb.presentation.AboutViewModel
import com.example.imdb.ui.moviecast.MovieCastActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.getValue

class AboutFragment : Fragment() {

    companion object {
        const val MOVIE_ID = "MOVIE_ID"

        private var currentMovieId =""

        fun newInstance(movieId: String): AboutFragment {

            currentMovieId = movieId

            return AboutFragment().apply {
                arguments = Bundle().apply {
                    putString(MOVIE_ID, movieId)
                }
            }
        }
    }

    private val viewModel: AboutViewModel by viewModel {
        parametersOf(requireArguments().getString(MOVIE_ID))
    }

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

        viewModel.movieDetailsState.observe(viewLifecycleOwner) { renderAbout(it) }
        binding.buttonCast.setOnClickListener { openMovieCast() }
        binding.inFavoriteToggle.setOnClickListener { onFavoriteToggleClick() }
    }

    private fun openMovieCast() {
        startActivity(
            MovieCastActivity.newInstance(
                context = requireContext(),
                movieId = requireArguments().getString(MOVIE_ID).orEmpty()
            )
        )
    }

    private fun renderAbout(state: MovieDetailsState) {
        when (state) {
            is MovieDetailsState.Content -> showContent(state.movieDetails)
            is MovieDetailsState.Error -> showError(state.errorMessage)
            is MovieDetailsState.Loading -> showLoading()
        }
    }

    private fun showContent(movieDetails: MovieDetails) {
        showElements(View.VISIBLE)

        binding.title.text = movieDetails.title
        binding.rating.text = movieDetails.imDbRating
        binding.year.text = movieDetails.year
        binding.country.text = movieDetails.countries
        binding.genre.text = movieDetails.genres
        binding.director.text = movieDetails.directors
        binding.writer.text = movieDetails.writers
        binding.stars.text = movieDetails.stars
        binding.plot.text = movieDetails.plot

        setInFavorite(movieDetails.inFavorite)

        binding.progressBar.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
    }
    private fun showLoading() {
        showElements(View.GONE)

        binding.placeholderMessage.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showError(errorMessage: String) {
        showElements(View.GONE)
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.placeholderMessage.text = errorMessage
        binding.progressBar.visibility = View.GONE
    }

    private fun showElements(visibility:Int) {
        binding.title.visibility = visibility
        binding.rating.visibility = visibility
        binding.year.visibility = visibility
        binding.country.visibility = visibility
        binding.genre.visibility = visibility
        binding.director.visibility = visibility
        binding.writer.visibility = visibility
        binding.stars.visibility = visibility
        binding.plot.visibility = visibility
        binding.inFavoriteToggle.visibility = visibility

        binding.ratingText.visibility = visibility
        binding.yearText.visibility = visibility
        binding.countryText.visibility = visibility
        binding.genreText.visibility = visibility
        binding.directorText.visibility = visibility
        binding.writerText.visibility = visibility
        binding.starsText.visibility = visibility
        binding.plotText.visibility = visibility
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