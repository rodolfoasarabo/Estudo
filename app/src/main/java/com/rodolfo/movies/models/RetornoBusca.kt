package com.rodolfo.movies.models

import java.io.Serializable

class RetornoBusca(val Search: List<Movies>?, val totalResults: String?, val Response: String?) : Serializable
