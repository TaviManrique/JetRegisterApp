package com.manriquetavi.jetregisterapp

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.manriquetavi.jetregisterapp.Constant.Permissions.CAMERAX_PERMISSIONS

fun hasRequiredPermissions(context: Context): Boolean {
    return CAMERAX_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            context,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }
}