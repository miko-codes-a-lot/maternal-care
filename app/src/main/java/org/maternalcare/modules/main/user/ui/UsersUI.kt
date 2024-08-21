package org.maternalcare.modules.main.user.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.user.model.UserDto
import org.maternalcare.modules.main.user.viewmodel.UserViewModel

@Composable
fun UsersUI(navController: NavController) {
    val userViewModel: UserViewModel = hiltViewModel()

    val users by produceState<List<UserDto>>(emptyList(), userViewModel) {
        value = userViewModel.fetchUsers()
    }

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

        LazyColumn{
            items(users) { user ->
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(user.email)
                }
            }
        }

    }
}