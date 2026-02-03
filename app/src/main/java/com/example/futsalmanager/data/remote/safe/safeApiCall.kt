package com.example.futsalmanager.data.remote.safe

import android.util.Log
import com.example.futsalmanager.data.remote.dto.ApiError
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
            val apiError = Json.decodeFromString<ApiError>(text)
            apiError.message

        } catch (ex: Exception) {
            e.localizedMessage ?: "Client request error"
        }
        Result.failure(Exception(errorMessage))
    } catch (e: ServerResponseException) { // 5xx
        val errorMessage = try {
            e.response.bodyAsText()
        } catch (ex: Exception) {
            e.localizedMessage ?: "Server response error"
        }
        Result.failure(Exception(errorMessage))
    } catch (e: IOException) {
        Log.e("safeApiCall", "IOException", e.fillInStackTrace())
        Result.failure(Exception(e.localizedMessage ?: "Network error"))
    } catch (e: Exception) {
        Result.failure(e)
    }
}
