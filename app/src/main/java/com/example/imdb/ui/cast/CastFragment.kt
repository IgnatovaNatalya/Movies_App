package com.example.imdb.ui.cast

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imdb.databinding.FragmentCastBinding
import com.example.imdb.viewmodel.cast.CastViewModel
import com.example.imdb.ui.core.BindingFragment
import com.example.imdb.ui.details.DetailsFragment
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.getValue

class CastFragment : BindingFragment<FragmentCastBinding>() {

    private val adapter = ListDelegationAdapter(
        movieCastHeaderDelegate(),
        movieCastPersonDelegate(),
    )

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCastBinding {
        return FragmentCastBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieId = requireArguments().getString(EXTRA_MOVIE_ID)

        val viewModel: CastViewModel by viewModel {
            parametersOf(movieId)
        }

        binding.movieCastRecyclerView.adapter = adapter
        binding.movieCastRecyclerView.layoutManager = LinearLayoutManager(activity)

        viewModel.movieCastState.observe(viewLifecycleOwner) { renderCast(it) }
    }

    private fun renderCast(state: MovieCastState) {
        when (state) {
            is MovieCastState.Content -> showContent(state)
            is MovieCastState.Error -> showError(state)
            is MovieCastState.Loading -> showLoading()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showContent(contentState: MovieCastState.Content) {
        binding.contentContainer.isVisible = true
        binding.progressBar.isVisible = false
        binding.placeholderMessage.isVisible = false

        binding.title.text = contentState.fullTitle
        adapter.items = contentState.items
        adapter.notifyDataSetChanged()
    }

    private fun showLoading() {
        binding.contentContainer.isVisible = false
        binding.placeholderMessage.isVisible = false
        binding.progressBar.isVisible = true
    }

    private fun showError(errorState: MovieCastState.Error) {
        binding.contentContainer.isVisible = false
        binding.placeholderMessage.isVisible = true
        binding.progressBar.isVisible = false

        binding.placeholderMessage.text = errorState.errorMessage
    }

    companion object {

        const val EXTRA_MOVIE_ID = "MOVIE_ID"

        fun createArgs(movieId: String) =
            Bundle().apply { putString(EXTRA_MOVIE_ID, movieId) }
    }
}