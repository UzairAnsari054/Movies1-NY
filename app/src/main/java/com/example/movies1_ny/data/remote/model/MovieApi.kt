package com.example.movies1_ny.data.remote.model

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("movie/{category}")
    suspend fun getMovieList(
        @Path("category") category: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): MovieListDto


    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"     //IMAGE_THUMBNAIL + backdrop_path
        const val API_KEY = "45ce46a99d8b61da5c6f04124fc1c03d"
    }

    //https://api.themoviedb.org/3/movie/popular?language=en-US&page=1&api_key=45ce46a99d8b61da5c6f04124fc1c03d
}