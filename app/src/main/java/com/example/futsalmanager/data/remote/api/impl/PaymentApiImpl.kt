package com.example.futsalmanager.data.remote.api.impl

import com.example.futsalmanager.data.remote.api.ApiRegistry
import com.example.futsalmanager.data.remote.api.PaymentApi
import com.example.futsalmanager.data.remote.dto.PaymentIntentResponseDTO
import com.example.futsalmanager.data.remote.dto.ReservationRequestDTO
import com.example.futsalmanager.data.remote.dto.ReservationResponseDTO
import com.example.futsalmanager.data.remote.safe.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.path
import javax.inject.Inject

class PaymentApiImpl @Inject constructor(
    private val client: HttpClient
) : PaymentApi {

    override suspend fun createPayment(bookingId: String): Result<PaymentIntentResponseDTO> {
        return safeApiCall {
            client.post(ApiRegistry.CREATE_PAYMENT) {
                url {
                    path(
                        bookingId,
                        ApiRegistry.CREATE_PAYMENT
                    )
                }
            }.body<PaymentIntentResponseDTO>()
        }
    }

    override suspend fun reserve(request: ReservationRequestDTO): Result<ReservationResponseDTO> {
        return safeApiCall {
            client.post(ApiRegistry.BOOKINGS) {
                url {
                    path(
                        ApiRegistry.BOOKINGS,
                        ApiRegistry.RESERVE
                    )
                }
                setBody(request)
            }.body<ReservationResponseDTO>()
        }
    }
}