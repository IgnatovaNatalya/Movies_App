package com.example.imdb.ui.movies

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imdb.R
import com.example.imdb.databinding.FragmentMoviesBinding
import com.example.imdb.domain.models.Movie
import com.example.imdb.presentation.MoviesSearchViewModel
import com.example.imdb.ui.core.BindingFragment
import com.example.imdb.ui.details.DetailsFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoviesFragment : BindingFragment<FragmentMoviesBinding>() {
    companion object {
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val adapter = MoviesAdapter(
        object : MoviesAdapter.MovieClickListener {
            override fun onMovieClick(movie: Movie) {
                openDetails(movie)
            }

            override fun onFavoriteToggleClick(movie: Movie) {
                viewModel.toggleFavorite(movie)
            }
        })

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private var textWatcher: TextWatcher? = null

    private val viewModel: MoviesSearchViewModel by viewModel()

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?):
            FragmentMoviesBinding {
        return FragmentMoviesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) { render(it) }

        viewModel.observeToastState().observe(viewLifecycleOwner) { toastState ->
            if (toastState is ToastState.Show) {
                showToast(toastState.additionalMessage)
                viewModel.toastWasShown()
            }
        }

        binding.recyclerMovies.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerMovies.adapter = adapter

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.loadMoviesDebounce(changedText = p0?.toString() ?: "")
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }
        binding.queryInput.addTextChangedListener(textWatcher)
    }

    private fun clickDebounce(): Boolean { //дебаунс для нажатия на постер
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed(
                { isClickAllowed = true },
                CLICK_DEBOUNCE_DELAY
            )
        }
        return current
    }

    private fun openDetails(movie: Movie) {
        if (clickDebounce())
            findNavController().navigate(
                R.id.action_moviesFragment_to_detailsFragment,
                DetailsFragment.createArgs(movie.id, movie.image)
            )
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun render(state: MoviesState) {
        when (state) {
            is MoviesState.Content -> showContent(state.movies)
            is MoviesState.Empty -> showEmpty(state.message)
            is MoviesState.Error -> showError(state.errorMessage)
            is MoviesState.Loading -> showLoading()
        }
    }

    private fun showLoading() {
        binding.recyclerMovies.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showError(errorMessage: String) {
        binding.recyclerMovies.visibility = View.GONE
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.placeholderMessage.text = errorMessage
    }

    private fun showEmpty(emptyMessage: String) {
        showError(emptyMessage)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showContent(movies: List<Movie>) {
        binding.recyclerMovies.visibility = View.VISIBLE
        binding.placeholderMessage.visibility = View.GONE
        binding.progressBar.visibility = View.GONE

        adapter.movies = movies
        adapter.notifyDataSetChanged()
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        binding.queryInput.removeTextChangedListener(textWatcher)
//    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshFavoriteMovies()
    }
}