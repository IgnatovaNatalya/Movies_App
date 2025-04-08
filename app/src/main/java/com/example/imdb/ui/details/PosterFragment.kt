package com.example.imdb.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.imdb.R
import com.example.imdb.databinding.FragmentPosterBinding
import com.example.imdb.presentation.PosterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PosterFragment : Fragment() {

    companion object {
        const val POSTER_URL = "POSTER_URL"

        fun newInstance(url: String) = PosterFragment().apply {
            arguments = Bundle().apply {
                putString(POSTER_URL, url)
            }
        }
    }

    private val viewModel: PosterViewModel by viewModel {
        parametersOf(requireArguments().getString(POSTER_URL))
    }

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

        viewModel.posterLiveData.observe(viewLifecycleOwner) { render(it) }
        //binding.inFavoriteToggle.setOnClickListener { onFavoriteToggleClick() }
    }

    private fun render(posterUrl: String?) {
        if (posterUrl != null) setImagePoster(posterUrl)
    }

    private fun setImagePoster(url: String) {
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.cover_blank)
            .centerCrop()
            .into(binding.poster)
    }

//    private fun setInFavorite(favorite: Boolean) {
//        binding.inFavoriteToggle.setImageDrawable(getFavoriteToggleDrawable(favorite))
//    }
//
//    private fun getFavoriteToggleDrawable(inFavorite: Boolean): Drawable? {
//        return AppCompatResources.getDrawable(
//            requireActivity(),
//            if (inFavorite) R.drawable.star_on else R.drawable.star_off
//        )
//    }

//    fun onFavoriteToggleClick(movie: Movie) {
//        viewModel.toggleFavorite(movie)
//    }

//    fun onFavoriteToggleClick() {
//        viewModel.toggleFavoriteCurrentMovie()
//    }

}