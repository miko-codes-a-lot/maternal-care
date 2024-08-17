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
fun AddressListScreen() {
    val items = listOf("A1", "A2", "A3", "A4", "A5")
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