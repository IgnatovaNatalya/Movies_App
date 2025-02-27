package com.example.imdb.presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imdb.R
import com.example.imdb.domain.api.MoviesInteractor
import com.example.imdb.domain.models.Movie
import com.example.imdb.ui.movies.MoviesAdapter

class MoviesSearchController(private val activity: Activity,
                             private val adapter: MoviesAdapter
) {

    private lateinit var moviesInteractor: MoviesInteractor

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private lateinit var queryInput: EditText
    private lateinit var placeholderMessage: TextView
    private lateinit var recyclerMovie: RecyclerView
    private lateinit var progressBar: ProgressBar

    private var query = ""

    private val handler = Handler(Looper.getMainLooper())

    private val searchMoviesRunnable = Runnable { loadMovies() }

    fun onCreate() {
        placeholderMessage = activity.findViewById(R.id.placeholderMessage)
        queryInput = activity.findViewById(R.id.queryInput)
        recyclerMovie = activity.findViewById(R.id.recMovies)
        progressBar = activity.findViewById(R.id.progressBar)

        //adapter.movies = movies

        recyclerMovie.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerMovie.adapter = adapter

        queryInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                loadMoviesDebounce()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    fun onDestroy() {
        handler.removeCallbacks(searchMoviesRunnable)
    }

    private fun loadMovies() {

        placeholderMessage.visibility = View.GONE
        recyclerMovie.visibility = View.GONE
        progressBar.visibility = View.VISIBLE

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



    private fun loadMoviesDebounce() {
        handler.removeCallbacks(searchMoviesRunnable)
        handler.postDelayed(searchMoviesRunnable, SEARCH_DEBOUNCE_DELAY)
    }

}