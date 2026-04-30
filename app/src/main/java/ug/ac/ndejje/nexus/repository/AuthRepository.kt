package ug.ac.ndejje.nexus.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ug.ac.ndejje.nexus.model.User

/**
 * Singleton repository for Authentication.
 * Using 'object' ensures it survives Activity recreations and maintains the "simulated DB".
 */
object AuthRepository {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    private val users = mutableListOf<User>()

    suspend fun login(email: String, password: String): Result<User> {
        delay(1000)
        val cleanEmail = email.trim().lowercase()
        val user = users.find { (it.email.lowercase() == cleanEmail) && (it.password == password) }
        
        return if (user != null) {
            _currentUser.value = user
            Result.success(user)
        } else {
            Result.failure(Exception("Invalid email or password. Please register first."))
        }
    }

    suspend fun register(user: User): Result<Unit> {
        delay(1000)
        val cleanEmail = user.email.trim().lowercase()
        if (users.any { it.email.lowercase() == cleanEmail }) {
            return Result.failure(Exception("An account with this email already exists."))
        }
        val cleanUser = user.copy(email = cleanEmail)
        users.add(cleanUser)
        return Result.success(Unit)
    }

    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        delay(1000)
        val cleanEmail = email.trim().lowercase()
        val userExists = users.any { it.email.lowercase() == cleanEmail }
        return if (userExists) Result.success(Unit) else Result.failure(Exception("Email not found."))
    }

    fun getCurrentUser(): User? = _currentUser.value

    fun logout() {
        _currentUser.value = null
    }
}
