package com.example.movies1_ny.presentation

import com.example.movies1_ny.domain.Movie

data class DetailsState(
    val isLoading: Boolean = false,
    val movie: Movie? = null
)