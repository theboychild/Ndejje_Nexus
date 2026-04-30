package ug.ac.ndejje.nexus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MockMapView(
    modifier: Modifier = Modifier,
    busLocation: Boolean = false
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFE0E0E0)) // Light gray background to simulate map
    ) {
        // Grid lines to look like a map
        Column(Modifier.fillMaxSize()) {
            repeat(10) {
                Spacer(modifier = Modifier.weight(1f))
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.5f)))
            }
        }
        Row(Modifier.fillMaxSize()) {
            repeat(10) {
                Spacer(modifier = Modifier.weight(1f))
                Box(modifier = Modifier.fillMaxHeight().width(1.dp).background(Color.White.copy(alpha = 0.5f)))
            }
        }

        // Main Campus Marker
        MapMarker(
            modifier = Modifier.align(Alignment.Center),
            label = "Main Campus",
            color = Color.Blue
        )

        // Kampala Campus Marker
        MapMarker(
            modifier = Modifier.align(Alignment.BottomStart).padding(start = 50.dp, bottom = 100.dp),
            label = "Kampala Campus",
            color = Color.Blue
        )

        if (busLocation) {
            // Simulated Bus Location
            MapMarker(
                modifier = Modifier.align(Alignment.TopEnd).padding(top = 80.dp, end = 60.dp),
                label = "Shuttle",
                color = Color.Red,
                isBus = true
            )
        }

        // Overlay text
        Text(
            text = "MOCK MAP MODE",
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 16.dp),
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun MapMarker(
    modifier: Modifier = Modifier,
    label: String,
    color: Color,
    isBus: Boolean = false
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(color, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isBus) Icons.Default.DirectionsBus else Icons.Default.LocationOn,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier.background(Color.White.copy(alpha = 0.7f)).padding(horizontal = 4.dp)
        )
    }
}
