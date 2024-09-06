package org.maternalcare.modules.main.user.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.viewmodel.UserViewModel

@Composable
fun UserCreateUI(navController: NavController) {
    val userViewModel: UserViewModel = hiltViewModel()
    var userDetails by remember { mutableStateOf(UserDto()) }
    var showForm by remember { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()

    if (showForm) {
        val onSubmit: suspend (UserDto) -> Unit = { user ->
            userDetails = user
            showForm = false
        }

        UserForm(
            userDto = userDetails,
            title = "Create Account",
            onSubmit = { user ->
                coroutineScope.launch {
                    userDetails = user
                    onSubmit(user)
                }
            },
            navController = navController
        )
    } else {
        val onSave: suspend (UserDto) -> Unit = { userDto ->
            val result = userViewModel.upsertUser(userDto)

            if (result.isSuccess) {
                navController.navigate(MainNav.User){
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            } else {
                Log.e("micool", "Something went wrong: ${result.exceptionOrNull()}")
            }
        }

        UserPreviewUI(
            navController = navController,
            user = userDetails,
            title = "Preview Account",
            onSave = onSave,
            onCancel = {
                userDetails.password = ""
                showForm = true
            }
        )
    }
}