package org.maternalcare.modules.main.message.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.maternalcare.R
import org.maternalcare.modules.main.message.model.dto.MessageDto
import org.maternalcare.modules.main.message.model.entity.Message
import org.maternalcare.modules.main.message.viewmodel.MessageViewModel
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.shared.ext.toObjectId

data class SMSMessage(
    val type: Int,
    val message: String,
    val date: String
)

@Preview(showSystemUi = true)
@Composable
fun MessageUIPreview() {
    MessageUI(
        navController = rememberNavController(),
        currentUser = UserDto(),
        userDto = UserDto()
    )
}

@Composable
fun MessageUI(navController: NavController, currentUser: UserDto, userDto: UserDto) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MessageContainer(userDto = userDto, currentUser = currentUser)
    }
}

@Composable
fun MessageContainer(currentUser: UserDto, userDto: UserDto) {
    val messageViewModel: MessageViewModel = hiltViewModel()
    val coroutineScope = rememberCoroutineScope()
    val messages = remember { mutableStateListOf<Message>() }

    LaunchedEffect("Message") {
        launch {
            val flow = messageViewModel.fetchMessagesAsync(
                currentUser.id.toObjectId(),
                userDto.id.toObjectId()
            )
            flow.collect { results ->
                Log.d("micool", "live changes: ${results.list}")
                messages.clear()
                results.list.forEach { item -> messages.add((item)) }
            }
        }
    }

    Column(modifier = Modifier
        .background(Color.White)
//        .height(660) Original Size
        .height(750.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .background(Color.White)
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            reverseLayout = true
        ) {
            items(messages) { message ->
                MessageView(message = message, currentUser, userDto)
            }
        }
        MessageInputField { newMessage ->
            coroutineScope.launch {
                val messageDto = MessageDto(
                    senderId = currentUser.id,
                    receiverId = userDto.id,
                    content = newMessage,
                )
                val result = messageViewModel.sendMessage(messageDto)

                if (result.isSuccess) {
                    Log.d("micool", "Successful!")
                } else {
                    Log.e("micool", "error: " + result.exceptionOrNull())
                }
            }
        }
    }
}

@Composable
fun MessageView(message: Message, currentUser: UserDto, userDto: UserDto) {
    val isReceiver = message.senderId?.toHexString() == currentUser.id

    val (containerColor, contentColor) =
        if (isReceiver) {
            Color(0xFF6650a4) to Color(0xFFFFFFFF)
        } else {
            Color(0xFFcccccf) to Color.Black
        }

    val horizontalArrangement =
        if (isReceiver)
            Arrangement.End
        else
            Arrangement.Start

    Row(
        modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth(),
        horizontalArrangement = horizontalArrangement
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
                    text = message.content ?: "",
                    maxLines = Int.MAX_VALUE,
                    overflow = TextOverflow.Visible
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "",
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
fun MessageInputField(onSend: (message: String) -> Unit) {
    val messageText = remember { mutableStateOf("") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFF6650a4), RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextField(
            value = messageText.value,
            onValueChange = { messageText.value = it },
            placeholder = { Text("Type your message...")},
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White
            )
        )
        Icon(
            painter = painterResource(id = R.drawable.icon_send),
            contentDescription = "Send",
            modifier = Modifier
                .padding(8.dp)
                .size(25.dp)
                .clickable {
                    onSend(messageText.value)
                    messageText.value = ""
                },
            tint = Color(0xFF6650a4)
        )
        Spacer(modifier = Modifier.padding(end = 10.dp))
    }
}
