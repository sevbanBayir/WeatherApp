package com.sevban.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun PermissionAlertDialog(
    onConfirmed: () -> Unit,
    onDismissed: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissed,
        confirmButton = {
            Button(onClick = onConfirmed) {
                Text(text = "Go To Settings")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissed) {
                Text(text = "I won't let you know")
            }
        },
        title = {
            Text(text = "We need your permission")
        },
        text = {
            Text(text = "To provide related information about weather we have to know your location")
        }
    )
}