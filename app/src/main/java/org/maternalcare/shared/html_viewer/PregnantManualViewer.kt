package org.maternalcare.shared.html_viewer

import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun PregnantManualViewer(
    fileName: String
) {
    val context = LocalContext.current
    AndroidView(
        factory = {
            WebView(context).apply {
                loadUrl("file:///android_asset/$fileName")
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}