package com.urlshortener.urlshortener.model

sealed class OperationResult<out T> {
    data class Success<out T>(val data: T) : OperationResult<T>()
    data class Failure(val reason: String) : OperationResult<Nothing>()
}
