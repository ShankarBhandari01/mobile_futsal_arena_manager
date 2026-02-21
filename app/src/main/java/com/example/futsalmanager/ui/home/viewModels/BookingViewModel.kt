package com.example.futsalmanager.ui.home.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.futsalmanager.core.utils.Common.toDisplayDate
import com.example.futsalmanager.core.utils.Common.toDisplayTime
import com.example.futsalmanager.domain.usecase.BookingUseCase
import com.example.futsalmanager.ui.home.booking.BookingEffect
import com.example.futsalmanager.ui.home.booking.BookingIntent
import com.example.futsalmanager.ui.home.booking.BookingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val useCase: BookingUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val arenaId = savedStateHandle.get<String>("id") ?: ""

    private val _state = MutableStateFlow(BookingState(isLoading = true))
    val state = _state.asStateFlow()


    private val _effect = Channel<BookingEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        loadArenaAndCourts()
        observeArenaWithCourts()
        observeSlotRequests()
        observeDisplayTime()
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

    private fun observeSlotRequests() {
        viewModelScope.launch {
            _state
                .map { it.selectedCourt?.id to it.selectedDate }
                .distinctUntilChanged()
                .collect { (courtId, date) ->

                    if (courtId == null) return@collect

                    loadSlots(courtId, date)
                }
        }
    }

    private suspend fun loadSlots(courtId: String, date: LocalDate) {

        val subDomain = _state.value.arena?.subdomain ?: return

        useCase.getCourtSlots(
            subDomain = subDomain,
            courtId = courtId,
            date = date.toString(),
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

    private fun observeDisplayTime() {
        viewModelScope.launch {
            _state
                .map { it.selectedDate to it.selectedSlot }
                .distinctUntilChanged()
                .collect { (date, slot) ->
                    if (slot == null) return@collect

                    _state.update {
                        it.copy(
                            displayDate = date.toString().toDisplayDate(),
                            displayStartTime = slot.start.toDisplayTime(),
                            displayEndTime = slot.end.toDisplayTime()
                        )
                    }
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
                _state.update { it.copy(selectedCourt = intent.court) }
            }

            is BookingIntent.SelectDate -> {
                _state.update { it.copy(selectedDate = intent.date) }
            }

            is BookingIntent.SetupRecurring -> {

            }

            is BookingIntent.MakePayment -> {

            }
        }
    }
}
