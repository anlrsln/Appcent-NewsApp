package com.example.newsapp.util

// success-error ve loading state'ler için
sealed class Resource <T>(
    val data:T? = null,
    val message:String? = null,
    ) {

    // Succes cevabında data, Resource'a gönderilecektir
    class Success<T>(data:T) : Resource<T>(data)
    class Error<T>(message:String, data: T?=null) : Resource<T>(data,message)
    class Loading<T> : Resource<T>()
}