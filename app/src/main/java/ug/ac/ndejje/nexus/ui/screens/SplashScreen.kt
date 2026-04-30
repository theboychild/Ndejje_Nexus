/* 
 * This file contains the "Splash Screen."
 * This is the very first screen a user sees. It shows the Ndejje University 
 * logo while the app prepares everything in the background.
 *
 * HOW MVVM IS USED HERE:
 * 1. VIEW: SplashScreen.kt handles the visual logo and the 2-second delay.
 * 2. MODEL: Conceptually relates to the initial application state.
 * 3. VIEW-MODEL: Not directly used here, as this is a simple timed transition.
 */
package ug.ac.ndejje.nexus.ui.screens

/* These are the "Tools" we use to build the visual parts of the screen. */
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import ug.ac.ndejje.nexus.R

/**
 * SplashScreen is the initial "Welcome" page that appears for a few seconds.
 * 
 * @param onNavigateToNext The action to take once the loading time is finished.
 */
@Composable
fun SplashScreen(onNavigateToNext: () -> Unit) {
    /* VIEW LOGIC: A 2-second timer handled by the View itself. */
    LaunchedEffect(Unit) {
        delay(2000)
        onNavigateToNext()
    }


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            /* UNIVERSITY LOGO: Branded image asset. */
            Image(
                painter = painterResource(id = R.drawable.ndejje_badge),
                contentDescription = "Ndejje University Logo",
                modifier = Modifier.size(150.dp)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            /* APP NAME: Text branding. */
            Text(
                text = "NDEJJE NEXUS",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp
            )
        }
    }
}
