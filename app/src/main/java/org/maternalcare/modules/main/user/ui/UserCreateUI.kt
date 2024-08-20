package org.maternalcare.modules.main.user.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import org.maternalcare.modules.main.user.model.UserDto
import org.maternalcare.modules.main.user.viewmodel.UserViewModel

@Composable
fun UserCreateUI() {
    val userViewModel: UserViewModel = hiltViewModel()

    val onSubmit: suspend (UserDto) -> Unit = { user ->
        userViewModel.createUser(user)
    }

    UserForm(onSubmit = onSubmit)
}