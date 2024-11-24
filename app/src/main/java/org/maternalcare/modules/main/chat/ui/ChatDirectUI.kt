package org.maternalcare.modules.main.chat.ui

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.maternalcare.R
import org.maternalcare.modules.main.chat.model.dto.MessageDto
import java.util.Locale


@Preview(showSystemUi = true)
@Composable
fun ChatDirectPreview() {
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

    ChatDirectUI(
        messages = messages,
        currentUserId = currentUserId,
        onSendMessage = { /* Handle send message */ }
    )
}

@Composable
fun ChatDirectUI(
    messages: List<MessageDto>,
    currentUserId: String,
    onSendMessage: suspend (String) -> Unit
) {
    var messageContent by remember { mutableStateOf(TextFieldValue("")) }
    val isSendingMessage = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(top = 40.dp)
                .fillMaxWidth()
                .fillMaxHeight()
                .weight(1f),
            reverseLayout = true,
        ) {
            items(items = messages) { message ->
                MessageBubble(
                    message = message,
                    isSentByCurrentUser = message.senderId == currentUserId
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp))
                .border(1.dp, Color(0xFF6650a4), RoundedCornerShape(8.dp))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = messageContent,
                onValueChange = { messageContent = it },
                placeholder = { Text("Type your message...",color = Color.Black)},
                modifier = Modifier
                    .weight(0.7f)
                    .padding(end = 2.dp)
                    .verticalScroll(rememberScrollState()),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedBorderColor = Color(0xFF6650a4),
                    unfocusedBorderColor = Color(0xFF6650a4)
                ),
                maxLines = 2
            )
            Icon(
                painter = painterResource(id = R.drawable.icon_send),
                contentDescription = "Send",
                modifier = Modifier
                    .size(25.dp)
                    .clickable {
                        if (isSendingMessage.value) return@clickable
                        isSendingMessage.value = true
                        if (messageContent.text.isNotBlank()) {
                            scope.launch {
                                onSendMessage(messageContent.text)
                                messageContent = TextFieldValue("")
                                isSendingMessage.value = false
                            }
                        }
                    },
                tint = Color(0xFF6650a4)
            )
            Spacer(modifier = Modifier.width(10.dp))
        }
        Spacer(modifier = Modifier.height(45.dp))
    }
}

@Composable
fun MessageBubble(message: MessageDto, isSentByCurrentUser: Boolean) {
    val alignment = if (isSentByCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
    if (isSentByCurrentUser) {
        PaddingValues(start = 64.dp, end = 8.dp)
    }else {
        PaddingValues(start = 8.dp, end = 64.dp)
    }
    val (containerColor, contentColor) =
        if (isSentByCurrentUser) {
            Color(0xFF6650a4) to Color(0xFFFFFFFF)
        } else {
            Color(0xFFcccccf) to Color.Black
        }

    val displayFormat = remember { SimpleDateFormat("MMM-dd-yyyy", Locale.getDefault()) }
    val formattedDate = try {
        val parsedDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(message.createdAt.toString())
        parsedDate?.let { displayFormat.format(it) } ?: "Invalid Date"
    } catch (e: Exception) {
        "Invalid Date"
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp),
        contentAlignment = alignment
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(top = 10.dp, bottom = 10.dp),
            shape = RoundedCornerShape(12.dp),
            color = containerColor,
            contentColor = contentColor
        ) {
            Column(modifier = Modifier
                .padding(12.dp)
            ) {
                Text(
                    text = message.content,
                    maxLines = Int.MAX_VALUE,
                    overflow = TextOverflow.Visible,
                    fontFamily = FontFamily.SansSerif,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = formattedDate,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    textAlign = TextAlign.End,
                    fontFamily = FontFamily.SansSerif,
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(0.38f),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}