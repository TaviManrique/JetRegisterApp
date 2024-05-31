package com.manriquetavi.jetregisterapp.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Home: Screen()
    @Serializable
    data object TakePicture: Screen()
}