package com.example.futsalmanager.ui.home.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.futsalmanager.domain.usecase.HomeUseCase
import com.example.futsalmanager.ui.home.HomeEffect
import com.example.futsalmanager.ui.home.HomeIntent
import com.example.futsalmanager.ui.home.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: HomeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _effect = Channel<HomeEffect>(capacity = Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        observeSearchAndFilters()
    }


    @OptIn(FlowPreview::class)
    private fun observeSearchAndFilters() {
        // Observe search and date changes reactively
        state
            .map { it.search to it.date }
            .distinctUntilChanged()
            .debounce(500)
            .onEach { (query, date) ->
                loadArenaList(query, date)
            }
            .launchIn(viewModelScope)
    }

    fun dispatch(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.SearchChanged -> {
                _state.update { it.copy(search = intent.query, offset = 0) }
            }

            is HomeIntent.DateChanged -> {
                _state.update { it.copy(date = intent.date, offset = 0) }
            }

            is HomeIntent.Refresh -> viewModelScope.launch {
                loadArenaList(_state.value.search, _state.value.date)
            }

            HomeIntent.MarketPlaceClicked -> _effect.trySend(HomeEffect.NavigateToMarketPlace)
            HomeIntent.MyBookingClicked -> _effect.trySend(HomeEffect.NavigateToMyBooking)
            HomeIntent.MyProfileClicked -> _effect.trySend(HomeEffect.NavigateToMyProfile)
            HomeIntent.LogoutClicked -> _effect.trySend(HomeEffect.NavigateToLogin)
        }
    }

    private suspend fun loadArenaList(query: String, date: String) {
        _state.update { it.copy(isLoading = true) }

        useCase.getArenaList(
            search = query,
            offset = _state.value.offset,
            limit = _state.value.limit,
            date = date
        ).fold(
            onSuccess = { result ->
                _state.update { it.copy(arenaList = result.arenas) }
            },
            onFailure = { error ->
                _effect.send(HomeEffect.ShowError(error.message ?: "Network Error"))
            }
        )
        _state.update { it.copy(isLoading = false) }
    }
}