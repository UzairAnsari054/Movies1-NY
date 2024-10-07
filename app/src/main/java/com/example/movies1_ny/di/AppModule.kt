package com.example.movies1_ny.di

import android.app.Application
import androidx.room.Room
import com.example.movies1_ny.data.local.MovieDatabase
import com.example.movies1_ny.data.remote.model.MovieApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    @Singleton
    @Provides
    fun provideMovieApi(app: Application) = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(MovieApi.BASE_URL)
        .client(client)
        .build()
        .create(MovieApi::class.java)

    @Singleton
    @Provides
    fun provideMovieDatabase(app: Application) = Room.databaseBuilder(
        app,
        MovieDatabase::class.java,
        "movie_db"
    ).build()

}