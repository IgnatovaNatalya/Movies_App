package com.example.imdb.ui.poster

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.imdb.R
import com.example.imdb.databinding.ActivityPosterBinding
import com.example.imdb.domain.models.Movie
import com.example.imdb.presentation.poster.PosterViewModel
import com.example.imdb.ui.movies.MoviesActivity
import com.example.imdb.util.Creator

class PosterActivity : ComponentActivity() {

    private lateinit var binding: ActivityPosterBinding
    private lateinit var viewModel: PosterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityPosterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.poster_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(
            this,
            PosterViewModel.getViewModelFactory(Creator.provideMoviesInteractor(this))
        )[PosterViewModel::class.java]

        val movie = Movie(
            image = intent.getStringExtra(MoviesActivity.EXTRA_POSTER).toString(),
            id = intent.getStringExtra(MoviesActivity.EXTRA_MOVIE_ID).toString(),
            title = "",
            description = "",
            inFavorite = intent.getBooleanExtra(MoviesActivity.EXTRA_IN_FAVORITE, false)
        )

        viewModel.renderMovie(movie)

        viewModel.observeMovie().observe(this) { render(it) }

        binding.inFavoriteToggle.setOnClickListener { onFavoriteToggleClick(movie) }
    }

    private fun render(movie: Movie) {
        setImagePoster(movie.image)
        setInFavorite(movie.inFavorite)
    }

    private fun setImagePoster(url: String) {
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.cover_blank)
            .fitCenter()
            .into(binding.poster)
    }

    private fun setInFavorite(favorite: Boolean) {
        binding.inFavoriteToggle.setImageDrawable(getFavoriteToggleDrawable(favorite))
    }

    private fun getFavoriteToggleDrawable(inFavorite: Boolean): Drawable? {
        return AppCompatResources.getDrawable(
            this,
            if (inFavorite) R.drawable.star_on else R.drawable.star_off
        )
    }

    fun onFavoriteToggleClick(movie: Movie) {
        viewModel.toggleFavorite(movie)
    }
}