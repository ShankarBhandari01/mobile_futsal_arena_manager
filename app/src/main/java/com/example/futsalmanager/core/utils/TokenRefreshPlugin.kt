package com.example.futsalmanager.core.utils

import com.example.futsalmanager.data.remote.api.AuthApi
import com.example.futsalmanager.domain.session.SessionStorage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.plugin
import io.ktor.client.request.bearerAuth
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.util.AttributeKey
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class TokenRefreshPlugin(
    private val sessionStorage: SessionStorage,
    private val getAuthApi: () -> AuthApi,  // lazy to avoid circular dependency
    private val onLogout: () -> Unit,
) {

    class Config {
        var sessionStorage: SessionStorage? = null
        var getAuthApi: () -> AuthApi = { error("AuthApi not provided") }
        var onLogout: () -> Unit = {}
    }

    companion object : HttpClientPlugin<Config, TokenRefreshPlugin> {
        override val key = AttributeKey<TokenRefreshPlugin>("TokenRefreshPlugin")
        private val refreshMutex = Mutex()
        val IS_REFRESH_REQUEST = AttributeKey<Boolean>("IsRefreshRequest")

        override fun prepare(block: Config.() -> Unit): TokenRefreshPlugin {
            val config = Config().apply(block)
            return TokenRefreshPlugin(
                sessionStorage = requireNotNull(config.sessionStorage),
                getAuthApi = config.getAuthApi,
                onLogout = config.onLogout
            )
        }

        override fun install(plugin: TokenRefreshPlugin, scope: HttpClient) {
            scope.plugin(HttpSend).intercept { request ->

                if (request.attributes.getOrNull(IS_REFRESH_REQUEST) == true) {
                    return@intercept execute(request)
                }

                val originalCall = execute(request)

                if (originalCall.response.status != HttpStatusCode.Unauthorized) {
                    return@intercept originalCall
                }

                val refreshed = refreshMutex.withLock {
                    // If another coroutine already refreshed while we waited, skip
                    val currentToken = plugin.sessionStorage.getAccessToken()
                    val requestToken = request.headers[HttpHeaders.Authorization]
                        ?.removePrefix("Bearer ")

                    if (currentToken != null && currentToken != requestToken) {
                        true // already refreshed by someone else
                    } else {
                        tryRefresh(plugin)
                    }
                }

                if (refreshed) {
                    val newToken = plugin.sessionStorage.getAccessToken()
                    request.headers.remove(HttpHeaders.Authorization)
                    request.bearerAuth(newToken ?: "")
                    execute(request)
                } else {
                    plugin.onLogout()
                    originalCall
                }
            }
        }

        private suspend fun tryRefresh(plugin: TokenRefreshPlugin): Boolean {
            val refreshToken = plugin.sessionStorage.getRefreshToken() ?: return false

            // Use  existing AuthApi.refresh — clean and consistent
            return plugin.getAuthApi()
                .refresh(refreshToken)
                .fold(
                    onSuccess = { response ->
                        plugin.sessionStorage.saveSession(response)
                        true
                    },
                    onFailure = {
                        plugin.sessionStorage.clear()
                        false
                    }
                )
        }
    }
}