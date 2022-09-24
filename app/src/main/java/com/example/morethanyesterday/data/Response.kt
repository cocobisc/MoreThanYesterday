package com.example.morethanyesterday.data

sealed class Response<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T?) : Response<T>(data)
    class Fail<T>(data: T?, message: String) : Response<T>(data, message)
}