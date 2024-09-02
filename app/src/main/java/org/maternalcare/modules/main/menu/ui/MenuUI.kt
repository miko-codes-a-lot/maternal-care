package org.maternalcare.modules.main.menu.ui

import androidx.compose.foundation.Image
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Preview(showSystemUi = true)
@Composable
fun MenuUIPreview() {
    MenuUI(navController = rememberNavController())
}

@Composable
fun MenuUI(navController: NavController) {
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
                modifier = Modifier.size(100.dp)
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally)
            )
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
        modifier = Modifier.fillMaxWidth()
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