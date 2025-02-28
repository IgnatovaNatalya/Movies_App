package com.example.imdb.ui.poster

import android.os.Bundle

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import com.example.imdb.R
import com.example.imdb.creator.Creator

class PosterActivity : AppCompatActivity() {

    private val posterController = Creator.providePosterController(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_poster)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.poster_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        posterController.onCreate()
    }
}