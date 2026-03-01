package com.example.core_data.data.remote.api.impl

import com.example.core_data.data.remote.api.ApiRegistry
import com.example.core_data.data.remote.safe.safeApiCall
import com.example.core_domain.domain.apis.IPaymentApi
import com.example.core_domain.domain.dto.PaymentIntentResponseDTO
import com.example.core_domain.domain.dto.ReservationRequestDTO
import com.example.core_domain.domain.dto.ReservationResponseDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.path
import javax.inject.Inject

class IPaymentApiImpl @Inject constructor(
    private val client: HttpClient
) : IPaymentApi {

    override suspend fun createPayment(bookingId: String): Result<PaymentIntentResponseDTO> {
        return safeApiCall {
            client.post(ApiRegistry.CREATE_PAYMENT) {
                url {
                    path(
                        ApiRegistry.BOOKINGS,
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