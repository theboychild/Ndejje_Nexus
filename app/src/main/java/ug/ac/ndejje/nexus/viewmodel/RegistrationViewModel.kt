package ug.ac.ndejje.nexus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ug.ac.ndejje.nexus.model.User
import ug.ac.ndejje.nexus.repository.AuthRepository

class RegistrationViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _registrationState = MutableStateFlow<RegistrationState>(RegistrationState.Idle)
    val registrationState: StateFlow<RegistrationState> = _registrationState

    fun register(name: String, regNumber: String, faculty: String, level: String, program: String, email: String, password: String) {
        if (name.isBlank() || regNumber.isBlank() || faculty.isBlank() || level.isBlank() || program.isBlank() || email.isBlank() || password.isBlank()) {
            _registrationState.value = RegistrationState.Error("All fields are required")
            return
        }

        viewModelScope.launch {
            _registrationState.value = RegistrationState.Loading
            val result = repository.register(User(name, regNumber, email, faculty, level, program, password))
            if (result.isSuccess) {
                _registrationState.value = RegistrationState.Success
            } else {
                _registrationState.value = RegistrationState.Error(result.exceptionOrNull()?.message ?: "Registration failed")
            }
        }
    }

    fun resetState() {
        _registrationState.value = RegistrationState.Idle
    }
}

sealed class RegistrationState {
    object Idle : RegistrationState()
    object Loading : RegistrationState()
    object Success : RegistrationState()
    data class Error(val message: String) : RegistrationState()
}