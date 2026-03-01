package com.example.core_domain.domain.repository


import com.example.core_domain.domain.apis.IAuthApi
import com.example.core_domain.domain.model.User
import kotlinx.coroutines.flow.Flow


interface IAuthRepository : IAuthApi {
    // data store apis
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun clear(isAutoLogout: Boolean = false)
    val getUser: Flow<User?>
}
