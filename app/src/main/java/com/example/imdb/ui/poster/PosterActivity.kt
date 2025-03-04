package com.example.imdb.ui.poster

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.imdb.R
import com.example.imdb.presentation.poster.PosterView
import com.example.imdb.util.Creator

class PosterActivity : AppCompatActivity(), PosterView {

    private lateinit var ivPoster:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_poster)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.poster_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ivPoster = findViewById(R.id.poster)

        val posterPresenter = Creator.providePosterPresenter(this)
        posterPresenter.onCreate()
    }

    override fun getPosterUrl(): String {
        return intent.getStringExtra("poster").toString()
    }

    override fun setImagePoster(url: String) {
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.cover_blank)
            .centerCrop()
            .into(ivPoster)
    }
}