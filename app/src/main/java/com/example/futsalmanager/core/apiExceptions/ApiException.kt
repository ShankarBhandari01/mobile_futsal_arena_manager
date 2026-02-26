package com.example.futsalmanager.core.apiExceptions

class ApiException(
    override val message: String,
    val type: ApiExceptionTypes? = null
) : Exception(message)
