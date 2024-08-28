package org.maternalcare.modules.main.user.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.viewmodel.UserViewModel

@Composable
fun UserCreateUI(navController: NavController) {
    val userViewModel: UserViewModel = hiltViewModel()
    var userDetails by remember { mutableStateOf(UserDto()) }
    var showForm by remember { mutableStateOf(true) }

    val onSubmit: suspend (UserDto) -> Unit = { user ->
        userViewModel.createUser(user)
        userDetails = user
        showForm = false
    }

    val coroutineScope = rememberCoroutineScope()

    if (showForm) {
        UserForm(title = "Create Account", onSubmit = { user ->
            coroutineScope.launch {
                onSubmit(user)
            }
        } , navController)
    } else {
        UserPreviewUI( navController = navController, user = userDetails, title = "Preview Account", onSave = { userDto ->
                userViewModel.createUser(userDto)
            }
        )
    }
}