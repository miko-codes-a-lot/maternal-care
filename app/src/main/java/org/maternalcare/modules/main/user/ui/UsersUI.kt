package org.maternalcare.modules.main.user.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import org.maternalcare.modules.main.MainNav

@Composable
fun UsersUI(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {}) {
            Text(text = "User1, User2, User3")
        }
        Button(onClick = {
            navController.navigate(MainNav.CreateUser)
        }) {
            Text(text = "Create User")
        }
    }
}