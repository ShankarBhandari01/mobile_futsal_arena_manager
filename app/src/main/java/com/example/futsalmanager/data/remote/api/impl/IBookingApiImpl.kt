package com.example.futsalmanager.data.remote.api.impl

import com.example.futsalmanager.data.remote.api.ApiRegistry
import com.example.futsalmanager.data.remote.api.IBookingApi
import com.example.futsalmanager.data.remote.dto.CourtDto
import com.example.futsalmanager.data.remote.safe.safeApiCall
import com.example.futsalmanager.domain.model.Arenas
import com.example.futsalmanager.domain.model.Slot
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.path
import javax.inject.Inject

class IBookingApiImpl @Inject constructor(
    private val client: HttpClient
) : IBookingApi {

    override suspend fun getCourtSlots(
        subDomain: String,
        courtId: String,
        date: String,
        includeStatus: Boolean
    ): Result<List<Slot>> {
        return safeApiCall {
            client.get(ApiRegistry.ARENA_LIST) {
                url {
                    path(
                        ApiRegistry.ARENA_LIST,
                        subDomain,
                        ApiRegistry.COURTS,
                        courtId,
                        ApiRegistry.SLOT
                    )
                    parameter("date", date)
                    parameter("includeStatus", includeStatus)
                }
            }.body<List<Slot>>()
        }
    }

    override suspend fun arenasSubDomainCourts(subDomain: String): Result<List<CourtDto>> {
        return safeApiCall {
            client.get(ApiRegistry.ARENA_LIST) {
                url {
                    path(ApiRegistry.ARENA_LIST, subDomain, ApiRegistry.COURTS)
                }
            }.body<List<CourtDto>>()
        }
    }

    override suspend fun arenaSubDomain(subDomain: String): Result<Arenas> {
        return safeApiCall {
            client.get(ApiRegistry.ARENA_LIST) {
                url {
                    path(ApiRegistry.ARENA_LIST, subDomain)
                }
            }.body<Arenas>()
        }
    }


}