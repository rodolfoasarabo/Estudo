package com.rodolfo.movies.models

import java.io.Serializable

data class MovieDetail(
        val Title: String?,
        val Year: String?,
        val Rated: String?,
        val Released: String?,
        val Runtime: String?,
        val Genre: String?,
        val Director: String?,
        val Writer: String?,
        val Actors: String?,
        val Website: String?,
        val imdbRating: String?,
        val Type: String?,
        val imdbVotes: String?,
        val Ratings: Array<Ratings>?,
        val Response: String?,
        val Poster: String?,
        val imdbID: String?,
        val Country: String?,
        val BoxOffice: String?,
        val DVD: String?,
        val Plot: String?,
        val Metascore: String?,
        val Production: String?,
        val Language: String?,
        val Awards: String?) : Serializable
