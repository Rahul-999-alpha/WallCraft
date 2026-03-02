package com.rahul.clearwalls.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rahul.clearwalls.presentation.admin.AdminScreen
import com.rahul.clearwalls.presentation.aigenerate.AiGenerateScreen
import com.rahul.clearwalls.presentation.browse.BrowseScreen
import com.rahul.clearwalls.presentation.detail.WallpaperDetailScreen
import com.rahul.clearwalls.presentation.favorites.FavoritesScreen
import com.rahul.clearwalls.presentation.home.HomeScreen
import com.rahul.clearwalls.presentation.onboarding.OnboardingScreen
import com.rahul.clearwalls.presentation.search.SearchScreen
import com.rahul.clearwalls.presentation.settings.SettingsScreen

private const val TRANSITION_DURATION = 300

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = Screen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = {
            fadeIn(animationSpec = tween(TRANSITION_DURATION)) +
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(TRANSITION_DURATION))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(TRANSITION_DURATION)) +
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(TRANSITION_DURATION))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(TRANSITION_DURATION)) +
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(TRANSITION_DURATION))
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(TRANSITION_DURATION)) +
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(TRANSITION_DURATION))
        }
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
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
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
                onBackClick = { navController.popBackStack() },
                onAdminNavigate = {
                    navController.navigate(Screen.Admin.route)
                }
            )
        }

        composable(Screen.Admin.route) {
            AdminScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
