package com.example.futsalmanager.core.utils

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.http.HttpHeaders
import io.ktor.util.AttributeKey

class AuthHeaderPlugin (
    val tokenProvider: () -> String?
) {

    class Config {
        var tokenProvider: () -> String? = { null }
    }

    companion object : HttpClientPlugin<Config, AuthHeaderPlugin> {
        override val key = AttributeKey<AuthHeaderPlugin>("AuthHeaderPlugin")

        override fun prepare(block: Config.() -> Unit): AuthHeaderPlugin {
            val config = Config().apply(block)
            return AuthHeaderPlugin(config.tokenProvider)
        }

        override fun install(plugin: AuthHeaderPlugin, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.State) {
                plugin.tokenProvider()?.let {
                    context.headers.append(
                        HttpHeaders.Authorization,
                        "Bearer $it"
                    )
                }
            }
        }
    }
}


