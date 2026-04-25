/* 
 * This is the "Dashboard Screen." 
 * Think of this as the "Home Base" or the "Main Lobby" of the app. 
 * From here, students can quickly get to all the important features like 
 * emergency alerts, the shuttle tracker, and the latest news.
 *
 * HOW MVVM IS USED HERE:
 * 1. VIEW: This file (DashboardScreen.kt) is the "View." It only cares about 
 *    how things look (buttons, colors, layout).
 * 2. MODEL: The "User" and "Notice" data classes are the "Models." They are 
 *    the simple containers for the data we show.
 * 3. VIEW-MODEL: The "DashboardViewModel" is the "Brain." The View asks the 
 *    ViewModel for data, and the ViewModel provides it via "dashboardState."
 */
package ug.ac.ndejje.nexus.ui.screens

/* These are the "Tools" we use to build the visual parts of the screen. */
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ug.ac.ndejje.nexus.R
import ug.ac.ndejje.nexus.model.Notice
import ug.ac.ndejje.nexus.model.User
import ug.ac.ndejje.nexus.viewmodel.DashboardUiState
import ug.ac.ndejje.nexus.viewmodel.DashboardViewModel

/**
 * DashboardScreen is the first thing a logged-in user sees.
 * 
 * @param user Information about the student (like their name).
 * @param viewModel The "Brain" that fetches data like bus times and news.
 * @param onNavigateToSOS Action to navigate to the SOS screen.
 * @param onNavigateToTracker Action to navigate to the shuttle tracker screen.
 * @param onNavigateToNotices Action to navigate to the notice board screen.
 * @param onNavigateToProfile Action to navigate to the profile screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    user: User,
    viewModel: DashboardViewModel,
    onNavigateToSOS: () -> Unit,
    onNavigateToTracker: () -> Unit,
    onNavigateToNotices: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    /* VIEW-MODEL USAGE: We "Watch" the state of the dashboard here. If new information 
     * arrives (like an updated bus time), this screen will automatically refresh.
     */
    val dashboardState by viewModel.dashboardState.collectAsState()
    val state = dashboardState

    /* VIEW USAGE: The "Scaffold" is the basic skeleton of the page. */
    Scaffold(
        topBar = {
            /* This is the Header with the app's name centered at the top. */
            CenterAlignedTopAppBar(
                title = { Text("Ndejje Nexus") },
                actions = {
                    /* Clicking the Person icon takes you to your Profile/Settings. */
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                }
            )
        }
    ) { padding ->
        /* VIEW USAGE: We stack everything from top to bottom. */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /* MODEL USAGE: A warm greeting for the student using their name from the "User" model. */
            Text(
                text = stringResource(R.string.welcome_user, user.name),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.align(Alignment.Start)
            )

            /* A bit of empty space for a clean look. */
            Spacer(modifier = Modifier.height(24.dp))

            /* THE SOS BUTTON: A big red circle that acts as an emergency alert. */
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.error)
                    /* When clicked, it "tells" the app to go to the SOS screen. */
                    .clickable { onNavigateToSOS() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.sos_button),
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            /* VIEW-MODEL USAGE: Here, we decide what to show based on the current state from the ViewModel. */
            when (state) {
                /* 1. Still loading? Show a spinning wheel. */
                is DashboardUiState.Loading -> CircularProgressIndicator()
                
                /* 2. Success? Show the shuttle info and announcements. */
                is DashboardUiState.Success -> {
                    /* SHUTTLE ETA CARD: Tells the user when the next bus arrives. */
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            /* Clicking this card opens the live bus tracker map. */
                            .clickable { onNavigateToTracker() },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = androidx.compose.ui.res.painterResource(id = android.R.drawable.ic_dialog_map),
                                contentDescription = null,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = stringResource(R.string.shuttle_tracker),
                                    style = MaterialTheme.typography.titleMedium
                                )
                                /* Shows the minutes remaining from the "Success" state data. */
                                Text(
                                    text = stringResource(R.string.shuttle_eta, state.nextShuttleEta),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    /* ANNOUNCEMENTS SECTION: A quick look at the latest news. */
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.announcements),
                            style = MaterialTheme.typography.titleLarge
                        )
                        /* Clicking "View All" takes you to the full Notice Board. */
                        TextButton(onClick = onNavigateToNotices) {
                            Text("View All")
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    /* MODEL USAGE: A Horizontal List of "Notice" models. */
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(state.notices) { notice ->
                            AnnouncementCard(notice)
                        }
                    }
                }
                
                /* 3. Error? Show the problem and a retry button. */
                is DashboardUiState.Error -> {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error)
                    Button(onClick = { viewModel.loadDashboardData() }) {
                        Text("Retry")
                    }
                }
                DashboardUiState.Idle -> {}
            }
        }
    }
}

/**
 * AnnouncementCard is a small preview box for a single "Notice" Model.
 */
@Composable
fun AnnouncementCard(notice: Notice) {
    /* VIEW USAGE: Defines how a single announcement looks. */
    Card(
        modifier = Modifier.width(250.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            /* MODEL USAGE: Pulls title, content, and date from the Notice object. */
            Text(text = notice.title, fontWeight = FontWeight.Bold, maxLines = 1)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = notice.content, style = MaterialTheme.typography.bodySmall, maxLines = 2)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = notice.date, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
    }
}
