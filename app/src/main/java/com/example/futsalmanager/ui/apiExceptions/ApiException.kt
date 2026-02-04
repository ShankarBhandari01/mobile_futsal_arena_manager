package com.example.futsalmanager.ui.apiExceptions

class ApiException(
    override val message: String,
    val type: ApiExceptionTypes? = null
) : Exception(message)
