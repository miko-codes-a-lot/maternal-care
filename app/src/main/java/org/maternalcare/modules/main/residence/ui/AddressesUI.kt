package org.maternalcare.modules.main.residence.ui

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
fun AddressesUI(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            navController.navigate(MainNav.Residences(CheckupStatus.ALL.name))
        }) {
            Text(text = "Address 1, Address 2")
        }
    }
}