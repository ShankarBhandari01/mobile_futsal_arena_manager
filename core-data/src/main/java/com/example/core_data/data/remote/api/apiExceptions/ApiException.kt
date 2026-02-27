package com.example.core_data.data.remote.api.apiExceptions

class ApiException(
    override val message: String,
    val type: ApiExceptionTypes? = null
) : Exception(message)
