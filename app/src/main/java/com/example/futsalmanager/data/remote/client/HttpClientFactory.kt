package com.example.futsalmanager.data.remote.client

import com.example.futsalmanager.core.utils.AuthHeaderPlugin
import com.example.futsalmanager.core.utils.NetworkConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {

    fun create(tokenProvider: () -> String?): HttpClient {

        return HttpClient(OkHttp) {

            expectSuccess = true

            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                })
            }

            install(Logging) {
                level = LogLevel.ALL
                logger = object : io.ktor.client.plugins.logging.Logger {
                    override fun log(message: String) {
                        android.util.Log.d("KtorClient", message)
                    }
                }
            }

            install(DefaultRequest) {
                url(NetworkConfig.BASE_URL)
                contentType(ContentType.Application.Json)
            }

            install(HttpTimeout) {
                requestTimeoutMillis = NetworkConfig.TIMEOUT
                connectTimeoutMillis = NetworkConfig.TIMEOUT
            }
            //  auto attach token
            install(AuthHeaderPlugin) {
                this.tokenProvider = tokenProvider
            }

        }
    }
}
