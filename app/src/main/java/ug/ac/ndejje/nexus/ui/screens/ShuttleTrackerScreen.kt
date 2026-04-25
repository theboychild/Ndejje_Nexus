package ug.ac.ndejje.nexus.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
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

    val kampala = LatLng(0.3112, 32.5811)
    val luwero = LatLng(0.8354, 32.5055)
    
    Box(modifier = modifier.fillMaxSize()) {
        when (state) {
            is ShuttleUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is ShuttleUiState.Success -> {
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(state.busPosition, 10f)
                }

                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {
                    Marker(
                        state = rememberMarkerState(position = state.busPosition),
                        title = "Kampala-Luwero Shuttle",
                        snippet = "ETA: ${state.eta} mins"
                    )
                    Marker(state = rememberMarkerState(position = kampala), title = "Kampala Campus")
                    Marker(state = rememberMarkerState(position = luwero), title = "Main Campus (Luwero)")
                }
            }
            is ShuttleUiState.Error -> {
                Text(text = state.message, modifier = Modifier.align(Alignment.Center))
            }
            ShuttleUiState.Idle -> {}
        }
    }
}
