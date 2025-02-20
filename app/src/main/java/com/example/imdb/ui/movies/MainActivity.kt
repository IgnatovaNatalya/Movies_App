package com.example.imdb.ui.movies

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
import com.example.imdb.presentation.ImdbAdapter
import com.example.imdb.domain.models.Movie
import com.example.imdb.MovieResponse
import com.example.imdb.R
import com.example.imdb.data.network.RetrofitInstance
import com.example.imdb.ui.poster.PosterActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val CLICK_DEBOUNCE_DELAY = 1000L
const val SEARCH_DEBOUNCE_DELAY = 800L

class MainActivity : AppCompatActivity() {

    private val movies = ArrayList<Movie>()
    private val adapter = ImdbAdapter { openPoster(it) }

    private lateinit var queryInput: EditText
    private lateinit var placeholderMessage: TextView
    private lateinit var movieList: RecyclerView
    private lateinit var progressBar: ProgressBar

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private val searchRunnable = Runnable { search() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        placeholderMessage = findViewById(R.id.weatherPlaceholderMessage)
        queryInput = findViewById(R.id.queryInput)
        movieList = findViewById(R.id.recMovies)
        progressBar = findViewById(R.id.progressBar)

        movieList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adapter.movies = movies
        movieList.adapter = adapter

        queryInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchDebounce()
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun search() {
        placeholderMessage.visibility = View.GONE
        movieList.visibility = View.GONE
        progressBar.visibility = View.VISIBLE

        RetrofitInstance.apiService.getMovies( queryInput.text.toString())
            .enqueue(object : Callback<MovieResponse> {
                override fun onResponse(call: Call<MovieResponse>,
                                        response: Response<MovieResponse>
                ) {
                    progressBar.visibility = View.GONE

                    when (response.code()) {
                        200 -> {
                            if (response.body()?.results?.isNotEmpty() == true) {
                                movieList.visibility = View.VISIBLE
                                movies.clear()
                                movies.addAll(response.body()?.results!!)
                                adapter.notifyDataSetChanged()
                                showMessage("", "")
                            } else {
                                showMessage(getString(R.string.nothing_found), "")
                            }
                        }
                        else -> showMessage(getString(R.string.something_went_wrong), response.code().toString())
                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    showMessage(getString(R.string.something_went_wrong), t.message.toString())
                }
            })
    }

    private fun showMessage(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) {
            placeholderMessage.visibility = View.VISIBLE
            movies.clear()
            adapter.notifyDataSetChanged()
            placeholderMessage.text = text
            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            placeholderMessage.visibility = View.GONE
        }
    }

    private fun openPoster(movie: Movie) {
        if (clickDebounce()) {
            val intent = Intent(this, PosterActivity::class.java)
            intent.putExtra("poster", movie.image)
            startActivity(intent)
        }
    }

    private fun clickDebounce() : Boolean { //дебаунс для нажатия на постер
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }
}