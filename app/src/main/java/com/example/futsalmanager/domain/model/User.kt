package com.example.futsalmanager.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class User(
    val activeSessionId: String,
    val avatarUrl: JsonElement?,
    val createdAt: String,
    val deletedAt: JsonElement,
    val email: String,
    val emailVerified: Boolean,
    val emailVerifiedAt: JsonElement,
    val failedLoginAttempts: Int,
    val firstName: String,
    val id: String,
    val isActive: Boolean,
    val lastLoginAt: String,
    val lastLoginIp: JsonElement,
    val lastName: String,
    val lockedUntil: JsonElement,
    val phone: String,
    val platformRole: JsonElement,
    val preferences: Preferences,
    val role: String,
    val scope: String,
    val stripeCustomerId: JsonElement,
    val updatedAt: String
)