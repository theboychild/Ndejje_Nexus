package ug.ac.ndejje.nexus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ug.ac.ndejje.nexus.repository.Reservation
import ug.ac.ndejje.nexus.repository.ReservationStatus
import ug.ac.ndejje.nexus.repository.ShuttleRepository

sealed class ShuttleUiState {
    object Idle : ShuttleUiState()
    object Loading : ShuttleUiState()
    data class Success(
        val busPosition: LatLng,
        val eta: Int,
    ) : ShuttleUiState()
    data class Error(val message: String) : ShuttleUiState()
}

class ShuttleViewModel(private val repository: ShuttleRepository) : ViewModel() {
    private val _shuttleState = MutableStateFlow<ShuttleUiState>(ShuttleUiState.Idle)
    val shuttleState: StateFlow<ShuttleUiState> = _shuttleState

    val reservations: StateFlow<List<Reservation>> = repository.reservations
    val schedules: StateFlow<List<Pair<String, String>>> = repository.schedules

    init {
        observeShuttle()
    }

    private fun observeShuttle() {
        viewModelScope.launch {
            _shuttleState.value = ShuttleUiState.Loading
            val result = repository.getLiveShuttleInfo()
            if (result.isSuccess) {
                val info = result.getOrNull()
                if (info != null) {
                    _shuttleState.value = ShuttleUiState.Success(
                        busPosition = info.position,
                        eta = info.eta
                    )
                } else {
                    _shuttleState.value = ShuttleUiState.Error("No shuttle currently active.")
                }
            } else {
                _shuttleState.value = ShuttleUiState.Error("Failed to track shuttle.")
            }
        }
    }

    fun addSchedule(route: String, time: String) {
        viewModelScope.launch {
            repository.addSchedule(route, time)
        }
    }

    fun makeReservation(studentName: String, route: String) {
        viewModelScope.launch {
            repository.makeReservation(studentName, route)
        }
    }

    fun updateReservationStatus(id: String, status: ReservationStatus) {
        viewModelScope.launch {
            repository.updateReservationStatus(id, status)
        }
    }
}
