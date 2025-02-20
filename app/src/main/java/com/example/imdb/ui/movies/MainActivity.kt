package com.example.imdb.ui.movies

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.R
import com.example.imdb.creator.Creator
import com.example.imdb.domain.api.MoviesInteractor
import com.example.imdb.domain.models.Movie
import com.example.imdb.ui.poster.PosterActivity

const val CLICK_DEBOUNCE_DELAY = 1000L
const val SEARCH_DEBOUNCE_DELAY = 800L

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ImdbAdapter
    private lateinit var queryInput: EditText
    private lateinit var placeholderMessage: TextView
    private lateinit var recyclerMovie: RecyclerView
    private lateinit var progressBar: ProgressBar

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private val searchMoviesRunnable = Runnable { loadMovies() }

    private var query = ""

    private lateinit var moviesInteractor: MoviesInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adapter = ImdbAdapter { openPoster(it) }

        moviesInteractor = Creator.provideMoviesInteractor()

        placeholderMessage = findViewById(R.id.weatherPlaceholderMessage)
        queryInput = findViewById(R.id.queryInput)
        recyclerMovie = findViewById(R.id.recMovies)
        progressBar = findViewById(R.id.progressBar)

        recyclerMovie.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerMovie.adapter = adapter


        queryInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                query = p0.toString()
                loadMoviesDebounce()
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun loadMovies() {
        moviesInteractor.searchMovies(query, object : MoviesInteractor.MoviesConsumer {
            @SuppressLint("NotifyDataSetChanged")
            override fun consume(foundMovies: List<Movie>) {
                runOnUiThread {
                    adapter.movies = foundMovies
                    recyclerMovie.visibility = View.VISIBLE
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }

    private fun openPoster(movie: Movie) {
        if (clickDebounce()) {
            val intent = Intent(this, PosterActivity::class.java)
            intent.putExtra("poster", movie.image)
            startActivity(intent)
        }
    }
//
    private fun clickDebounce() : Boolean { //дебаунс для нажатия на постер
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun loadMoviesDebounce() {
        handler.removeCallbacks(searchMoviesRunnable)
        handler.postDelayed(searchMoviesRunnable, SEARCH_DEBOUNCE_DELAY)
    }
}