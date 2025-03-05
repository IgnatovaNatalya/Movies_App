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
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.R
import com.example.imdb.domain.models.Movie
import com.example.imdb.presentation.movies.MoviesView
import com.example.imdb.ui.poster.PosterActivity
import com.example.imdb.util.Creator

const val CLICK_DEBOUNCE_DELAY = 1000L

class MoviesActivity : AppCompatActivity(), MoviesView {

    private lateinit var queryInput: EditText
    private lateinit var placeholderMessage: TextView
    private lateinit var recyclerMovie: RecyclerView
    private lateinit var progressBar: ProgressBar

    private val adapter = MoviesAdapter { openPoster(it) }

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    private val moviesSearchPresenter = Creator.provideMoviesSearchPresenter(this, this)

    private var textWatcher: TextWatcher? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        placeholderMessage = findViewById(R.id.placeholderMessage)
        queryInput = findViewById(R.id.queryInput)
        recyclerMovie = findViewById(R.id.recMovies)
        progressBar = findViewById(R.id.progressBar)

        recyclerMovie.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerMovie.adapter = adapter

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                moviesSearchPresenter.loadMoviesDebounce(changedText = p0?.toString() ?: "")
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        }
        queryInput.addTextChangedListener(textWatcher)
        //textWatcher?.let { queryInput.addTextChangedListener(it) }

        //moviesSearchPresenter.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        queryInput.removeTextChangedListener(textWatcher)
        moviesSearchPresenter.onDestroy()
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

    @SuppressLint("NotifyDataSetChanged")
    override fun updateMoviesList(newMoviesList: List<Movie>) {
        adapter.movies = newMoviesList
        adapter.notifyDataSetChanged()
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun render(state: SearchMoviesState) {
        when (state) {
            is SearchMoviesState.Content -> showContent(state.movies)
            is SearchMoviesState.Empty -> showEmpty(state.message)
            is SearchMoviesState.Error ->  showError(state.errorMessage)
            is SearchMoviesState.Loading -> showLoading()
        }
    }

    private fun showLoading() {
        recyclerMovie.visibility = View.GONE
        placeholderMessage.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun showError(errorMessage: String) {
        recyclerMovie.visibility = View.GONE
        placeholderMessage.visibility = View.VISIBLE
        progressBar.visibility = View.GONE

        placeholderMessage.text = errorMessage
    }

    private fun showEmpty(emptyMessage: String) {
        showError(emptyMessage)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showContent(movies: List<Movie>) {
        recyclerMovie.visibility = View.VISIBLE
        placeholderMessage.visibility = View.GONE
        progressBar.visibility = View.GONE

        adapter.movies = movies
        adapter.notifyDataSetChanged()
    }
}