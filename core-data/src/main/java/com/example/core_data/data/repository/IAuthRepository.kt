package com.example.core_data.data.repository

import com.example.core_data.data.model.User
import com.example.core_data.data.remote.api.IAuthApi
import kotlinx.coroutines.flow.Flow


interface IAuthRepository : IAuthApi {
    // data store apis
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun clear(isAutoLogout: Boolean = false)
    val getUser: Flow<User?>
}
