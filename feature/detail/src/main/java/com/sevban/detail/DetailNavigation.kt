package com.sevban.detail

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import java.net.URLDecoder
import java.net.URLEncoder

private val URL_CHARACTER_ENCODING = Charsets.UTF_8.name()

@VisibleForTesting
internal const val ITEM_ID_ARG = "itemId"

internal class DetailArgs(val itemId: String) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(URLDecoder.decode(checkNotNull(savedStateHandle[ITEM_ID_ARG]), URL_CHARACTER_ENCODING))
}

fun NavController.navigateToDetail(itemId: String) {
    val encodedId = URLEncoder.encode(itemId, URL_CHARACTER_ENCODING)
    navigate("detail/$encodedId") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.detailScreen(
    onBackClick: () -> Unit,
) {
    composable(
        route = "detail/{$ITEM_ID_ARG}",
        arguments = listOf(
            navArgument(ITEM_ID_ARG) { type = NavType.StringType },
        ),
    ) {
        DetailScreenRoute()
    }
}
