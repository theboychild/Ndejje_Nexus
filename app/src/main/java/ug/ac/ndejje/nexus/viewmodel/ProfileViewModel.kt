/* 
 * This file contains the "Profile ViewModel."
 * This "Brain" remembers the student's preferences, such as which notifications 
 * they want to receive (Shuttle updates, Notice Board news, or Emergency alerts).
 */
package ug.ac.ndejje.nexus.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ProfilePreferences(
    val shuttleNotifications: Boolean = true,
    val noticeNotifications: Boolean = true,
    val sosNotifications: Boolean = true
)

class ProfileViewModel : ViewModel() {
    private val _preferences = MutableStateFlow(ProfilePreferences())
    val preferences: StateFlow<ProfilePreferences> = _preferences

    fun toggleShuttleNotifications(enabled: Boolean) {
        _preferences.value = _preferences.value.copy(shuttleNotifications = enabled)
    }

    fun toggleNoticeNotifications(enabled: Boolean) {
        _preferences.value = _preferences.value.copy(noticeNotifications = enabled)
    }

    fun toggleSosNotifications(enabled: Boolean) {
        _preferences.value = _preferences.value.copy(sosNotifications = enabled)
    }
}
