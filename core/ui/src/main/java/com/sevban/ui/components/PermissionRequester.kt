package com.sevban.ui.components

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.sevban.common.extensions.hasLocationPermission
import com.sevban.common.extensions.shouldShowPermissionRationale

@Composable
fun PermissionRequester(
    onPermissionGranted: () -> Unit,
    onPermissionFirstDeclined: () -> Unit,
    onPermissionPermanentlyDeclined: () -> Unit,
) {
    val context = LocalContext.current
    val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )
    val activityResultLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { permissionResults ->
                permissions.forEach { permission ->
                    if (permissionResults[permission] == true) {
                        onPermissionGranted()
                    } else if (context.shouldShowPermissionRationale(permission)
                            .not() && context.hasLocationPermission().not()
                    ) {
                        onPermissionPermanentlyDeclined()
                    } else {
                        onPermissionFirstDeclined()
                    }
                }
            }
        )

    LaunchedEffect(key1 = true) {
        activityResultLauncher.launch(permissions)
    }
    /*    Button(onClick = {
            activityResultLauncher.launch(permissions)
        }) {
            Text(text = "Grant location permissions")
        }*/

}