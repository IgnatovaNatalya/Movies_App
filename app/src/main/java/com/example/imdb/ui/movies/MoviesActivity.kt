package com.example.imdb.ui.movies

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imdb.R
import com.example.imdb.databinding.ActivityMainBinding
import com.example.imdb.domain.models.Movie
import com.example.imdb.presentation.movies.MoviesSearchViewModel
import com.example.imdb.ui.poster.PosterActivity


class MoviesActivity : ComponentActivity() {

    companion object {
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val adapter = MoviesAdapter { openPoster(it) }
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private var textWatcher: TextWatcher? = null
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MoviesSearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(
            this,
            MoviesSearchViewModel.getViewModelFactory()
        )[MoviesSearchViewModel::class.java]

        viewModel.observeState().observe(this) {
            render(it)
        }

        viewModel.observeToastState().observe(this) { toastState ->
            if (toastState is ToastState.Show) {
                showToast(toastState.additionalMessage)
                viewModel.toastWasShown()
            }
        }

        binding.recyclerMovies.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerMovies.adapter = adapter

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                this@MoviesActivity.viewModel.loadMoviesDebounce(changedText = p0?.toString() ?: "")
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
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun openPoster(movie: Movie) {
        if (clickDebounce()) {
            val intent = Intent(this, PosterActivity::class.java)
            intent.putExtra("poster", movie.image)
            startActivity(intent)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
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

    override fun onDestroy() {
        super.onDestroy()
        binding.queryInput.removeTextChangedListener(textWatcher)
    }
}