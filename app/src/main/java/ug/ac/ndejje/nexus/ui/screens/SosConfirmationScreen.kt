/* 
 * This file contains the "SOS Confirmation Screen."
 * Once an emergency alert is sent, this screen gives the student peace of mind 
 * by confirming that help is on the way and providing details about the response.
 *
 * HOW MVVM IS USED HERE:
 * 1. VIEW: SosConfirmationScreen.kt defines the success layout and feedback text.
 * 2. MODEL: Conceptually relates to the safety "confirmation" data.
 * 3. VIEW-MODEL: Uses "SosViewModel" to reset the emergency state when the user is safe.
 */
package ug.ac.ndejje.nexus.ui.screens

/* These are the "Tools" we use for the confirmation layout. */
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ug.ac.ndejje.nexus.viewmodel.SosViewModel

/**
 * SosConfirmationScreen provides feedback that security has been notified.
 * 
 * @param viewModel The "Brain" used to reset the emergency state when safe.
 * @param onNavigateBack Action to return to the Dashboard.
 */
@Composable
fun SosConfirmationScreen(
    viewModel: SosViewModel,
    onNavigateBack: () -> Unit
) {
    /* VIEW USAGE: Centralized layout for status confirmation. */
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        /* SUCCESS ICON: Visual confirmation of success. */
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = Color(0xFF4CAF50)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        /* HEADER: Verbal confirmation. */
        Text(
            text = "Security Response Confirmed",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        /* DETAILS: Static information about the dispatcher response. */
        Text(
            text = "Officer Mutebi has been dispatched and is 2 minutes away.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        /* VIEW-MODEL USAGE: Clicking this calls "resetState" in the ViewModel. */
        Button(
            onClick = { 
                viewModel.resetState()
                onNavigateBack() 
            },
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("I am Safe Now")
        }
    }
}
