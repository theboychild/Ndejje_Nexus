/* 
 * This file contains the "Authentication ViewModel."
 * This "Brain" handles the logic for logging in, registering new accounts, 
 * and resetting passwords. It talks to the AuthRepository to save and check 
 * student records.
 */
package ug.ac.ndejje.nexus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ug.ac.ndejje.nexus.model.User
import ug.ac.ndejje.nexus.repository.AuthRepository

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val user: User? = null) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _authState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val authState: StateFlow<AuthUiState> = _authState

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthUiState.Error("Email and password are required")
            return
        }
        viewModelScope.launch {
            _authState.value = AuthUiState.Loading
            val result = repository.login(email, password)
            if (result.isSuccess) {
                _authState.value = AuthUiState.Success(result.getOrThrow())
            } else {
                _authState.value = AuthUiState.Error(result.exceptionOrNull()?.message ?: "Login failed")
            }
        }
    }

    fun register(user: User) {
        if (user.email.isBlank() || user.password.isBlank() || user.name.isBlank() || user.regNumber.isBlank()) {
            _authState.value = AuthUiState.Error("All fields are required")
            return
        }
        viewModelScope.launch {
            _authState.value = AuthUiState.Loading
            val result = repository.register(user)
            if (result.isSuccess) {
                _authState.value = AuthUiState.Success()
            } else {
                _authState.value = AuthUiState.Error(result.exceptionOrNull()?.message ?: "Registration failed")
            }
        }
    }

    fun sendResetEmail(email: String) {
        if (email.isBlank()) {
            _authState.value = AuthUiState.Error("Email is required")
            return
        }
        viewModelScope.launch {
            _authState.value = AuthUiState.Loading
            val result = repository.sendPasswordResetEmail(email)
            if (result.isSuccess) {
                _authState.value = AuthUiState.Success()
            } else {
                _authState.value = AuthUiState.Error(result.exceptionOrNull()?.message ?: "Failed to send reset email")
            }
        }
    }

    fun logout() {
        repository.logout()
        _authState.value = AuthUiState.Idle
    }

    fun resetState() {
        _authState.value = AuthUiState.Idle
    }
}
