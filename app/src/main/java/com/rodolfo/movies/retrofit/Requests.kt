package com.rodolfo.movies.retrofit

import com.rodolfo.movies.models.MovieDetail
import com.rodolfo.movies.models.RetornoBusca
import kotlinx.coroutines.Deferred

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Requests {

    @GET("/")
    fun searchMovie(@Query("apikey") apiKey: String,
                    @Query("s") name: String,
                    @Query("type") type: String): Call<RetornoBusca>

    @GET("/")
    fun getMovie(@Query("apikey") apiKey: String,
                 @Query("i") imdbId: String): Call<MovieDetail>
}
