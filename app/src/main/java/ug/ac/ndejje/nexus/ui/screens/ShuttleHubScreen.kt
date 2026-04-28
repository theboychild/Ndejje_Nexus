package ug.ac.ndejje.nexus.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import ug.ac.ndejje.nexus.model.User
import ug.ac.ndejje.nexus.viewmodel.ShuttleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShuttleHubScreen(
    user: User,
    viewModel: ShuttleViewModel,
    onNavigateBack: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Tracker", "Schedule", "Route", "Reservation")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Shuttle Hub") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }
            
            when (selectedTab) {
                0 -> ShuttleTrackerContent(viewModel = viewModel)
                1 -> ShuttleScheduleContent(viewModel = viewModel)
                2 -> RouteMapContent()
                3 -> SeatReservationContent(user = user, viewModel = viewModel)
            }
        }
    }
}
