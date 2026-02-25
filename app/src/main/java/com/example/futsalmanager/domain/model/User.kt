package com.example.futsalmanager.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val activeSessionId: String?="",
    val avatarUrl: String? = "",
    val createdAt: String?="",
    val deletedAt: String? = "",
    val email: String? = "",
    val emailVerified: Boolean,
    val emailVerifiedAt: String? = "",
    val failedLoginAttempts: Int?=0,
    val firstName: String? = "",
    val id: String? = "",
    val isActive: Boolean,
    val lastLoginAt: String? = "",
    val lastLoginIp: String? = "",
    val lastName: String? = "",
    val lockedUntil: String? = "",
    val phone: String? = "",
    val platformRole: String? = "",
    val preferences: Preferences,
    val role: String? = "",
    val scope: String? = "",
    val stripeCustomerId: String? = "",
    val updatedAt: String? = ""
)