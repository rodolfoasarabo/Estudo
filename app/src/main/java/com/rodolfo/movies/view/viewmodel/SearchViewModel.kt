package com.rodolfo.movies.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rodolfo.movies.ResultArch
import com.rodolfo.movies.base.BaseViewModel
import com.rodolfo.movies.models.RetornoBusca
import com.rodolfo.movies.repository.AppMoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(val moviesRepository: AppMoviesRepository) : BaseViewModel() {

    private val movies: MutableLiveData<RetornoBusca> = MutableLiveData()

    fun movies() = movies

    fun searchMovie(name: String) {
        launch {
            val retornoBusca = withContext(Dispatchers.Default) { moviesRepository.searchMovie(name) }
            when (retornoBusca) {
                is ResultArch.Success -> {
                    movies.value = retornoBusca.data
                }
                is ResultArch.Error -> {
                    movies.value = RetornoBusca(null, null, retornoBusca.data.message)
                }
            }
        }
    }

}