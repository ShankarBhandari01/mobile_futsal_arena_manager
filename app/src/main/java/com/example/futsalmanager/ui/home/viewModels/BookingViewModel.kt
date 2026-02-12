package com.example.futsalmanager.ui.home.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.futsalmanager.domain.usecase.HomeUseCase
import com.example.futsalmanager.ui.home.booking.BookingEffect
import com.example.futsalmanager.ui.home.booking.BookingIntent
import com.example.futsalmanager.ui.home.booking.BookingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val useCase: HomeUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val arenaId = savedStateHandle.get<String>("id") ?: ""

    private val _state = MutableStateFlow(BookingState(isLoading = true))
    val state: StateFlow<BookingState> = _state

    private val _effect = Channel<BookingEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        loadArenaAndCourts()
        observeArenaWithCourts()
    }

    private fun observeCourtSlots() = viewModelScope.launch {
        useCase.getCourtSlots(
            subDomain = _state.value.arena?.subdomain ?: "sitapaila",
            courtId = _state.value.selectedCourt?.id ?: "e34d9e0-1585-497b-9c32-8ad78a8de6da",
            date = _state.value.selectedDate,
            includeStatus = true
        ).fold(
            onSuccess = { slots ->
                _state.update { it.copy(availableSlots = slots) }
            },
            onFailure = {
                _effect.send(
                    BookingEffect.ShowError(it.message ?: "Failed to load court slots")
                )
            }
        )
    }

    private fun loadArenaAndCourts() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }
        try {
            useCase.refreshArenaDetailsWithCourts(arenaId)
        } catch (e: Exception) {
            _effect.send(
                BookingEffect.ShowError(e.message ?: "Failed to load arena")
            )
        } finally {
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun observeArenaWithCourts() = viewModelScope.launch {
        useCase.arenaById(arenaId)
            .collect { arenaWithCourts ->
                _state.update {
                    it.copy(
                        arena = arenaWithCourts?.arena,
                        courts = arenaWithCourts?.courts ?: emptyList()
                    )
                }
            }


    }

    fun dispatch(intent: BookingIntent) {
        when (intent) {
            is BookingIntent.SelectSlot -> {
                _state.update { it.copy(selectedSlot = intent.slot) }
            }

            is BookingIntent.BookingButton -> {
                // handle booking click
            }

            is BookingIntent.SelectPaymentMethod -> {
                _state.update { it.copy(selectedPaymentMethod = intent.method) }
            }

            is BookingIntent.SelectCourt -> {
                _state.update { it.copy(selectedCourt = intent.courtId) }
                observeCourtSlots()
            }

            is BookingIntent.SelectDate -> {
                _state.update { it.copy(selectedDate = intent.date.toString()) }
            }

        }
    }
}
