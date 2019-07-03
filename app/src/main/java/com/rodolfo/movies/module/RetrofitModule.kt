package com.rodolfo.movies.module

import com.rodolfo.movies.repository.AppMoviesRepository
import com.rodolfo.movies.repository.MoviesRepository
import com.rodolfo.movies.retrofit.Requests
import com.rodolfo.movies.utils.Constants
import com.rodolfo.movies.view.viewmodel.DetailsViewModel
import com.rodolfo.movies.view.viewmodel.SearchViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val moviesModule = module {

    factory {
        Retrofit.Builder()
                .baseUrl(Constants.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(Requests::class.java)
    }

    factory { AppMoviesRepository(get()) }

    viewModel { SearchViewModel(get()) }
    viewModel { DetailsViewModel(get()) }
}