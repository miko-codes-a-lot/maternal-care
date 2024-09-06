package org.maternalcare.modules.main.menu.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.maternalcare.R
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.menu.model.MenuItem
import org.maternalcare.modules.main.residence.enum.CheckupStatus

@Preview(showSystemUi = true)
@Composable
fun MenuUIPreview() {
    MenuUI(navController = rememberNavController())
}

@Composable
fun MenuUI(navController: NavController) {
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
                ReminderAlert(
                    isReminderAlert = true,
                    onDismiss = { isReminderAlertVisible.value = false }
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            UserPosition()
            Spacer(modifier = Modifier.height(13.dp))
            Menu(navController)
        }
    }
}

@Composable
fun UserPosition() {
    val userDetails = listOf( "Super Admin" )
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
private fun Menu(navController: NavController) {
    val menuItems = listOf(
        MenuItem(text = "Profile") {
            navController.navigate(MainNav.Addresses(CheckupStatus.ALL.name))
        },
        MenuItem(text = "Messages") {
            navController.navigate(MainNav.MessagesList)
        },
        MenuItem(text = "Reminder") {
            navController.navigate(MainNav.ReminderLists)
        },
        MenuItem(text = "Dashboard") {
            navController.navigate(MainNav.Dashboard)
        },
        MenuItem(text = "Data Storage") {
            navController.navigate(MainNav.Addresses(CheckupStatus.ALL.name, isArchive = true))
        },
        MenuItem(text = "Manage User") {
            navController.navigate(MainNav.User)
        },
        MenuItem(text = "Settings") {
            navController.navigate(MainNav.Settings)
        }
    )
    LazyColumn(
        modifier = Modifier
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ReminderAlert(
    onDismiss : () -> Unit,
    isReminderAlert: Boolean = false
) {
    val listOfText = listOf("Reminder","Your next check-up will be on.")
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .height(169.dp)
                .border(
                    4.dp,
                    color = Color(0xFF6650a4),
                    shape = RoundedCornerShape(15.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 15.dp, bottom = 15.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                listOfText.forEach{ text ->
                    TextContainer(text = text)
                }

                CheckUpDateContainer()
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { onDismiss() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6650a4),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .height(32.dp)
                ) {
                    Text(
                        text = "Close",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
fun CheckUpDateContainer() {
    var checkUpDetails = listOf( "August 24, 2024" )
    Row(
        modifier = Modifier.fillMaxWidth(),
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
                fontFamily = (FontFamily.SansSerif)
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