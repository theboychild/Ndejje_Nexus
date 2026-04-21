package ug.ac.ndejje.nexus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EmergencyViewModel : ViewModel() {
    private val _sosState = MutableStateFlow<SOSState>(SOSState.Idle)
    val sosState: StateFlow<SOSState> = _sosState

    fun triggerSOS() {
        viewModelScope.launch {
            _sosState.value = SOSState.Sending
            // Simulate capturing GPS and sending to security
            delay(2000)
            _sosState.value = SOSState.Sent
        }
    }

    fun callSecurity() {
        // This would typically use an Intent to call a number
    }
}

sealed class SOSState {
    object Idle : SOSState()
    object Sending : SOSState()
    object Sent : SOSState()
}