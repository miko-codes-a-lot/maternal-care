package org.maternalcare.modules.main.user.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.viewmodel.UserViewModel

@Composable
fun UserEditUI(navController: NavController, userDto: UserDto) {
    val userViewModel: UserViewModel = hiltViewModel()
    var userDetails by remember { mutableStateOf(UserDto()) }
    var showForm by remember { mutableStateOf(true) }

    if (showForm) {
        UserForm(
            title = "Edit Account",
            userDto = userDto,
            onSubmit = { user ->
                userDetails = user
                showForm = false
            },
            navController = navController,
            includePassword = false
        )
    } else {
        UserPreviewUI(
            navController = navController,
            user = userDetails,
            title = "Review Changes",
            onSave = { user ->
                val result = userViewModel.upsertUser(user)

                if (result.isSuccess) {
                    navController.navigate(MainNav.User){
                        popUpTo(MainNav.Menu) { inclusive = true }
                    }
                } else {
                    Log.e("micool", "Something went wrong: ${result.exceptionOrNull()}")
                }
            },
            onCancel = {
                showForm = true
            }
        )
    }
}
