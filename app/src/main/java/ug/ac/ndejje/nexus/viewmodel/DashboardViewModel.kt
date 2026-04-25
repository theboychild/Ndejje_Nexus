/* 
 * This file contains the "Dashboard ViewModel."
 * Think of this as the "Brain" or the "Memory" of the Dashboard screen. 
 * Even if the phone is rotated or a call comes in, this "Brain" keeps all the 
 * important data (like bus times and news) safe so the screen doesn't forget them.
 */
package ug.ac.ndejje.nexus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ug.ac.ndejje.nexus.model.Notice
import ug.ac.ndejje.nexus.model.NoticeCategory

/**
 * DashboardUiState is like a "Status Report." 
 * It tells the app exactly what is happening right now so the screen knows what to draw.
 */
sealed class DashboardUiState {
    /* "Idle" means the app is waiting and hasn't started doing anything yet. */
    object Idle : DashboardUiState()
    
    /* "Loading" means the app is currently "thinking" or fetching data from the internet. */
    object Loading : DashboardUiState()
    
    /* "Success" means the data arrived! We package the shuttle time and notices here. */
    data class Success(
        val nextShuttleEta: String,
        val notices: List<Notice>,
    ) : DashboardUiState()
    
    /* "Error" means something went wrong (like a bad internet connection). */
    data class Error(val message: String) : DashboardUiState()
}

class DashboardViewModel : ViewModel() {
    /* "_dashboardState" is the app's "Internal Thoughts." 
     * Only this "Brain" can change what it's thinking.
     */
    private val _dashboardState = MutableStateFlow<DashboardUiState>(DashboardUiState.Idle)
    
    /* "dashboardState" is the app's "Public Voice." 
     * The screen "Listens" to this voice to know what to show to the student.
     */
    val dashboardState: StateFlow<DashboardUiState> = _dashboardState

    /* "init" is like an "Alarm Clock." 
     * As soon as this ViewModel is created, it immediately starts loading data. 
     */
    init {
        loadDashboardData()
    }

    /**
     * "loadDashboardData" is the mission to go get the latest info.
     */
    fun loadDashboardData() {
        /* "viewModelScope.launch" starts a "Background Task." 
         * This lets the app fetch data without "freezing" the screen.
         */
        viewModelScope.launch {
            /* First, we tell the screen to show a "Loading" spinner. */
            _dashboardState.value = DashboardUiState.Loading
            
            /* We wait for 1 second to simulate the time it takes to "talk" to the university server. */
            delay(1000) 
            
            /* Once the data "arrives," we tell the screen it was a "Success" and give it the info. */
            _dashboardState.value = DashboardUiState.Success(
                nextShuttleEta = "15", /* The bus is 15 minutes away. */
                notices = listOf(
                    /* Here are some sample notices to show on the board. */
                    Notice("1", "Exam Timetable Out", "Please check the notice board...", "2023-10-27", NoticeCategory.ACADEMIC),
                    Notice("2", "Inter-Campus Games", "Join us this Friday at Main Campus", "2023-10-26", NoticeCategory.SOCIAL)
                )
            )
        }
    }
}
