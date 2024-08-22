package org.maternalcare.modules.main.user.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.user.model.UserDto
import org.maternalcare.modules.main.user.viewmodel.UserViewModel

@Composable
fun UserCreateUI(navController: NavController) {
    val userViewModel: UserViewModel = hiltViewModel()

    val onSubmit: suspend (UserDto) -> Unit = { user ->
        userViewModel.createUser(user)
        navController.navigate(MainNav.User)
    }

    UserForm(title = "Create Account", onSubmit = onSubmit,navController)
}