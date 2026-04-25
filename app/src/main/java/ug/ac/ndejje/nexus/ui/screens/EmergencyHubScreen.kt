package ug.ac.ndejje.nexus.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import ug.ac.ndejje.nexus.viewmodel.SosUiState
import ug.ac.ndejje.nexus.viewmodel.SosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyHubScreen(
    viewModel: SosViewModel,
    onNavigateToConfirmation: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("SOS", "Safe Walk")
    
    val sosState by viewModel.sosState.collectAsState()
    
    LaunchedEffect(sosState) {
        if (sosState is SosUiState.Sent) {
            onNavigateToConfirmation()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Emergency Hub") }
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
                0 -> SOSContent(viewModel = viewModel)
                1 -> SafeWalkContent(viewModel = viewModel)
            }
        }
    }
}
