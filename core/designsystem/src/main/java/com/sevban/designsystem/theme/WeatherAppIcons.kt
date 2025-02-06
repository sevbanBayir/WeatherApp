package com.sevban.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.sevban.designsystem.R

@Stable
object WeatherAppIcons {
    val Humidity: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_humidity)

    val Wind: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_wind)

    val Location: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_location)

    val Visibility: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_visibility)

    val City: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_city)
}