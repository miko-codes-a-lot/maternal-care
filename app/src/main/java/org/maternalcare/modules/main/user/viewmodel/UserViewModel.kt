package org.maternalcare.modules.main.user.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.maternalcare.modules.main.user.model.UserDto
import org.maternalcare.modules.main.user.service.UserService
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userService: UserService
): ViewModel()  {
    suspend fun createUser(userDto: UserDto): Result<UserDto> {
        return this.userService.create(userDto)
    }
}