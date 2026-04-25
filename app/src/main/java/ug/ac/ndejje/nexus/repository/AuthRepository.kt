/* 
 * This file contains the "Auth Repository."
 * Think of this as the app's "Secretary" or "Record Keeper."
 * Its job is to handle the "Paperwork" for logging in, signing up, and keeping 
 * track of which student is currently using the phone.
 */
package ug.ac.ndejje.nexus.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ug.ac.ndejje.nexus.model.User

class AuthRepository {
    /* "currentUser" is now a StateFlow so the app can "Listen" for changes 
     * to who is logged in. 
     */
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    /* "users" is our in-memory list of registered students. */
    private val users = mutableListOf<User>()

    /**
     * login checks if the email and password match someone in our records.
     */
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

    /**
     * register adds a new student to our records.
     */
    suspend fun register(user: User): Result<Unit> {
        delay(1000) 
        val cleanUser = user.copy(email = user.email.trim().lowercase())
        users.add(cleanUser)
        _currentUser.value = cleanUser 
        return Result.success(Unit)
    }

    /**
     * sendPasswordResetEmail handles the logic for resetting a password.
     */
    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        delay(1000)
        val cleanEmail = email.trim().lowercase()
        val userExists = users.any { it.email.lowercase() == cleanEmail }
        
        return if (userExists) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Email not found in our records."))
        }
    }

    /**
     * Returns the info of the student who is currently logged in.
     */
    fun getCurrentUser(): User? = _currentUser.value

    /**
     * logout signs the student out.
     */
    fun logout() {
        _currentUser.value = null
    }
}
