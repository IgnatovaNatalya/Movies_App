package com.example.imdb.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.imdb.databinding.FragmentDetailsBinding
import com.example.imdb.ui.core.BindingFragment


class DetailsFragment : BindingFragment<FragmentDetailsBinding>() {


    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?):
            FragmentDetailsBinding {
        return FragmentDetailsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    companion object {
        const val EXTRA_POSTER = "poster"
        const val EXTRA_IN_FAVORITE = "inFavorite"
        const val EXTRA_MOVIE_ID = "MOVIE_ID"

        fun newInstance(movieId: String, poster: String, inFavorite: Boolean) =
            DetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_MOVIE_ID, movieId)
                    putString(EXTRA_POSTER, poster)
                    putBoolean(EXTRA_IN_FAVORITE, inFavorite)
                }
            }
    }
}