package com.rahul.clearwalls.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rahul.clearwalls.presentation.navigation.NavGraph
import com.rahul.clearwalls.presentation.navigation.Screen
import com.rahul.clearwalls.presentation.navigation.bottomNavItems
import com.rahul.clearwalls.presentation.settings.SettingsViewModel
import com.rahul.clearwalls.presentation.theme.ClearWallsTheme
import com.rahul.clearwalls.presentation.theme.ThemeMode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var dataStore: DataStore<Preferences>

    // BUG-011 FIX: Notification permission launcher for Android 13+
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ ->
        // Permission result handled; WorkManager will function correctly if granted.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // BUG-014 FIX: installSplashScreen() MUST be called before super.onCreate().
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // BUG-011 FIX: Request POST_NOTIFICATIONS on Android 13+.
        requestNotificationPermissionIfNeeded()

        setContent {
            val themeMode by dataStore.data
                .map { prefs ->
                    prefs[SettingsViewModel.KEY_THEME_MODE]?.let {
                        try { ThemeMode.valueOf(it) } catch (_: Exception) { ThemeMode.SYSTEM }
                    } ?: ThemeMode.SYSTEM
                }
                .collectAsState(initial = ThemeMode.SYSTEM)

            ClearWallsTheme(themeMode = themeMode) {
                ClearWallsMainApp()
            }
        }
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

@Composable
fun ClearWallsMainApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        Screen.Home.route,
        Screen.Browse.route,
        Screen.AiGenerate.route,
        Screen.Favorites.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = currentRoute == item.screen.route
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.label
                                )
                            },
                            label = {
                                Text(item.label, style = MaterialTheme.typography.labelSmall)
                            },
                            selected = selected,
                            onClick = {
                                navController.navigate(item.screen.navigationRoute) {
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
    ) { padding ->
        NavGraph(
            navController = navController,
            modifier = Modifier.padding(padding)
        )
    }
}
