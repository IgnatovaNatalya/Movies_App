package com.example.imdb.ui.moviecast

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imdb.R
import com.example.imdb.databinding.ActivityMovieCastBinding
import com.example.imdb.presentation.cast.CastViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MovieCastActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieCastBinding

//    private val directorsAdapter = CastPersonAdapter()
//    private val writersAdapter = CastPersonAdapter()
//    private val actorsAdapter = ActorsAdapter()

    private val adapter =  MoviesCastAdapter()

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

        binding.movieCastRecyclerView.adapter = adapter
        binding.movieCastRecyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.movieCastState.observe(this) { renderCast(it) }
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
        //movieCast.directors + movieCast.writers +movieCast.actors + movieCast.others
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
}