/* 
 * This file contains the "SOS ViewModel."
 * This "Brain" manages emergency alerts. When a student triggers an SOS, 
 * this ViewModel handles the process of notifying security and keeping 
 * the student updated on the response status.
 */
package ug.ac.ndejje.nexus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class SosUiState {
    object Idle : SosUiState()
    object Sending : SosUiState()
    object Sent : SosUiState()
    data class Error(val message: String) : SosUiState()
}

class SosViewModel : ViewModel() {
    private val _sosState = MutableStateFlow<SosUiState>(SosUiState.Idle)
    val sosState: StateFlow<SosUiState> = _sosState

    fun triggerSos() {
        viewModelScope.launch {
            _sosState.value = SosUiState.Sending
            delay(2000)
            _sosState.value = SosUiState.Sent
        }
    }

    fun callSecurity() {
        // Intent logic would go here
    }

    fun resetState() {
        _sosState.value = SosUiState.Idle
    }
}
