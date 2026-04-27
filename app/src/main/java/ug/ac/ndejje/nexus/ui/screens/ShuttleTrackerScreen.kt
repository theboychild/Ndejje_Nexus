package ug.ac.ndejje.nexus.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ug.ac.ndejje.nexus.ui.components.MockMapView
import ug.ac.ndejje.nexus.viewmodel.ShuttleUiState
import ug.ac.ndejje.nexus.viewmodel.ShuttleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShuttleTrackerScreen(
    viewModel: ShuttleViewModel,
    onNavigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Live Shuttle Tracker") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        ShuttleTrackerContent(viewModel = viewModel, modifier = Modifier.padding(padding))
    }
}

@Composable
fun ShuttleTrackerContent(
    viewModel: ShuttleViewModel,
    modifier: Modifier = Modifier
) {
    val shuttleState by viewModel.shuttleState.collectAsState()
    val state = shuttleState

    Box(modifier = modifier.fillMaxSize()) {
        when (state) {
            is ShuttleUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is ShuttleUiState.Success -> {
                MockMapView(
                    modifier = Modifier.fillMaxSize(),
                    busLocation = true
                )
            }
            is ShuttleUiState.Error -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { /* ViewModel handles initial load */ }) {
                        Text("Retry")
                    }
                }
            }
            ShuttleUiState.Idle -> {
                Text("Waiting for shuttle data...", modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}
