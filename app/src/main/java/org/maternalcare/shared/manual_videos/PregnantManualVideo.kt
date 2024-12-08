package org.maternalcare.shared.manual_videos

import android.webkit.WebView
import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import java.io.File

@Composable
fun PregnantManualVideo(fileName: String = "videos/PREGNANT.mp4", startPlaying: Boolean ) {
    val context = LocalContext.current
    val videoPath = remember {
        File(context.cacheDir, "temp_video.mp4").apply {
            if (!exists()) {
                context.assets.open(fileName).use { input ->
                    outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            }
        }.absolutePath
    }

    AndroidView(
        factory = { ctx ->
            VideoView(ctx).apply {
                setVideoPath(videoPath)
                setMediaController(MediaController(ctx).apply {
                    setAnchorView(this@apply)
                })
                setOnPreparedListener {
                    it.isLooping = true
                    start()
                }
            }
        },
        update = { videoView ->
            if (startPlaying) videoView.start() else videoView.pause()
        },
        modifier = Modifier
            .fillMaxSize()
            .border(width = 1.dp, Color.Black)
            .padding(16.dp)
            .background(Color.Black)
    )
}
