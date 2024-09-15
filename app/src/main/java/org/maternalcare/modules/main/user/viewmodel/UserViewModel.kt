package org.maternalcare.modules.main.user.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.service.UserService
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    val userService: UserService
): ViewModel()  {
    fun fetchUsers(): List<UserDto> {
        return this.userService.fetch()
    }

    suspend fun upsertUser(userDto: UserDto): Result<UserDto> {
        return this.userService.upsert(userDto)
    }

    fun fetchUser(userId: String): UserDto {
        return this.userService.fetchOne(userId)
    }
}