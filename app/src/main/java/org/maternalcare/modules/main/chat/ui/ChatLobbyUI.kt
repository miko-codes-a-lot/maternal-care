package org.maternalcare.modules.main.chat.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import kotlinx.datetime.Clock
import org.maternalcare.modules.main.chat.model.dto.ChatDto
import org.maternalcare.modules.main.chat.model.dto.UserChatDto
import org.maternalcare.modules.main.user.model.dto.UserDto

@Preview(showSystemUi = true)
@Composable
fun ChatLobbyUIPreview() {
    ChatLobbyUI(
        listOf(
            UserChatDto(
                UserDto(
                    id = "12",
                    firstName = "Reymond",
                    middleName = "Sorreda",
                    lastName = "Pena"
                ),
                ChatDto(
                    id = "",
                    user1Id = "",
                    user2Id = "",
                    lastMessage = "Hello, I need some help please!",
                    isRead = false,
                    updatedAt = Clock.System.now()
                ),
                Clock.System.now()
            ),
            UserChatDto(
                UserDto(
                    id = "12",
                    firstName = "Darwin",
                    middleName = "Pena",
                    lastName = "Chu"
                ),
                ChatDto(
                    id = "",
                    user1Id = "",
                    user2Id = "",
                    lastMessage = "Please call me back, I need urgent care, I think my water broke, I'm panicking",
                    isRead = true,
                    updatedAt = Clock.System.now()
                ),
                Clock.System.now()
            ),
        )
    )
}

@Composable
fun ChatLobbyUI(data: List<UserChatDto>) {
    LazyColumn {
        items(items = data) { userChat ->
            ChatCard(userChat)
        }
    }
}

@Composable
fun ChatCard(userChat: UserChatDto) {
    val user = userChat.userDto
    val chat = userChat.chatDto

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { /* Handle click event */ },
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // User Avatar
            Image(
                painter = rememberImagePainter(data = "https://img.freepik.com/premium-vector/default-avatar-profile-icon-social-media-user-image-gray-avatar-icon-blank-profile-silhouette-vector-illustration_561158-3467.jpg"),
                contentDescription = "${user.firstName} ${user.lastName}'s avatar",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // User Name and Last Message
            Column {
                Text(
                    text = "${user.firstName} ${user.middleName} ${user.lastName}",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = chat.lastMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (chat.isRead) FontWeight.Normal else FontWeight.Bold,
                    color = if (chat.isRead) Color.Gray else Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}