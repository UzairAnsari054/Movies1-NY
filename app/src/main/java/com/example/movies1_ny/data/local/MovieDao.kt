package com.example.movies1_ny.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert


@Dao
interface MovieDao {

    @Upsert
    suspend fun upsertMovieList(movieEntityList: List<MovieEntity>)

    @Query("SELECT * FROM movie_table WHERE category = :category")
    suspend fun getMovieByCategory(category: String): List<MovieEntity>

    @Query("SELECT * FROM movie_table WHERE id = :id")
    suspend fun getMovieById(id: Int): MovieEntity
}