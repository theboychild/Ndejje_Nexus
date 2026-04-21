package ug.ac.ndejje.nexus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ug.ac.ndejje.nexus.model.User
import ug.ac.ndejje.nexus.repository.AuthRepository

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("Email and password are required")
            return
        }

        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            val result = repository.login(email, password)
            if (result.isSuccess) {
                _loginState.value = LoginState.Success(result.getOrThrow())
            } else {
                _loginState.value = LoginState.Error(result.exceptionOrNull()?.message ?: "Login failed")
            }
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: User) : LoginState()
    data class Error(val message: String) : LoginState()
}