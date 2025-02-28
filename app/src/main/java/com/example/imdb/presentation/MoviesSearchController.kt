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
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imdb.R
import com.example.imdb.creator.Creator
import com.example.imdb.domain.api.MoviesInteractor
import com.example.imdb.domain.models.SearchResult
import com.example.imdb.ui.movies.MoviesAdapter

class MoviesSearchController(private val activity: Activity,
                             private val adapter: MoviesAdapter
) {

    private  var moviesInteractor=  Creator.provideMoviesInteractor()

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private lateinit var queryInput: EditText
    private lateinit var placeholderMessage: TextView
    private lateinit var recyclerMovie: RecyclerView
    private lateinit var progressBar: ProgressBar

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

        moviesInteractor.searchMovies(queryInput.text.toString(), object : MoviesInteractor.MoviesConsumer {
            @SuppressLint("NotifyDataSetChanged")
            override fun consume(searchResult: SearchResult) {
                handler.post{
                    progressBar.visibility = View.GONE
                    when (searchResult.resultCode) {
                      200-> {
                          if (searchResult.results.isNotEmpty()) {
                              hideMessage()
                              adapter.movies = searchResult.results
                              //recyclerMovie.visibility = View.VISIBLE
                              adapter.notifyDataSetChanged()
                          }
                          else {
                              showMessage(activity.getString(R.string.nothing_found), "")
                          }
                      }
                        else -> {
                            showMessage(activity.getString(R.string.something_went_wrong), "")
                        }
                    }
                }
            }
        })
    }

    private fun loadMoviesDebounce() {
        handler.removeCallbacks(searchMoviesRunnable)
        handler.postDelayed(searchMoviesRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showMessage(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) {
            placeholderMessage.visibility = View.VISIBLE
            recyclerMovie.visibility = View.GONE
            adapter.notifyDataSetChanged()
            placeholderMessage.text = text
            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(activity, additionalMessage, Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            placeholderMessage.visibility = View.GONE
            recyclerMovie.visibility = View.VISIBLE
        }
    }

    private fun hideMessage() {
        placeholderMessage.visibility = View.GONE
        recyclerMovie.visibility = View.VISIBLE
    }

}