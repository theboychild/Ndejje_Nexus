/* 
 * This is the "Main Activity." 
 * Think of it as the "Front Door" or the "Engine Room" of the entire app. 
 */
package ug.ac.ndejje.nexus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ug.ac.ndejje.nexus.navigation.Screen
import ug.ac.ndejje.nexus.repository.AuthRepository
import ug.ac.ndejje.nexus.repository.NoticeRepository
import ug.ac.ndejje.nexus.repository.ShuttleRepository
import ug.ac.ndejje.nexus.model.User
import ug.ac.ndejje.nexus.ui.screens.*
import ug.ac.ndejje.nexus.ui.theme.NexusTheme
import ug.ac.ndejje.nexus.viewmodel.*

class MainActivity : ComponentActivity() {
    private val authRepository = AuthRepository()
    private val noticeRepository = NoticeRepository()
    private val shuttleRepository = ShuttleRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NexusTheme {
                NexusApp(authRepository, noticeRepository, shuttleRepository)
            }
        }
    }
}

/**
 * NexusApp is the "Director" or "Traffic Controller" of the app.
 */
@Composable
fun NexusApp(
    authRepository: AuthRepository,
    noticeRepository: NoticeRepository,
    shuttleRepository: ShuttleRepository
) {
    val navController = rememberNavController()
    val factory = ViewModelFactory(authRepository, noticeRepository, shuttleRepository)
    
    /* We initialize the ViewModels. */
    val authViewModel: AuthViewModel = viewModel(factory = factory)
    val dashboardViewModel: DashboardViewModel = viewModel(factory = factory)
    val shuttleViewModel: ShuttleViewModel = viewModel(factory = factory)
    val sosViewModel: SosViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()
    
    /* We "Watch" the current user from the repository. */
    val currentUser by authRepository.currentUser.collectAsState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = when (currentDestination?.route) {
        Screen.Splash.route -> false
        Screen.ForgotPassword.route -> false
        null -> false
        else -> true
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    val items = listOf(
                        Triple("Home", Screen.Dashboard.route, Icons.Default.Home),
                        Triple("Shuttle", Screen.ShuttleTracker.route, Icons.Default.DirectionsBus),
                        Triple("SOS", Screen.SOS.route, Icons.Default.Warning),
                        Triple("Notices", Screen.NoticeBoard.route, Icons.Default.Notifications),
                        Triple("Profile", Screen.Profile.route, Icons.Default.Person)
                    )
                    items.forEach { (label, route, icon) ->
                        NavigationBarItem(
                            icon = { Icon(icon, contentDescription = label) },
                            label = { Text(label) },
                            selected = currentDestination?.hierarchy?.any { it.route == route } == true,
                            onClick = {
                                navController.navigate(route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController, 
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            
            /* 1. SPLASH SCREEN. */
            composable(Screen.Splash.route) {
                SplashScreen(onNavigateToNext = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                })
            }

            /* 4. FORGOT PASSWORD SCREEN. */
            composable(Screen.ForgotPassword.route) {
                ForgotPasswordScreen(
                    viewModel = authViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            /* 5. DASHBOARD: Home base. */
            composable(Screen.Dashboard.route) {
                val user = currentUser ?: User(name = "Guest User")
                DashboardScreen(
                    user = user,
                    viewModel = dashboardViewModel,
                    onNavigateToSOS = { navController.navigate(Screen.SOS.route) },
                    onNavigateToTracker = { navController.navigate(Screen.ShuttleTracker.route) },
                    onNavigateToNotices = { navController.navigate(Screen.NoticeBoard.route) },
                    onNavigateToProfile = { navController.navigate(Screen.Profile.route) }
                )
            }

            /* 6. NOTICE BOARD. */
            composable(Screen.NoticeBoard.route) {
                NoticeBoardScreen(
                    viewModel = dashboardViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            /* 7. SHUTTLE HUB. */
            composable(Screen.ShuttleTracker.route) {
                ShuttleHubScreen(
                    viewModel = shuttleViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            /* 8. EMERGENCY HUB. */
            composable(Screen.SOS.route) {
                EmergencyHubScreen(
                    viewModel = sosViewModel,
                    onNavigateToConfirmation = {
                        navController.navigate(Screen.SosConfirmation.route) {
                            popUpTo(Screen.SOS.route) { inclusive = true }
                        }
                    },
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            /* 9. SOS CONFIRMATION. */
            composable(Screen.SosConfirmation.route) {
                SosConfirmationScreen(
                    viewModel = sosViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            /* 10. SHUTTLE SCHEDULE. */
            composable(Screen.ShuttleSchedule.route) {
                ShuttleScheduleScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            /* 11. ROUTE MAP. */
            composable(Screen.RouteMap.route) {
                RouteMapScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            /* 12. SEAT RESERVATION. */
            composable(Screen.SeatReservation.route) {
                SeatReservationScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            /* 13. SAFE WALK. */
            composable(Screen.SafeWalk.route) {
                SafeWalkScreen(
                    viewModel = sosViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            /* 14. PROFILE SCREEN. */
            composable(Screen.Profile.route) {
                val user = currentUser ?: User(name = "Guest User")
                ProfileScreen(
                    user = user,
                    viewModel = profileViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate(Screen.Splash.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

/**
 * ViewModelFactory helps create ViewModels that need dependencies.
 */
class ViewModelFactory(
    private val authRepository: AuthRepository,
    private val noticeRepository: NoticeRepository,
    private val shuttleRepository: ShuttleRepository
) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> 
                AuthViewModel(authRepository) as T
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> 
                DashboardViewModel(noticeRepository, shuttleRepository) as T
            modelClass.isAssignableFrom(ShuttleViewModel::class.java) -> 
                ShuttleViewModel(shuttleRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
