package com.sevban.home

sealed interface HomeScreenEvent {
    data object OnLocationPermissionGranted: HomeScreenEvent
    data object OnLocationPermissionPermanentlyDeclined: HomeScreenEvent
}