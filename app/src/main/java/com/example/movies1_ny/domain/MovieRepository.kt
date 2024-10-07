package com.example.movies1_ny.domain

import com.example.movies1_ny.util.Resource
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    suspend fun getMovieList(
        forceFetchFromRemote: Boolean,
        page: Int,
        category: String
    ): Flow<Resource<List<Movie>>>

    suspend fun getMovieById(id: Int): Flow<Resource<Movie>>
}