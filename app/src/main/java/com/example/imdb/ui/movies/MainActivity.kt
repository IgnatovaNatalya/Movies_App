package com.example.imdb.ui.movies

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.imdb.R
import com.example.imdb.domain.models.Movie
import com.example.imdb.ui.poster.PosterActivity
import com.example.imdb.util.Creator

const val CLICK_DEBOUNCE_DELAY = 1000L

class MainActivity : AppCompatActivity() {

    private val adapter = MoviesAdapter { openPoster(it) }

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    private val moviesSearchController = Creator.provideMoviesSearchController(this, adapter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        moviesSearchController.onCreate()

    }

    override fun onDestroy() {
        super.onDestroy()
        moviesSearchController.onDestroy()
    }

    private fun clickDebounce() : Boolean { //дебаунс для нажатия на постер
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

}