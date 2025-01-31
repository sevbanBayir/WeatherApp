package com.sevban.location

sealed interface LocationScreenEvet {
    data object OnLocationPermissionDeclined : LocationScreenEvet
    data object OnLocationPermissionPermanentlyDeclined : LocationScreenEvet
    data object OnPermissionDialogDismissed : LocationScreenEvet
}