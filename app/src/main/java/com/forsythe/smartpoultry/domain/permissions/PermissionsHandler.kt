package com.forsythe.smartpoultry.domain.permissions

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun checkIfPermissionGranted(context: Context, permission : String) = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

