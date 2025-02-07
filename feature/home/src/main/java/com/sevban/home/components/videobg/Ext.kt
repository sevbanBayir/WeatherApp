package com.sevban.home.components.videobg

import android.content.Context
import android.net.Uri
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout.RESIZE_MODE_ZOOM
import androidx.media3.ui.PlayerView

fun Context.buildExoPlayer(uri: Uri) =
    ExoPlayer.Builder(this).build().apply {
        setMediaItem(MediaItem.fromUri(uri))
        repeatMode = Player.REPEAT_MODE_ALL
        playWhenReady = true
        volume = 0f
        prepare()
    }

@OptIn(UnstableApi::class)
fun Context.buildPlayerView(exoPlayer: ExoPlayer) =
    PlayerView(this).apply {
        player = exoPlayer
        layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        useController = false
        resizeMode = RESIZE_MODE_ZOOM

    }

fun Context.getVideoUri(videoName: String): Uri {
    val rawId = resources.getIdentifier(videoName, "raw", packageName)
    val videoUri = "android.resource://$packageName/$rawId"
    return Uri.parse(videoUri)
}
