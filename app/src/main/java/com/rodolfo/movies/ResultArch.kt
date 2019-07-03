package com.rodolfo.movies

sealed class ResultArch<T> {
    class Success<T>(val data: T) : ResultArch<T>()
    class Error<T>(val data: Exception) : ResultArch<T>()
}