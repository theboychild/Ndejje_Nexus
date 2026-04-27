package ug.ac.ndejje.nexus.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ug.ac.ndejje.nexus.model.SosAlert
import ug.ac.ndejje.nexus.model.SosStatus

class SosRepository {
    private val _alerts = MutableStateFlow<List<SosAlert>>(emptyList())
    val alerts: StateFlow<List<SosAlert>> = _alerts.asStateFlow()

    fun addAlert(alert: SosAlert) {
        val currentList = _alerts.value.toMutableList()
        currentList.add(0, alert)
        _alerts.value = currentList
    }

    fun updateAlertStatus(alertId: String, newStatus: SosStatus) {
        val currentList = _alerts.value.map {
            if (it.id == alertId) it.copy(status = newStatus) else it
        }
        _alerts.value = currentList
    }
    
    fun resolveActiveAlertForStudent(studentRegNumber: String) {
        val currentList = _alerts.value.map {
            if (it.studentRegNumber == studentRegNumber && it.status == SosStatus.ACTIVE) {
                it.copy(status = SosStatus.RESOLVED)
            } else it
        }
        _alerts.value = currentList
    }
}
