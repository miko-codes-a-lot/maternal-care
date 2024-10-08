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
import org.maternalcare.modules.main.user.model.dto.AddressDto
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.viewmodel.UserViewModel

@Composable
fun UserCreateUI(
    navController: NavController,
    currentUser: UserDto,
    addressDto: AddressDto? = null
) {
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
            currentUser = currentUser,
            onSubmit = { user ->
                coroutineScope.launch {
                    userDetails = user
                    onSubmit(user)
                }
            },
            addressDto = addressDto,
            navController = navController
        )
    } else {
        val onSave: suspend (UserDto) -> Unit = { userDto ->
            val result = userViewModel.upsertUser(userDto, currentUser)

            if (result.isSuccess) {
                navController.popBackStack()
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