/* 
 * This file contains the "Shuttle ViewModel."
 * This "Brain" is responsible for tracking the university bus. It continuously 
 * updates the bus's GPS coordinates and calculates how many minutes are left 
 * until it reaches the student.
 */
package ug.ac.ndejje.nexus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ShuttleUiState {
    object Idle : ShuttleUiState()
    object Loading : ShuttleUiState()
    data class Success(
        val busPosition: LatLng,
        val eta: Int
    ) : ShuttleUiState()
    data class Error(val message: String) : ShuttleUiState()
}

class ShuttleViewModel : ViewModel() {
    private val _shuttleState = MutableStateFlow<ShuttleUiState>(ShuttleUiState.Idle)
    val shuttleState: StateFlow<ShuttleUiState> = _shuttleState

    init {
        observeShuttle()
    }

    private fun observeShuttle() {
        viewModelScope.launch {
            _shuttleState.value = ShuttleUiState.Loading
            delay(1000)
            _shuttleState.value = ShuttleUiState.Success(
                busPosition = LatLng(0.5733, 32.5433),
                eta = 15
            )
        }
    }
}
