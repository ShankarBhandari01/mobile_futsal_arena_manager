package com.example.feature_home.home.viewModels

import androidx.lifecycle.ViewModel
import com.example.core_domain.domain.usecase.RecurringBookingUseCase
import com.example.feature_home.home.booking.recurringBooking.RecurringBookingEffect
import com.example.feature_home.home.booking.recurringBooking.RecurringBookingIntent
import com.example.feature_home.home.booking.recurringBooking.RecurringBookingState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class RecurringBookingViewModel @Inject constructor(
    private val recurringBookingUseCase: RecurringBookingUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(RecurringBookingState())
    val state = _state.asStateFlow()

    private val _effect = Channel<RecurringBookingEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()


    fun dispatch(intent: RecurringBookingIntent) {
        when (intent) {
            is RecurringBookingIntent.OnSlotSelected -> {
                _state.update { it.copy(selectedSlot = intent.slot) }
            }

            is RecurringBookingIntent.OnCourtSelected -> {
                _state.update { it.copy(selectedCourt = intent.court) }
            }

            is RecurringBookingIntent.UpdateFrequency -> {
                _state.update { it.copy(frequency = intent.frequency) }
            }

            is RecurringBookingIntent.UpdatePaymentMethod -> {
                _state.update { it.copy(selectedPaymentStyle = intent.paymentMethod) }
            }

            is RecurringBookingIntent.UpdateSessionCount -> {
                _state.update { it.copy(sessionCount = intent.sessionCount) }
            }

            is RecurringBookingIntent.OnDaySelected -> {
                _state.update { it.copy(selectedDay = intent.day) }

            }

            is RecurringBookingIntent.OnTimeSelected -> {
                _state.update { it.copy(selectedTime = intent.time) }

            }

            is RecurringBookingIntent.Submit -> {

            }
        }

    }

}