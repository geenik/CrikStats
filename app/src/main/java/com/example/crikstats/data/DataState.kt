package com.example.crikstats.data

sealed class DataState<out T> {
    class Success<out T>(val data: T) : DataState<T>()
    class Error(val exception: String) : DataState<Nothing>()
    object Loading : DataState<Nothing>()
}