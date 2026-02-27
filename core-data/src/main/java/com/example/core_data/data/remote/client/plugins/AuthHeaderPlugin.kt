package com.example.core_data.data.remote.client.plugins

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.http.HttpHeaders
import io.ktor.util.AttributeKey

class AuthHeaderPlugin(
    private val tokenProvider: suspend () -> String?
) {

    class Config {
        var tokenProvider: suspend () -> String? = { null }
    }

    companion object : HttpClientPlugin<Config, AuthHeaderPlugin> {

        override val key = AttributeKey<AuthHeaderPlugin>("AuthHeaderPlugin")

        override fun prepare(block: Config.() -> Unit): AuthHeaderPlugin {
            val config = Config().apply(block)
            return AuthHeaderPlugin(config.tokenProvider)
        }

        override fun install(plugin: AuthHeaderPlugin, scope: HttpClient) {

            scope.requestPipeline.intercept(HttpRequestPipeline.Phases.State) {

                val token = plugin.tokenProvider()

                token?.let {
                    context.headers.remove(HttpHeaders.Authorization)
                    context.headers.append(HttpHeaders.Authorization, "Bearer $it")
                }

                proceed()
            }
        }
    }
}