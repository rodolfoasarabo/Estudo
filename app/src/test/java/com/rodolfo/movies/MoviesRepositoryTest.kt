package com.rodolfo.movies

import com.rodolfo.movies.models.RetornoBusca
import com.rodolfo.movies.repository.MoviesRepository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test

class MoviesRepositoryTest {

    @MockK
    private lateinit var moviesRepository: MoviesRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun getMovie_shouldReturnMoviesList() {
        val expectedResult = RetornoBusca(null, null, "Deu ruim")
    }

}