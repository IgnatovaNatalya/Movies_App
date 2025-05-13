package com.example.imdb.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.imdb.data.dao.MovieDao
import com.example.imdb.data.db.entity.MovieEntity

@Database(version = 1, entities = [MovieEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
}