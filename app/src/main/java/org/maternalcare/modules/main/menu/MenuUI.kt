package org.maternalcare.modules.main.menu

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
import org.maternalcare.modules.main.residence.enum.CheckupStatus

@Composable
fun MenuUI(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            navController.navigate(MainNav.Addresses(CheckupStatus.ALL.name))
        }) {
            Text(text = "Profiles")
        }
        Button(onClick = {
            navController.navigate(MainNav.Dashboard)
        }) {
            Text(text = "Dashboard")
        }
    }
}