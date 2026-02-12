package com.example.futsalmanager.ui.home.booking

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.futsalmanager.domain.model.Arenas
import com.example.futsalmanager.domain.model.Courts
import com.example.futsalmanager.domain.model.PaymentMethod
import com.example.futsalmanager.domain.model.Slot
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
data class BookingState(
    val isLoading: Boolean = false,
    val arena: Arenas? = null,
    val courts: List<Courts?> = emptyList(),
    val showLogoutDialog: Boolean = false,
    val selectedDate: String = LocalDate.now().toString(),
    val errorMessage: String? = null,
    val selectedPaymentMethod: PaymentMethod = PaymentMethod.ONLINE,
    val availableSlots: List<Slot> = emptyList(),
    val selectedSlot: Slot? = null,
    val selectedCourt: Courts? = null
)