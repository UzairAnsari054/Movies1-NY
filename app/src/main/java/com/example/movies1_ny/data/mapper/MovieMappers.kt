package com.example.movies1_ny.data.mapper

import com.example.movies1_ny.data.local.MovieEntity
import com.example.movies1_ny.data.remote.model.MovieDto
import com.example.movies1_ny.domain.Movie

fun MovieDto.toMovieEntity(category: String): MovieEntity {
    return MovieEntity(
        adult = adult ?: false,
        backdrop_path = backdrop_path ?: "",
        original_language = original_language ?: "",
        original_title = original_title ?: "",
        overview = overview ?: "",
        popularity = popularity ?: 0.0,
        poster_path = poster_path ?: "",
        release_date = release_date ?: "",
        title = title ?: "",
        video = video ?: false,
        vote_average = vote_average ?: 0.0,
        vote_count = vote_count ?: 0,

        id = id ?: 0,
        category = category,
        genre_ids = genre_ids?.joinToString(",") ?: "-1, -2"
    )
}

fun MovieEntity.toMovie(): Movie {
    return Movie(
        adult = adult,
        backdrop_path = backdrop_path,
        original_language = original_language,
        original_title = original_title,
        overview = overview,
        popularity = popularity,
        poster_path = poster_path,
        release_date = release_date,
        title = title,
        video = video,
        vote_average = vote_average,
        vote_count = vote_count,

        id = id,
        category = category,
        genre_ids = try {
            genre_ids.split(",").map { it.toInt() }
        } catch (e: Exception) {
            e.printStackTrace()
            listOf(-1, -2)
        }
    )
}