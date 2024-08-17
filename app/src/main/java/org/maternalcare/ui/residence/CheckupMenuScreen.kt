package org.maternalcare.ui.residence

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CheckupMenuScreen() {
    val items = listOf("First Checkup", "Second Checkup", "Third Checkup", "Fourth Checkup")
    LazyColumn(modifier = Modifier) {
        items(items) { item ->
            Text(
                text = item,
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color.LightGray)
                    .padding(16.dp)
            )
        }
    }
}