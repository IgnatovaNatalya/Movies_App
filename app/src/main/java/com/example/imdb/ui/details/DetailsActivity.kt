package com.example.imdb.ui.details

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.imdb.R
import com.example.imdb.databinding.ActivityDetailsBinding
import com.example.imdb.domain.models.Movie
import com.example.imdb.presentation.poster.DetailsViewModel
import com.example.imdb.ui.movies.MoviesActivity
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private lateinit var tabMediator: TabLayoutMediator

    private val viewModel: DetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.details_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val movie = Movie(
            image = intent.getStringExtra(MoviesActivity.EXTRA_POSTER).toString(),
            id = intent.getStringExtra(MoviesActivity.EXTRA_MOVIE_ID).toString(),
            title = "Нашли какой-то фильм",
            description = "",
            inFavorite = intent.getBooleanExtra(MoviesActivity.EXTRA_IN_FAVORITE, false)
        )

        viewModel.setCurrentMovie(movie)

        binding.viewPager.adapter = DetailsViewPagerAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Постер"
                1 -> tab.text = "О фильме"
            }
        }
        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }

}