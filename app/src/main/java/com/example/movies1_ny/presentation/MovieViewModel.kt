package com.example.movies1_ny.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies1_ny.domain.MovieRepository
import com.example.movies1_ny.util.Category
import com.example.movies1_ny.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _movieListState = MutableStateFlow(MovieListState())
    val movieListState = _movieListState.asStateFlow()

    init {
        getPopularMovieList(false)
        getUpcomingMovieList(false)
    }

    fun onEvent(event: MovieListUiEvent) {
        when (event) {
            MovieListUiEvent.Navigate -> {
                _movieListState.update {
                    it.copy(isCurrentPopularScreen = !_movieListState.value.isCurrentPopularScreen)
                }
            }

            is MovieListUiEvent.Paginate -> {
                if (movieListState.value.isCurrentPopularScreen) {
                    getPopularMovieList(true)
                } else if (!movieListState.value.isCurrentPopularScreen) {
                    getUpcomingMovieList(true)
                }
            }
        }
    }

    private fun getPopularMovieList(forceFetchFromRemote: Boolean) {
        _movieListState.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
            repository.getMovieList(
                forceFetchFromRemote = forceFetchFromRemote,
                page = movieListState.value.popularMovieListPage,
                category = Category.POPULAR
            ).collectLatest { result ->
                when(result){
                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }
                    is Resource.Loading -> {
                        _movieListState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                    is Resource.Success -> {
                        result.data?.let { movieList ->
                            _movieListState.update {
                                it.copy(
                                    popularMovieList = movieListState.value.popularMovieList + movieList.shuffled(),
                                    popularMovieListPage = movieListState.value.popularMovieListPage + 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getUpcomingMovieList(forceFetchFromRemote: Boolean) {
        _movieListState.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
            repository.getMovieList(
                forceFetchFromRemote = forceFetchFromRemote,
                page = movieListState.value.upcomingMovieListPage,
                category = Category.UPCOMING
            ).collectLatest { result ->
                when(result){
                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }
                    is Resource.Loading -> {
                        _movieListState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                    is Resource.Success -> {
                        result.data?.let { movieList ->
                            _movieListState.update {
                                it.copy(
                                    upcomingMovieList = movieListState.value.upcomingMovieList + movieList.shuffled(),
                                    upcomingMovieListPage = movieListState.value.upcomingMovieListPage + 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}