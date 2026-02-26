package com.example.futsalmanager.ui.home.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.futsalmanager.domain.model.FilterParams
import com.example.futsalmanager.domain.model.LocationModel
import com.example.futsalmanager.domain.model.User
import com.example.futsalmanager.domain.usecase.HomeUseCase
import com.example.futsalmanager.ui.home.HomeEffect
import com.example.futsalmanager.ui.home.HomeIntent
import com.example.futsalmanager.ui.home.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: HomeUseCase,
) : ViewModel() {
    private val _isScreenActive = MutableStateFlow(false)
    private val _state = MutableStateFlow(HomeState())

    val state: StateFlow<HomeState> = combine(_state, useCase.arenas) { currentState, arenaList ->
        currentState.copy(arenaList = arenaList)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeState()
    )

    private val _effect = Channel<HomeEffect>(capacity = Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        observeLocationState()
        observeSearchAndFilters()
    }

    val user: StateFlow<User?> = useCase.userFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    private fun observeLocationState() {
        _isScreenActive
            .flatMapLatest { active ->
                if (!active) return@flatMapLatest flowOf(state.value.location)
                combine(
                    useCase.observerLocationStatus,
                    _state.map { it.isPermissionGranted }.distinctUntilChanged()
                ) { isEnabled, isGranted -> isEnabled to isGranted }
                    .onEach { (isEnabled, _) ->
                        _state.update { it.copy(isLocationEnabled = isEnabled) }
                    }
                    .flatMapLatest { (isEnabled, isGranted) ->
                        if (isEnabled && isGranted) useCase.userLocation
                        else flowOf(null)
                    }
            }
            .onEach { loc -> _state.update { it.copy(location = loc) } }
            .launchIn(viewModelScope)
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchAndFilters() {
        _state.map {
            FilterParams(
                query = it.search,
                date = it.date,
                location = it.location,
                isEnabled = it.isLocationEnabled
            )
        }
            .distinctUntilChanged()
            .debounce(500)
            .onEach { params ->
                loadArenaList(
                    query = params.query,
                    date = params.date,
                    location = params.location,
                    isLocationEnabled = params.isEnabled
                )
            }
            .launchIn(viewModelScope)
    }

    fun dispatch(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.ScreenStarted -> _isScreenActive.value = true
            is HomeIntent.ScreenStopped -> _isScreenActive.value = false
            is HomeIntent.LoadNextPage -> loadNextPage()

            is HomeIntent.ViewModeChanged ->
                _state.update { it.copy(viewMode = intent.viewMode) }

            is HomeIntent.SearchChanged ->
                _state.update { it.copy(search = intent.query, offset = 0) }

            is HomeIntent.DateChanged ->
                _state.update { it.copy(date = intent.date, offset = 0) }

            is HomeIntent.Refresh -> viewModelScope.launch {
                _state.update { it.copy(offset = 0) }
                val s = _state.value
                loadArenaList(s.search, s.date, s.location, s.isLocationEnabled)
            }

            is HomeIntent.EnableLocationClicked -> viewModelScope.launch {
                _effect.send(HomeEffect.NavigateToLocationSettings)
            }

            is HomeIntent.LogoutClicked ->
                _state.update { it.copy(showLogoutDialog = true) }

            is HomeIntent.DismissLogoutDialog ->
                _state.update { it.copy(showLogoutDialog = false) }

            is HomeIntent.ArenaClicked -> viewModelScope.launch {
                _effect.send(HomeEffect.NavigateToBookingWithArea(intent.arena))
            }

            is HomeIntent.OnPermissionsGranted ->
                _state.update { it.copy(isPermissionGranted = true) }

            HomeIntent.MarketPlaceClicked,
            HomeIntent.MyBookingClicked,
            HomeIntent.MyProfileClicked,
            HomeIntent.ConfirmLogout -> handleNavigation(intent)
        }
    }

    private fun loadNextPage() {
        val currentState = _state.value
        val nextOffset = currentState.offset + currentState.limit
        _state.update { it.copy(offset = nextOffset) }

        viewModelScope.launch {
            loadArenaList(
                query = currentState.search,
                date = currentState.date,
                location = currentState.location,
                isLocationEnabled = currentState.isLocationEnabled
            )
        }
    }

    private fun handleNavigation(intent: HomeIntent) = viewModelScope.launch {
        val effect = when (intent) {
            HomeIntent.MarketPlaceClicked -> HomeEffect.NavigateToMarketPlace
            HomeIntent.MyBookingClicked -> HomeEffect.NavigateToMyBooking
            HomeIntent.MyProfileClicked -> HomeEffect.NavigateToMyProfile
            HomeIntent.ConfirmLogout -> {
               // _state.update { it.copy(showLogoutDialog = false) }
                HomeEffect.NavigateToLogin
            }

            else -> null
        }
        effect?.let { _effect.send(it) }
    }

    private suspend fun loadArenaList(
        query: String,
        date: String,
        location: LocationModel?,
        isLocationEnabled: Boolean = false,
    ) {
        _state.update { it.copy(isLoading = true) }
        try {
            val response = useCase.getArenaListFromApi(
                search = query,
                offset = _state.value.offset,
                limit = _state.value.limit,
                date = date,
                location = location,
                isGpsEnabled = isLocationEnabled
            )
            response.onFailure { error ->
                _effect.send(HomeEffect.ShowError(error.message ?: "Sync Failed"))
            }
        } catch (e: Exception) {
            _effect.send(HomeEffect.ShowError(e.message ?: "Sync Failed"))
        } finally {
            _state.update { it.copy(isLoading = false) }
        }
    }
}