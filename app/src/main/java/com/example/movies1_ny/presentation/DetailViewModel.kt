package com.example.movies1_ny.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies1_ny.domain.MovieRepository
import com.example.movies1_ny.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val movieId = savedStateHandle.get<Int>("movieId")

    private val _detailsState = MutableStateFlow(DetailsState())
    val detailsState = _detailsState.asStateFlow()

    init {
        getMovieById(movieId ?: 1)
    }

    fun getMovieById(id: Int) {
        _detailsState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            repository.getMovieById(id).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _detailsState.update { it.copy(isLoading = false) }
                    }

                    is Resource.Loading -> {
                        _detailsState.update { it.copy(isLoading = result.isLoading) }
                    }

                    is Resource.Success -> {
                        result.data?.let { movie ->
                            _detailsState.update { it.copy(movie = movie) }
                        }
                    }
                }
            }
        }
    }

}