package com.example.imdb.ui.movies

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.R
import com.example.imdb.databinding.FragmentMoviesBinding
import com.example.imdb.domain.models.Movie
import com.example.imdb.ui.RootActivity
import com.example.imdb.ui.core.BindingFragment
import com.example.imdb.ui.details.DetailsFragment
import com.example.imdb.util.debounce
import com.example.imdb.viewmodel.MoviesSearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoviesFragment : BindingFragment<FragmentMoviesBinding>() {
    companion object {
        const val CLICK_DEBOUNCE_DELAY = 300L
    }

    private lateinit var onMovieClickDebounce: (Movie) -> Unit
    private lateinit var recyclerMovies: RecyclerView
    private lateinit var queryInput: EditText

    private var adapter: MoviesAdapter? = null
    private var textWatcher: TextWatcher? = null
    private val viewModel: MoviesSearchViewModel by viewModel()

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?):
            FragmentMoviesBinding {
        return FragmentMoviesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerMovies = binding.recyclerMovies
        queryInput = binding.queryInput

        viewModel.observeState().observe(viewLifecycleOwner) { render(it) }

        viewModel.observeShowToast().observe(requireActivity()) { showToast(it) }

        onMovieClickDebounce = debounce<Movie>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { movie ->
            findNavController().navigate(
                R.id.action_moviesFragment_to_detailsFragment,
                DetailsFragment.createArgs(movie.id, movie.image)
            )
        }

        adapter = MoviesAdapter(
            object : MoviesAdapter.MovieClickListener {
                override fun onMovieClick(movie: Movie) {
                    (activity as RootActivity).animateBottomNavigationView()
                    onMovieClickDebounce(movie)
                }

                override fun onFavoriteToggleClick(movie: Movie) {
                    viewModel.toggleFavorite(movie)
                }
            }
        )

        binding.recyclerMovies.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerMovies.adapter = adapter

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.loadMoviesDebounce(changedText = p0?.toString() ?: "")
            }

            override fun afterTextChanged(p0: Editable?) {}
        }
        queryInput.addTextChangedListener(textWatcher)

    }

    private fun showToast(additionalMessage: String?) {
        Toast.makeText(activity, additionalMessage, Toast.LENGTH_LONG).show()
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

        adapter?.movies = movies
        adapter?.notifyDataSetChanged()
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        binding.queryInput.removeTextChangedListener(textWatcher)
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        recyclerMovies.adapter = null
        textWatcher?.let { queryInput.removeTextChangedListener(it) } //
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshFavoriteMovies()
    }
}