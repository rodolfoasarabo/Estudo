package com.rodolfo.movies.models

import java.io.Serializable

data class Movies(val Title: String?, val Year: String?, val imdbID: String?,
                  val Type: String?, val Poster: String?) : Serializable

