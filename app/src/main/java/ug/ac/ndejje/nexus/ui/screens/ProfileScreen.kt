/* 
 * This file contains the "Profile and Settings Screen."
 * Think of this as the student's "Personal Cabinet." It shows their university 
 * details and allows them to customize how they receive notifications.
 *
 * HOW MVVM IS USED HERE:
 * 1. VIEW: ProfileScreen.kt displays the student's info and toggle switches.
 * 2. MODEL: The "User" data class provides the student details to show.
 * 3. VIEW-MODEL: The "ProfileViewModel" manages the notification "Preferences."
 */
package ug.ac.ndejje.nexus.ui.screens

/* These are the "Tools" we use to build the visual parts of the screen. */
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ug.ac.ndejje.nexus.R
import ug.ac.ndejje.nexus.model.User
import ug.ac.ndejje.nexus.viewmodel.ProfileViewModel

/**
 * ProfileScreen is where students view their info and manage their app settings.
 * 
 * @param user The information about the logged-in student.
 * @param viewModel The "Brain" that manages the student's notification settings.
 * @param onNavigateBack Action to return to the previous screen.
 * @param onLogout Action to sign out of the app.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    user: User,
    viewModel: ProfileViewModel,
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit
) {
    /* VIEW-MODEL USAGE: We "Watch" the student's app preferences from the ProfileViewModel. */
    val preferences by viewModel.preferences.collectAsState()

    /* VIEW USAGE: Main layout with header and logout actions. */
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            /* MODEL USAGE: Using the "User" model to display name, reg number, etc. */
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = user.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(text = user.regNumber, style = MaterialTheme.typography.bodyMedium)
                    Text(text = user.program, style = MaterialTheme.typography.bodySmall)
                    Text(text = user.faculty, style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            /* PREFERENCES SECTION: Viewing and changing settings. */
            Text(text = "Preferences", style = MaterialTheme.typography.titleLarge)
            
            Spacer(modifier = Modifier.height(8.dp))

            /* VIEW-MODEL USAGE: Toggling settings in the ViewModel when the switches are clicked. */
            PreferenceItem(
                title = stringResource(R.string.pref_shuttle),
                checked = preferences.shuttleNotifications,
                onCheckedChange = { viewModel.toggleShuttleNotifications(it) }
            )
            
            PreferenceItem(
                title = stringResource(R.string.pref_notices),
                checked = preferences.noticeNotifications,
                onCheckedChange = { viewModel.toggleNoticeNotifications(it) }
            )
            
            PreferenceItem(
                title = stringResource(R.string.pref_sos),
                checked = preferences.sosNotifications,
                onCheckedChange = { viewModel.toggleSosNotifications(it) }
            )

            Spacer(modifier = Modifier.weight(1f))

            /* LOGOUT: Finalizing the session. */
            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Logout")
            }
        }
    }
}

/**
 * PreferenceItem is a custom "View" for a single setting switch.
 */
@Composable
fun PreferenceItem(title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.bodyLarge)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
