package com.emikhalets.mydates.data.database

sealed class ListResult<out T : Any> {
    object EmptyList : ListResult<Nothing>()
    data class Success<out T : Any>(val data: T) : ListResult<T>()
    data class Error(
        val exception: Exception? = null,
        val message: String = exception?.message.toString()
    ) : ListResult<Nothing>()
}