package com.example.imdb.presentation

import android.app.Activity
import android.widget.ImageView
import com.example.imdb.R
import com.bumptech.glide.Glide

class PosterController(private val activity: Activity) {

    val ivPoster = activity.findViewById<ImageView>(R.id.poster)
    val posterString = activity.intent.getStringExtra("poster")

    fun onCreate() {
        Glide.with(activity.applicationContext)
            .load(posterString)
            .placeholder(R.drawable.cover_blank)
            .centerCrop()
            .into(ivPoster)
    }
}