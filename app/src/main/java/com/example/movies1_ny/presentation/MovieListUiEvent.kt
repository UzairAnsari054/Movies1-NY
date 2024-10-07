package com.example.movies1_ny.presentation

sealed interface MovieListUiEvent {
    class Paginate(val category: String) : MovieListUiEvent
    object Navigate : MovieListUiEvent
}