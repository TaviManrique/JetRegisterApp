package com.manriquetavi.jetregisterapp

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

object Constant {
    object Permissions {
        val CAMERAX_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
        )
    }
    val options = listOf("Problema 1", "Problema 2", "Problema 3", "Problema 4")
}