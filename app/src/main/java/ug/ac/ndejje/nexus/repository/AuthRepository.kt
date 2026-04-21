package ug.ac.ndejje.nexus.repository

import kotlinx.coroutines.delay
import ug.ac.ndejje.nexus.model.User

class AuthRepository {
    private var currentUser: User? = null
    private val users = mutableListOf<User>()

    suspend fun login(email: String, password: String): Result<User> {
        delay(1000) // Simulate network delay
        val user = users.find { it.email == email }
        return if (user != null) {
            currentUser = user
            Result.success(user)
        } else {
            Result.failure(Exception("Invalid credentials"))
        }
    }

    suspend fun register(user: User): Result<Unit> {
        delay(1000) // Simulate network delay
        users.add(user)
        currentUser = user // Automatically log in the user upon registration
        return Result.success(Unit)
    }

    fun getCurrentUser(): User? = currentUser
}