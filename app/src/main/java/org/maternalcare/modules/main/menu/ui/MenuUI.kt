package org.maternalcare.modules.main.menu.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.maternalcare.R
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.menu.model.MenuItem
import org.maternalcare.modules.main.residence.enum.CheckupStatus
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.shared.ui.ReminderAlertUI

@Preview(showSystemUi = true)
@Composable
fun MenuUIPreview() {
    MenuUI(navController = rememberNavController(), currentUser = UserDto())
}

@Composable
fun MenuUI(navController: NavController, currentUser: UserDto) {
    val isReminderAlertVisible = rememberSaveable { mutableStateOf(true) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(id = R.drawable.care),
                contentDescription = "Login Image",
                modifier = Modifier
                    .size(100.dp)
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally)
            )
            if (isReminderAlertVisible.value) {
                ReminderAlertUI(
                    isReminderAlert = true,
                    onDismiss = { isReminderAlertVisible.value = false }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            UserPosition(userDto = currentUser)
            Spacer(modifier = Modifier.height(13.dp))
            Menu(navController = navController, userDto = currentUser)
        }
    }
}

@Composable
fun UserPosition(userDto: UserDto) {
    val userDetails = when {
        userDto.isSuperAdmin -> listOf("Super Admin")
        userDto.isAdmin -> listOf("Admin")
        userDto.isResidence -> listOf("Residence")
        else -> listOf("Unknown")
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        userDetails.forEach { userPosition ->
            Text(
                text = userPosition,
                fontSize = 24.sp,
                fontFamily = FontFamily.Serif
            )
        }
    }
}

@Composable
private fun Menu(navController: NavController, userDto: UserDto) {
    val menuItems = getMenuItems(navController = navController, userDto = userDto)
    Box(
        modifier = Modifier
            .height(395.dp)
            .border(
                BorderStroke(2.dp, Color(0xFF6650a4)),
                RoundedCornerShape(14.dp),
            )
            .background(
                Color(0xFFf3f0fc),
                shape = RoundedCornerShape(14.dp)
            )
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp)
                .height(380.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(menuItems) { menuItem ->
                Spacer(modifier = Modifier.padding(top = 4.dp))

                MenuButton(text = menuItem.text, onClick = menuItem.action)

                Spacer(modifier = Modifier.padding(bottom = 10.dp))
            }
        }
    }
}

fun getMenuItems(userDto: UserDto, navController: NavController): List<MenuItem> {
    return when {
        userDto.isSuperAdmin -> listOf(
            MenuItem(text = "Profile") {
                navController.navigate(MainNav.Addresses(CheckupStatus.ALL.name))
            },
            MenuItem(text = "Dashboard") {
                navController.navigate(MainNav.Dashboard)
            },
            MenuItem(text = "Manage User") {
                navController.navigate(MainNav.User)
            },
            MenuItem(text = "Data Storage") {
                navController.navigate(MainNav.Addresses(CheckupStatus.ALL.name, isArchive = true))
            },
            MenuItem(text = "Settings") {
                navController.navigate(MainNav.Settings)
            }
        )
        userDto.isAdmin -> listOf(
            MenuItem(text = "Profile") {
                navController.navigate(MainNav.Addresses(CheckupStatus.ALL.name))
            },
            MenuItem(text = "Messages") {
                navController.navigate(MainNav.MessagesList)
            },
            MenuItem(text = "Reminders") {
                navController.navigate(MainNav.ReminderLists)
            },
            MenuItem(text = "Data Storage") {
                navController.navigate(MainNav.Addresses(CheckupStatus.ALL.name, isArchive = true))
            },
            MenuItem(text = "Settings") {
                navController.navigate(MainNav.Settings)
            }
        )
        userDto.isResidence -> listOf(
            MenuItem(text = "Profile") {
                navController.navigate(MainNav.ChooseCheckup(userId = userDto.id!!))
            },
            MenuItem(text = "Messages") {
                navController.navigate(MainNav.MessagesList)
            },
            MenuItem(text = "Reminders") {
                navController.navigate(MainNav.ReminderLists)
            },
            MenuItem(text = "Settings") {
                navController.navigate(MainNav.Settings)
            }
        )
        else -> listOf()
    }
}

@Composable
private fun MenuButton(text: String, onClick: () -> Unit) {
    ElevatedButton(onClick = onClick,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor =  Color(0xFF6650a4),
            contentColor = Color(0xFFFFFFFF)
        ),
        modifier = Modifier
            .width(280.dp)
            .height(63.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Text(
            text = text,
            fontSize = 17.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
    }
}

@Composable
fun CheckUpDateContainer() {
    val checkUpDetails = listOf( "August 24, 2024" )
    Row(
        modifier = Modifier
            .padding(top = 10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        checkUpDetails.forEach { dates ->
            Text(
                text = dates,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = Color(0xFF6650a4),
                fontFamily = (FontFamily.SansSerif)
            )
        }
    }
}

@Composable
fun TextContainer(text: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(text.contains("Reminder")){
            Text(
                text = text,
                textAlign = TextAlign.Center,
                color =Color(0xFF6650a4),
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = (FontFamily.SansSerif),
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }else{
            Text(
                text = text,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = Color(0xFF6650a4),
                fontFamily = (FontFamily.SansSerif)
            )
        }
    }
}