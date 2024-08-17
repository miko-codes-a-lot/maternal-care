package org.maternalcare.modules.main.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.maternalcare.R
import org.maternalcare.modules.main.MainNav

@Composable
fun MenuUI(navController: NavController) {
    Surface {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.arrow),
                contentDescription = "Exit Icon",
                tint = Color.Black,
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.TopStart)
                    .padding(start = 12.dp, top = 12.dp)
                    .offset(x = 10.dp, y = 25.dp)
            )
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.care),
                contentDescription = "Login Image",
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Pregnant", fontSize = 30.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(10.dp))

            Menu(navController)
        }
    }
}

@Composable
private fun Menu(navController: NavController) {
    Column {
        //Profile
        Spacer(modifier = Modifier.height(20.dp))
        MenuButton(text = "Profile") {
            navController.navigate(MainNav.Residence)
        }

        //Dashboard
        Spacer(modifier = Modifier.height(20.dp))
        MenuButton(text = "Dashboard") {
            // move to dashboard
        }

        //Archive
        Spacer(modifier = Modifier.height(20.dp))
        MenuButton(text = "Archive") {
            // move to Archive
        }

        //UserManagement
        Spacer(modifier = Modifier.height(20.dp))
        MenuButton(text = "Manage User") {
            // move to manage user
        }

        //UserSettings
        Spacer(modifier = Modifier.height(20.dp))
        MenuButton(text = "Settings") {
            // move to Settings
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
            .height(60.dp),
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