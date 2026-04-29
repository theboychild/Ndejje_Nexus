package ug.ac.ndejje.nexus.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import ug.ac.ndejje.nexus.model.User
import ug.ac.ndejje.nexus.viewmodel.SosUiState
import ug.ac.ndejje.nexus.viewmodel.SosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyHubScreen(
    user: User,
    viewModel: SosViewModel,
    onNavigateToConfirmation: () -> Unit,
    onNavigateBack: () -> Unit
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
                title = { Text("Emergency Hub") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
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
                0 -> SOSContent(user = user, viewModel = viewModel)
                1 -> SafeWalkContent(user = user, viewModel = viewModel)
            }
        }
    }
}
