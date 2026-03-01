package com.example.core_data.data.local.session

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.core_domain.domain.session.ISessionStorage
import com.example.core_domain.domain.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject

private const val AUTH_PREF = "auth_pref"

private val Context.authDataStore by preferencesDataStore(AUTH_PREF)

class ISessionDataStore @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val json: Json,
) : ISessionStorage {

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

    override suspend fun saveSession(response: com.example.core_domain.domain.dto.LoginResponse) {
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

    override suspend fun updateSessionTokens(
        accessToken: String,
        refreshToken: String,
        expiresIn: Int
    ) {
        dataStore.edit { pref ->
            cachedToken = accessToken

            pref[Keys.ACCESS] = accessToken
            pref[Keys.REFRESH] = refreshToken
            pref[Keys.EXPIRES] = expiresIn
        }
    }

    override suspend fun getRefreshToken(): String? {
        return dataStore.data.first()[Keys.REFRESH]
    }

    override suspend fun clear() {
        dataStore.edit {
            cachedToken = null
            it.clear()
        }
    }

    override val userFlow: Flow<User?> = dataStore.data
        .map { pref ->
            pref[Keys.USER]?.let { json.decodeFromString<User>(it) }
        }
        .distinctUntilChanged() // only emit if changed
}