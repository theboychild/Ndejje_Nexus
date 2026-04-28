package ug.ac.ndejje.nexus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ug.ac.ndejje.nexus.model.SosAlert
import ug.ac.ndejje.nexus.model.SosStatus
import ug.ac.ndejje.nexus.model.User
import ug.ac.ndejje.nexus.repository.SosRepository

sealed class SosUiState {
    object Idle : SosUiState()
    object Sending : SosUiState()
    object Sent : SosUiState()
    data class Error(val message: String) : SosUiState()
}

class SosViewModel(private val repository: SosRepository) : ViewModel() {
    private val _sosState = MutableStateFlow<SosUiState>(SosUiState.Idle)
    val sosState: StateFlow<SosUiState> = _sosState.asStateFlow()

    val allAlerts: StateFlow<List<SosAlert>> = repository.alerts

    fun triggerSos(user: User) {
        viewModelScope.launch {
            _sosState.value = SosUiState.Sending
            delay(2000)
            
            val alert = SosAlert(
                studentName = user.name,
                studentRegNumber = user.regNumber
            )
            repository.addAlert(alert)
            
            _sosState.value = SosUiState.Sent
        }
    }

    fun markAsSafe(user: User) {
        viewModelScope.launch {
            repository.resolveActiveAlertForStudent(user.regNumber)
            _sosState.value = SosUiState.Idle
        }
    }

    fun updateAlertStatus(alertId: String, status: SosStatus) {
        viewModelScope.launch {
            repository.updateAlertStatus(alertId, status)
        }
    }

    fun resetState() {
        _sosState.value = SosUiState.Idle
    }
}
