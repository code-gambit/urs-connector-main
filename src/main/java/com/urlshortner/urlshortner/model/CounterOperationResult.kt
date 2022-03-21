package com.urlshortner.urlshortner.model

sealed class CounterOperationResult<out T> {
    data class Success<out T>(val data: T) : CounterOperationResult<T>()
    data class Failure(val reason: String) : CounterOperationResult<Nothing>()
    object RangeExhausted : CounterOperationResult<Nothing>()
}
