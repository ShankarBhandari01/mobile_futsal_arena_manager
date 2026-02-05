package com.example.futsalmanager.data.local.session

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.futsalmanager.data.remote.dto.LoginResponse
import com.example.futsalmanager.domain.model.User
import com.example.futsalmanager.domain.session.SessionStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject

private const val AUTH_PREF = "auth_pref"

private val Context.authDataStore by preferencesDataStore(AUTH_PREF)

class SessionDataStore @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val json: Json,
) : SessionStorage {

    private var cachedToken: String? = null
    private val dataStore = context.authDataStore

    private object Keys {
        val ACCESS = stringPreferencesKey("access_token")
        val REFRESH = stringPreferencesKey("refresh_token")
        val EXPIRES = intPreferencesKey("expires_in")
        val USER = stringPreferencesKey("user")
        val TENANT = stringPreferencesKey("tenant")
        val ARENA = stringPreferencesKey("arena")
    }

    override suspend fun saveSession(response: LoginResponse) {
        dataStore.edit { pref ->
            cachedToken = response.accessToken

            pref[Keys.ACCESS] = response.accessToken
            pref[Keys.REFRESH] = response.refreshToken
            pref[Keys.EXPIRES] = response.expiresIn

            pref[Keys.USER] = json.encodeToString(response.user)
            pref[Keys.TENANT] = response.tenant.toString()
            pref[Keys.ARENA] = response.selectedArena.toString()
        }
    }

    override suspend fun getAccessToken(): String? {
        return cachedToken ?: dataStore.data.first()[Keys.ACCESS].also {
            cachedToken = it
        }
    }

    override suspend fun getRefreshToken(): String? {
        return dataStore.data.first()[Keys.REFRESH]
    }

    override suspend fun clear() {
        dataStore.edit { it.clear() }
    }

    override val userFlow: Flow<User?> = dataStore.data
        .map { pref ->
            pref[Keys.USER]?.let { json.decodeFromString<User>(it) }
        }
        .distinctUntilChanged() // only emit if changed
}