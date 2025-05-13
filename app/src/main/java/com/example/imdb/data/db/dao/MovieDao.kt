package com.example.imdb.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.imdb.data.db.entity.MovieEntity

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Query("SELECT * FROM movie_table")
    suspend fun getMovies(): List<MovieEntity>


//    @Insert(entity = MovieEntity::class, onConflict = OnConflictStrategy.REPLACE)
//    fun insertNewMovie(movieEntity: MovieEntity)
//
//    @Update(entity = MovieEntity::class, onConflict = OnConflictStrategy.REPLACE)
//    fun updateMovie(movieEntity: MovieEntity)
//
//    @Delete(entity = MovieEntity::class)
//    fun deleteMovieEntity(movieEntity: MovieEntity)
//
//    @Transaction
//    fun insertMovieAndActor(movieEntity: MovieEntity, actorEntity: ActorEntity) {
//        insertMovie(movieEntity)
//        insertActor(actorEntity)
//    }
//
//    @Query("SELECT * FROM movie_table WHERE id = :movieId")
//    fun getMovieById(movieId: Long): MovieEntity
//
//    @Query("SELECT * FROM movie_table WHERE movie_name LIKE :search")
//    fun searchMoviesByName(search: String): List<MovieEntity>


}