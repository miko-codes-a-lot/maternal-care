package org.maternalcare.modules.main.chat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.maternalcare.modules.main.chat.model.dto.MessageDto


@Preview(showSystemUi = true)
@Composable
fun ChatScreenPreview() {
    val currentUserId = "user1"
    val messages = listOf(
        MessageDto(
            id = "1",
            chatId = "chat1",
            senderId = "user1",
            receiverId = "user2",
            content = "Hello!",
            createdAt = Clock.System.now()
        ),
        MessageDto(
            id = "2",
            chatId = "chat1",
            senderId = "user2",
            receiverId = "user1",
            content = "Hi there!",
            createdAt = Clock.System.now()
        )
    )

    ChatScreenUI(
        messages = messages,
        currentUserId = currentUserId,
        onSendMessage = { /* Handle send message */ }
    )
}


@Composable
fun ChatScreenUI(
    messages: List<MessageDto>,
    currentUserId: String,
    onSendMessage: suspend (String) -> Unit
) {
    var messageContent by remember { mutableStateOf(TextFieldValue("")) }
    val isSendingMessage = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            reverseLayout = true,
            contentPadding = PaddingValues(8.dp)
        ) {
            items(items = messages) { message ->
                MessageBubble(
                    message = message,
                    isSentByCurrentUser = message.senderId == currentUserId
                )
            }
        }
        HorizontalDivider()
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = messageContent,
                onValueChange = { messageContent = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .background(Color.LightGray, RoundedCornerShape(16.dp))
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                enabled = !isSendingMessage.value,
                onClick = {
                    isSendingMessage.value = true
                    if (messageContent.text.isNotBlank()) {
                        scope.launch {
                            onSendMessage(messageContent.text)
                            messageContent = TextFieldValue("")
                            isSendingMessage.value = false
                        }
                    }
                }
            ) {
                Text("Send")
            }
        }
    }
}

@Composable
fun MessageBubble(message: MessageDto, isSentByCurrentUser: Boolean) {
    val alignment = if (isSentByCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
    val backgroundColor = if (isSentByCurrentUser) Color(0xFFDCF8C6) else Color.White
    val textColor = if (isSentByCurrentUser) Color.Black else Color.DarkGray
    val padding = if (isSentByCurrentUser) PaddingValues(start = 64.dp, end = 8.dp)
    else PaddingValues(start = 8.dp, end = 64.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding),
        contentAlignment = alignment
    ) {
        Column(
            modifier = Modifier
                .background(backgroundColor, RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Text(
                text = message.content,
                color = textColor,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = message.createdAt.toString(), // Format as needed
                color = Color.Gray,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

