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
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val useCase: HomeUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var arenaId = savedStateHandle.get<String>("id") ?: ""

    private val _state = MutableStateFlow(BookingState())

    val state: StateFlow<BookingState> =
        combine(_state, useCase.areanaById(arenaId)) { currentState, arena ->
            currentState.copy(
                arena = arena,
                isLoading = false
            )
        }.stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5000),
            initialValue = BookingState(isLoading = true)
        )
    private val _effect = Channel<BookingEffect>(capacity = Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()


    fun dispatch(intent: BookingIntent) {
        when (intent) {
            is BookingIntent.BookingButton -> {
            }
        }
    }
}
