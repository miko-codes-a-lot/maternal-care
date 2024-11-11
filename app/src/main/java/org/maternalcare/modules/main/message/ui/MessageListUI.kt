package org.maternalcare.modules.main.message.ui

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import org.maternalcare.R
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.message.viewmodel.MessageViewModel
import org.maternalcare.modules.main.user.model.dto.UserDto

@Preview(showSystemUi = true)
@Composable
fun MessageListUIPreview() {
    MessageListUI(navController = rememberNavController(), currentUser = UserDto())
}

@Composable
fun MessageListUI(navController: NavController, currentUser: UserDto) {
    val messageViewModel: MessageViewModel = hiltViewModel()
    val users by produceState<List<UserDto>>(emptyList(), messageViewModel) {
        value = messageViewModel.getResidences(currentUser.id!!)
    }
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Inbox",
                fontFamily = FontFamily.Serif,
                fontSize = 23.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth(),
                color = Color(0xFF6650a4)
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(users) { userDto ->
                ListOfMessages(userDto = userDto,navController = navController)
            }
        }
    }
}

@Composable
fun ListOfMessages(userDto: UserDto, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp)
                .clickable {
                    navController.navigate(MainNav.Messages(userId = userDto.id!!))
                },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ImageContainer()
            Text(
                text = "${userDto.firstName} ${userDto.lastName}",
                fontSize = 19.sp,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f)
            )
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
            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}

@Composable
fun ImageContainer(imageUri: Uri? = null) {
    Box(
        Modifier.height(45.dp)
    ){
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFF6650a4))
                .border(3.dp, Color(0xFF6650a4), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(35.dp)
                        .clip(CircleShape)
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.person),
                    contentDescription = "Default placeholder",
                    modifier = Modifier.size(30.dp),
                    tint = Color.White
                )
            }
        }
    }
}