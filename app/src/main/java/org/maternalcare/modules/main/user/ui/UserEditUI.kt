package org.maternalcare.modules.main.user.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.user.model.dto.UserDto

@Composable
fun UserEditUI(navController: NavController, userDto: UserDto) {
    UserForm(
        title = "Edit Account",
        userDto = userDto,
        onSubmit = {
            navController.navigate(MainNav.User)
        },
        navController = navController
    )
}
