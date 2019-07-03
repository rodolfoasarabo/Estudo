package com.rodolfo.movies.repository

import com.rodolfo.movies.ResultArch
import com.rodolfo.movies.models.MovieDetail
import com.rodolfo.movies.models.RetornoBusca
import com.rodolfo.movies.retrofit.Requests
import com.rodolfo.movies.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.standalone.KoinComponent


interface MoviesRepository {

    suspend fun searchMovie(name: String): ResultArch<RetornoBusca>?

    suspend fun getMovie(imdbId: String): ResultArch<MovieDetail>?

}

class AppMoviesRepository(private val service: Requests) : MoviesRepository, KoinComponent {

    override suspend fun searchMovie(name: String) = withContext(Dispatchers.IO) {
        try {
            val response = service.searchMovie(Constants.API_KEY, name, "movie").execute()
            response.body()?.let {
                ResultArch.Success(it)
            } ?: ResultArch.Error<RetornoBusca>(IllegalStateException("Deu ruim"))
        } catch (error: Exception) {
            ResultArch.Error<RetornoBusca>(error)
        }
    }

    override suspend fun getMovie(imdbId: String) = withContext(Dispatchers.IO) {
        try {
            val response = service.getMovie(Constants.API_KEY, imdbId).execute()
            response.body()?.let {
                ResultArch.Success(it)
            } ?: ResultArch.Error<MovieDetail>(IllegalStateException("Deu ruim"))
        } catch (error: Exception) {
            ResultArch.Error<MovieDetail>(error)
        }
    }
}
