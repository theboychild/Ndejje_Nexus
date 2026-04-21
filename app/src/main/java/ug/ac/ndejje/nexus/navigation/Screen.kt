package ug.ac.ndejje.nexus.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Dashboard : Screen("dashboard")
    object ShuttleTracker : Screen("shuttle_tracker")
    object SOS : Screen("sos")
    object NoticeBoard : Screen("notice_board")
    object Profile : Screen("profile")
}