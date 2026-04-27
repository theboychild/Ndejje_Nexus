package ug.ac.ndejje.nexus.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteMapScreen(onNavigateBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Route Map") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        RouteMapContent(modifier = Modifier.padding(padding))
    }
}

@Composable
fun RouteMapContent(modifier: Modifier = Modifier) {
    val mainCampus = LatLng(0.8354, 32.5055) 
    val kampalaCampus = LatLng(0.3112, 32.5811)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(mainCampus, 10f)
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = false),
        uiSettings = MapUiSettings(zoomControlsEnabled = true)
    ) {
        Marker(
            state = rememberMarkerState(position = mainCampus),
            title = "Main Campus (Luwero)",
            snippet = "Main Shuttle Hub"
        )
        Marker(
            state = rememberMarkerState(position = kampalaCampus),
            title = "Kampala Campus",
            snippet = "City Hub"
        )
    }
}
