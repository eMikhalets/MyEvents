package com.emikhalets.mydates.data.database

sealed class CompleteResult<out T> {
    object Complete : CompleteResult<Nothing>()
    data class Error<T>(
        val exception: Exception? = null,
        val message: String = exception?.message.toString()
    ) : CompleteResult<T>()
}