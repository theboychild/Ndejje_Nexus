package ug.ac.ndejje.nexus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ug.ac.ndejje.nexus.model.Notice
import ug.ac.ndejje.nexus.repository.NoticeRepository
import ug.ac.ndejje.nexus.repository.ShuttleRepository

sealed class DashboardUiState {
    object Idle : DashboardUiState()
    object Loading : DashboardUiState()
    data class Success(
        val nextShuttleEta: String,
        val notices: List<Notice>,
    ) : DashboardUiState()
    data class Error(val message: String) : DashboardUiState()
}

class DashboardViewModel(
    private val noticeRepository: NoticeRepository,
    private val shuttleRepository: ShuttleRepository
) : ViewModel() {
    private val _dashboardState = MutableStateFlow<DashboardUiState>(DashboardUiState.Idle)
    val dashboardState: StateFlow<DashboardUiState> = _dashboardState

    init {
        loadDashboardData()
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            _dashboardState.value = DashboardUiState.Loading
            
            val noticeResult = noticeRepository.getNotices()
            val shuttleResult = shuttleRepository.getLiveShuttleInfo()
            
            if (noticeResult.isSuccess && shuttleResult.isSuccess) {
                _dashboardState.value = DashboardUiState.Success(
                    nextShuttleEta = shuttleResult.getOrNull()?.eta?.toString() ?: "N/A",
                    notices = noticeResult.getOrThrow()
                )
            } else {
                _dashboardState.value = DashboardUiState.Error("Failed to load dashboard data.")
            }
        }
    }
}
