package com.rahul.wallcraft.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rahul.wallcraft.presentation.aigenerate.AiGenerateScreen
import com.rahul.wallcraft.presentation.browse.BrowseScreen
import com.rahul.wallcraft.presentation.detail.WallpaperDetailScreen
import com.rahul.wallcraft.presentation.favorites.FavoritesScreen
import com.rahul.wallcraft.presentation.home.HomeScreen
import com.rahul.wallcraft.presentation.onboarding.OnboardingScreen
import com.rahul.wallcraft.presentation.search.SearchScreen
import com.rahul.wallcraft.presentation.settings.SettingsScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = Screen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onComplete = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onWallpaperClick = { wallpaper ->
                    navController.navigate(Screen.Detail.createRoute(wallpaper.id))
                },
                onCategoryClick = { category ->
                    navController.navigate(Screen.Browse.createRoute(category))
                },
                onSearchClick = {
                    navController.navigate(Screen.Search.route)
                }
            )
        }

        composable(
            route = Screen.Browse.route,
            arguments = listOf(
                navArgument("category") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) {
            BrowseScreen(
                onWallpaperClick = { wallpaper ->
                    navController.navigate(Screen.Detail.createRoute(wallpaper.id))
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                onWallpaperClick = { wallpaper ->
                    navController.navigate(Screen.Detail.createRoute(wallpaper.id))
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("wallpaperId") { type = NavType.StringType }
            )
        ) {
            WallpaperDetailScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.AiGenerate.route) {
            AiGenerateScreen(
                onWallpaperClick = { wallpaper ->
                    navController.navigate(Screen.Detail.createRoute(wallpaper.id))
                }
            )
        }

        composable(Screen.Favorites.route) {
            FavoritesScreen(
                onWallpaperClick = { wallpaper ->
                    navController.navigate(Screen.Detail.createRoute(wallpaper.id))
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
