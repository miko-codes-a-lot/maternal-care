package org.maternalcare.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import org.maternalcare.modules.intro.login.viewmodel.LoginViewModel
import org.maternalcare.modules.main.user.model.dto.UserDto


@Composable
fun Guard(navController: NavController, render: @Composable (currentUser: UserDto) -> Unit) {
    val loginViewModel: LoginViewModel = hiltViewModel()
    val userId = rememberSaveable { mutableStateOf(loginViewModel.getLoggedInUserId()) }
    if (userId.value != null) {
        val currentUser = loginViewModel.getUserLocal()!!
        render(currentUser)
    } else {
        loginViewModel.logout(navController)
    }
}