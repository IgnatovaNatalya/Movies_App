package com.example.imdb.ui.moviecast

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imdb.R
import com.example.imdb.databinding.ActivityMovieCastBinding
import com.example.imdb.domain.models.MovieCast
import com.example.imdb.presentation.CastViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MovieCastActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieCastBinding

    private val directorsAdapter = CastPersonAdapter()
    private val writersAdapter = CastPersonAdapter()
    private val actorsAdapter = ActorsAdapter()

    companion object {
        const val EXTRA_MOVIE_ID = "MOVIE_ID"

        fun newInstance(context: Context, movieId: String): Intent {
            return Intent(context, MovieCastActivity::class.java).apply {
                putExtra(EXTRA_MOVIE_ID, movieId)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMovieCastBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val movieId = intent.getStringExtra(EXTRA_MOVIE_ID).toString()

        val viewModel: CastViewModel by viewModel {
            parametersOf(movieId)
        }

        binding.directorsRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.directorsRecycler.adapter = directorsAdapter

        binding.writersRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.writersRecycler.adapter = writersAdapter

        binding.actorsRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.actorsRecycler.adapter = actorsAdapter

        viewModel.movieCastState.observe(this) { renderCast(it) }
    }

    private fun renderCast(state: MovieCastState) {
        when (state) {
            is MovieCastState.Content -> showContent(state.movieCast)
            is MovieCastState.Error -> showError(state.errorMessage)
            is MovieCastState.Loading -> showLoading()
        }
    }

    private fun showContent(movieCast: MovieCast) {
        showElements(View.VISIBLE)
        binding.title.text = movieCast.fullTitle
        directorsAdapter.persons = movieCast.directors
        writersAdapter.persons = movieCast.writers
        actorsAdapter.actors = movieCast.actors

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

    private fun showElements(visibility: Int) {
        binding.title.visibility = visibility
        binding.directorsText.visibility = visibility
        binding.directorsRecycler.visibility = visibility
        binding.writersRecycler.visibility = visibility
        binding.writersText.visibility = visibility
        binding.actorsRecycler.visibility = visibility
        binding.actorsText.visibility = visibility
    }

}