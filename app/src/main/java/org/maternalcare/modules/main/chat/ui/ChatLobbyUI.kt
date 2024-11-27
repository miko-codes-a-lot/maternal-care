package org.maternalcare.modules.main.chat.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.chat.model.dto.ChatDto
import org.maternalcare.modules.main.chat.model.dto.UserChatDto
import org.maternalcare.modules.main.residence.ui.SearchAddressIcon
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.ui.resizeBitmap

@Preview(showSystemUi = true)
@Composable
fun ChatLobbyUIPreview() {
    ChatLobbyUI(
        rememberNavController(),
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
fun ChatLobbyUI(navController: NavController, data: List<UserChatDto>) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var searchQuery by remember { mutableStateOf("") }
        var debouncedQuery by remember { mutableStateOf("") }

        LaunchedEffect(searchQuery) {
            delay(500L)
            debouncedQuery = searchQuery
        }

        val filteredData = remember(debouncedQuery, data) {
            if (debouncedQuery.isEmpty()) {
                data
            } else {
                data.filter { userChat ->
                    val fullName = "${userChat.userDto.firstName} ${userChat.userDto.middleName.orEmpty()} ${userChat.userDto.lastName}"
                    fullName.contains(debouncedQuery, ignoreCase = true)
                }
            }
        }

        Spacer(modifier = Modifier.height(50.dp))

        SearchMessageIcon(
            searchQuery = searchQuery,
            onSearchQueryChanged = { searchQuery = it },
        )

        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn {
            items(items = filteredData) { userChat ->
                ChatCard(navController = navController, userChat = userChat, searchQuery = debouncedQuery)
            }
        }
    }
}

@Composable
fun ChatCard(userChat: UserChatDto, navController: NavController, searchQuery: String = "") {
    val user = userChat.userDto
    val chat = userChat.chatDto

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 5.dp)
            .clickable {
                navController.navigate(MainNav.ChatDirect(userId = userChat.userDto.id!!))
            },
        colors = CardDefaults.cardColors(
            contentColor = Color.Black,
            containerColor = Color.White,
        )
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CardImageContainer(userChat.userDto.imageBase64)
            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = "${user.firstName} ${user.middleName} ${user.lastName}",
                    fontSize = 15.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = chat.lastMessage,
                    fontSize = 14.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = if (chat.isRead) FontWeight.Normal else FontWeight.Bold,
                    color = if (chat.isRead) Color.Gray else Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(bottom = 3.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            if (!chat.isRead) {
                Box(
                    modifier = Modifier
                        .background(
                            color = Color.Red,
                            shape = CircleShape
                        )
                        .size(13.dp)
                        .border(
                            width = 1.dp,
                            color = Color.Red,
                            shape = CircleShape
                        ),
                )
            }
        }
    }
}
@Composable
fun CardImageContainer(imageBase64: String? = null) {
    Box(
        Modifier.height(55.dp)
    ) {
        Box(
            modifier = Modifier
                .size(51.dp)
                .clip(CircleShape)
                .background(Color(0xFF6650a4))
                .border(1.dp, Color(0xFF6650a4), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (imageBase64 != null && imageBase64.isNotBlank()) {
                val resizedBitmap = remember(imageBase64) {
                    decodeBase64ToBitmap(imageBase64)?.let { bitmap ->
                        resizeBitmap(bitmap, maxWidth = 500, maxHeight = 500)
                    }
                }

                if (resizedBitmap != null) {
                    Image(
                        bitmap = resizedBitmap.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(51.dp)
                            .clip(CircleShape)
                    )
                } else {
                    PlaceholderImage()
                }
            } else {
                PlaceholderImage()
            }
        }
    }
}

@Composable
fun PlaceholderImage() {
    Image(
        painter = rememberAsyncImagePainter(model = "https://img.freepik.com/premium-vector/default-avatar-profile-icon-social-media-user-image-gray-avatar-icon-blank-profile-silhouette-vector-illustration_561158-3467.jpg"),
        contentDescription = "User's avatar",
        modifier = Modifier
            .size(51.dp)
            .clip(CircleShape)
            .background(Color(0xFF6650a4)),
    )
}

fun decodeBase64ToBitmap(base64Str: String): Bitmap? {
    return try {
        val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
        null
    }
}

@Composable
fun SearchMessageIcon(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = searchQuery,
        onValueChange = { onSearchQueryChanged(it) },
        leadingIcon = {
            IconButton(onClick = { } ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search Icon",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onSearchQueryChanged("") }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Clear Search",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Black,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            focusedBorderColor = Color.Black,
            disabledBorderColor = Color.Gray,
            errorBorderColor = Color.Red,
            cursorColor = Color.Black
        ),
        placeholder = {
            Text(
                text = "Search message...",
                color = Color.Black,
                fontFamily = FontFamily.SansSerif,
                fontSize = 16.sp
            )
        }
    )
}