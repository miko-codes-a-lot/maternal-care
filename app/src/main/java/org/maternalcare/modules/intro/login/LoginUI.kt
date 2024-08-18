package org.maternalcare.modules.intro.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.maternalcare.R
import org.maternalcare.modules.main.MainNav

@Composable
fun LoginUI(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.care),
            contentDescription = "Login Image",
            modifier = Modifier.size(180.dp)
        )
        Spacer(modifier = Modifier.height(25.dp))
        Text(text = "Login to your account", fontSize = 22.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(10.dp))


        // Email
        OutlinedTextField(value = "", onValueChange = {},
            label = {Text(text = "Email Account")},
            leadingIcon = {
                Icon(imageVector = Icons.Default.Person,contentDescription = null)
            },modifier = Modifier.fillMaxWidth(0.7f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password
        OutlinedTextField(value = "", onValueChange = {}, label = {
            Text(text = "Password")},
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock,contentDescription = null)
            },modifier = Modifier.fillMaxWidth(0.7f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Login
        ElevatedButton(onClick = {navController.navigate(MainNav.Menu)},
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(55.dp)
            , shape = RectangleShape,
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = Color(0xFF6650a4),
                contentColor = Color(0xFFFFFFFF))
        ) {
            Text(text = "Login",fontSize = 12.sp,fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(110.dp))

    }
}