package com.manriquetavi.jetregisterapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.manriquetavi.jetregisterapp.features.home.HomeScreen
import com.manriquetavi.jetregisterapp.features.home.HomeViewModel
import com.manriquetavi.jetregisterapp.features.take_picture.TakePictureScreen

@Composable
fun SetUpNavGraph(
    navController: NavHostController,
    startDestination: Screen = Screen.Home
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Screen.Home> {
            val homeViewModel: HomeViewModel = hiltViewModel()
            val homeUiState = homeViewModel.homeUiState.collectAsStateWithLifecycle()
            HomeScreen(
                homeUiState = homeUiState.value,
                navigateToTakePicture = {
                    navController.navigate(Screen.TakePicture)
                },
                onSaveInformation = { homeViewModel.saveInformation(it) }
            )
        }
        composable<Screen.TakePicture> {
            TakePictureScreen(
                navigateToHome = {
                    if (navController.canGoBack) {
                        navController.popBackStack()
                    }
                }
            )
        }
    }
}

val NavHostController.canGoBack: Boolean get() = this.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED
