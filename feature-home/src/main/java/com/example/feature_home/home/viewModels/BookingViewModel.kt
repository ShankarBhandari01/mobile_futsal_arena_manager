package com.example.feature_home.home.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_domain.domain.dto.ReservationRequestDTO
import com.example.core_domain.domain.model.emum.PaymentMethod
import com.example.core_domain.domain.usecase.BookingUseCase
import com.example.core_uitls.utils.Common
import com.example.core_uitls.utils.Common.toDisplayDate
import com.example.core_uitls.utils.Common.toDisplayTime
import com.example.feature_home.home.booking.BookingEffect
import com.example.feature_home.home.booking.BookingIntent
import com.example.feature_home.home.booking.BookingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
                        arena = arenaWithCourts.arena,
                        courts = arenaWithCourts.courts,
                        selectedCourt = arenaWithCourts.courts.firstOrNull()
                    )
                }
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeSlotRequests() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            _state
                .map { Triple(it.arena?.subdomain, it.selectedCourt?.id, it.selectedDate) }
                .distinctUntilChanged()
                .flatMapLatest { (subDomain, courtId, date) ->
                    if (subDomain == null || courtId == null) {
                        emptyFlow()
                    } else {
                        useCase.invoke(
                            subDomain = subDomain,
                            courtId = courtId,
                            date = date.toString(),
                            includeStatus = true
                        )
                    }
                }
                .collect { result ->
                    result.fold(
                        onSuccess = { slots ->
                            _state.update { current ->
                                val updatedSelectedSlot =
                                    slots.find { it.start == current.selectedSlot?.start && it.end == current.selectedSlot.end }
                                current.copy(
                                    availableSlots = slots,
                                    selectedSlot = updatedSelectedSlot,
                                    isLoading = false
                                )
                            }
                        },
                        onFailure = {
                            _state.update { it.copy(isLoading = false) }
                            _effect.send(
                                BookingEffect.ShowError(
                                    it.message ?: "Failed to load court slots"
                                )
                            )
                        }
                    )
                }
        }

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

            is BookingIntent.SelectPaymentMethod -> {
                _state.update { it.copy(selectedPaymentMethod = intent.method) }
            }

            is BookingIntent.SelectCourt -> {
                _state.update { it.copy(selectedCourt = intent.court) }
            }

            is BookingIntent.SelectDate -> {
                _state.update { it.copy(selectedDate = intent.date) }
            }

            is BookingIntent.MakePayment -> {
                createPaymentIntent()
            }
        }
    }

    fun createPaymentIntent() = viewModelScope.launch {
        try {
            val current = state.value
            _state.update { it.copy(isLoading = true) }

            if (current.selectedPaymentMethod == PaymentMethod.ONLINE) {
                val createSlotReservationDto = ReservationRequestDTO(
                    courtId = current.selectedCourt?.id ?: "",
                    startTime = current.selectedSlot?.start!!,
                    endTime = current.selectedSlot.end,
                    idempotencyKey = Common.generateIdempotencyKey(),
                    paymentMethod = current.selectedPaymentMethod.name.lowercase(),
                    arenaId = current.arena?.id ?: ""
                )

                useCase.createPaymentIntent(createSlotReservationDto).fold(
                    onSuccess = { response ->
                        response.arenas = current.arena!!
                        _effect.send(BookingEffect.ShowPaymentDialog(response))

                    }, onFailure = { error ->
                        _effect.send(
                            BookingEffect.ShowError(
                                error.message ?: "create payment Failed"
                            )
                        )
                    }
                )
            }
        } catch (e: Exception) {
            _effect.send(
                BookingEffect.ShowError(
                    e.message ?: "create payment Failed"
                )
            )
        } finally {
            _state.update { it.copy(isLoading = false) }
        }

    }


}
