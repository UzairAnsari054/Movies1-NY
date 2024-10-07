package com.example.movies1_ny.data.repository

import com.example.movies1_ny.data.local.MovieDatabase
import com.example.movies1_ny.data.mapper.toMovie
import com.example.movies1_ny.data.mapper.toMovieEntity
import com.example.movies1_ny.data.remote.model.MovieApi
import com.example.movies1_ny.domain.Movie
import com.example.movies1_ny.domain.MovieRepository
import com.example.movies1_ny.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDatabase: MovieDatabase
) : MovieRepository {

    override suspend fun getMovieList(
        forceFetchFromRemote: Boolean,
        page: Int,
        category: String
    ): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))

            val movieListFromLocal = movieDatabase.getMovieDao().getMovieByCategory(category)
            val loadMovieListFromLocal = movieListFromLocal.isNotEmpty() && !forceFetchFromRemote

            if (loadMovieListFromLocal) {
                val movie = movieListFromLocal.map { movieEntity ->
                    movieEntity.toMovie()
                }
                emit(Resource.Success(data = movie))
                emit(Resource.Loading(false))
                return@flow
            }

            val movieListFromRemote = movieApi.getMovieList(category, page)
            val movieEntityList = movieListFromRemote.results.map { movieDto ->
                movieDto.toMovieEntity(category)
            }
            movieDatabase.getMovieDao().upsertMovieList(movieEntityList)

            val latestMovieListFromLocal = movieDatabase.getMovieDao().getMovieByCategory(category)
            val latestMovies = latestMovieListFromLocal.map { movieEntity ->
                movieEntity.toMovie()
            }
            emit(Resource.Success(data = latestMovies))
            emit(Resource.Loading(false))
            return@flow
        }
    }

    override suspend fun getMovieById(id: Int): Flow<Resource<Movie>> {
        return flow {
            emit(Resource.Loading(true))

            val movieById = movieDatabase.getMovieDao().getMovieById(id)
            if (movieById != null) {
                val movie = movieById.toMovie()
                emit(Resource.Success(data = movie))
                emit(Resource.Loading(false))
                return@flow
            }
        }
    }
}