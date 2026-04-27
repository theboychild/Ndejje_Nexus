package ug.ac.ndejje.nexus.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ug.ac.ndejje.nexus.ui.components.MockMapView

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
    MockMapView(
        modifier = modifier.fillMaxSize(),
        busLocation = false
    )
}
