package com.rodolfo.movies.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rodolfo.movies.ResultArch
import com.rodolfo.movies.base.BaseViewModel
import com.rodolfo.movies.models.MovieDetail
import com.rodolfo.movies.repository.AppMoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsViewModel(val moviesRepository: AppMoviesRepository) : BaseViewModel() {

    private val movieDetail: MutableLiveData<MovieDetail> = MutableLiveData()

    fun movieDetail() = movieDetail

    fun getMovie(imdbId: String) {
        launch {
            val retornoMovieDetail = withContext(Dispatchers.Default) { moviesRepository.getMovie(imdbId) }
            when (retornoMovieDetail) {
                is ResultArch.Success -> {
                    movieDetail.value = retornoMovieDetail.data
                }
                is ResultArch.Error -> {
                    movieDetail.value = null
                }
            }
        }
    }

}