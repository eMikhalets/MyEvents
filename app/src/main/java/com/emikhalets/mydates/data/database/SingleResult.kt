package com.emikhalets.mydates.data.database

sealed class SingleResult<out T> {
    data class Success<T>(val data: T) : SingleResult<T>()
    data class Error<T>(
        val exception: Exception? = null,
        val message: String = exception?.message.toString()
    ) : SingleResult<T>()
}