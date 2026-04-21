package ug.ac.ndejje.nexus.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShuttleTrackerScreen(onNavigateBack: () -> Unit) {
    val kampala = LatLng(0.3112, 32.5811)
    val luwero = LatLng(0.8354, 32.5055)
    
    // Mock bus position
    val busPosition = LatLng(0.5733, 32.5433)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(busPosition, 10f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Live Shuttle Tracker") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = MarkerState(position = busPosition),
                title = "Kampala-Luwero Shuttle",
                snippet = "ETA: 15 mins"
            )
            
            Marker(
                state = MarkerState(position = kampala),
                title = "Kampala Campus"
            )
            
            Marker(
                state = MarkerState(position = luwero),
                title = "Main Campus (Luwero)"
            )
        }
    }
}
