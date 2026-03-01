package com.example.core_data.data.remote.safe

import android.util.Log
import com.example.core_data.data.remote.api.apiExceptions.ApiException
import com.example.core_data.data.remote.api.apiExceptions.ApiExceptionTypes
import com.example.core_domain.domain.dto.ApiError
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.bodyAsText
import kotlinx.io.IOException
import kotlinx.serialization.json.Json

suspend inline fun <T> safeApiCall(
    crossinline block: suspend () -> T
): Result<T> {
    return try {
        Result.success(block())
    } catch (e: ClientRequestException) { // 4xx
        val errorMessage = try {
            val text = e.response.bodyAsText()
            val apiError = Json.decodeFromString<com.example.core_domain.domain.dto.ApiError>(text)
            apiError
        } catch (ex: Exception) {
            com.example.core_domain.domain.dto.ApiError(
                error = "UNKNOWN",
                message = e.localizedMessage ?: "Client error"
            )
        }

        val type = try {
            ApiExceptionTypes.valueOf(errorMessage.error)
        } catch (ex: Exception) {
            null
        }
        Result.failure(ApiException(errorMessage.message, type))
    } catch (e: ServerResponseException) { // 5xx
        Result.failure(ApiException("Server error: ${e.response.status}", null))
    } catch (e: IOException) {
        Log.e("safeApiCall", "IOException", e.fillInStackTrace())
        Result.failure(ApiException("Network error: ${e.localizedMessage}", null))
    } catch (e: Exception) {
        Result.failure(ApiException(e.localizedMessage ?: "Unknown error", null))
    }
}
