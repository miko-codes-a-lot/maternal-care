package org.maternalcare.modules.main.message.message.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Preview(showSystemUi = true)
@Composable
fun MessageUIPreview() {
    MessageUI(navController = rememberNavController())
}

@Composable
fun MessageUI(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "This is Message Container")
        MessageContainer()
    }
}

@Composable
fun MessageContainer() {
    val messages = listOf(
        SMSMessage(type = 1, message = "Hello from User", date = "2024-09-01 10:00:00"),
        SMSMessage(type = 2, message = "Hello from Client", date = "2024-09-01 10:05:00")
    )

    LazyColumn(
        modifier = Modifier
            .border(1.dp, color = Color.Gray)
            .height(380.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(messages) { message ->
            SenderView(sender = if (message.type == 1) "User" else "Client")
            MessageView(message = message)
        }

    }
}

data class SMSMessage(
    val type: Int,
    val message: String,
    val date: String
)

@Composable
fun MessageView(message: SMSMessage) {
    // State to keep track of whether the message is expanded
    var isExpanded = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = if (message.type == 1) Arrangement.Start else Arrangement.End
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(10.dp)
                .clickable { isExpanded.value = !isExpanded.value }, // Toggle expansion on click
            shape = RoundedCornerShape(12.dp),
            color = if (message.type == 1) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.primary
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = message.message)
                if (isExpanded.value) {
                    // Show full message when expanded
                    Text(
                        text = "Full message content: ${message.message}", // Example, replace with actual logic
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = message.date.split(" ").last(),
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(0.38f)
                )
            }
        }
    }
}


@Composable
fun SenderView(sender: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .size(64.dp)
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = sender,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(12.dp)
        )
    }
}