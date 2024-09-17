package org.maternalcare.modules.main.residence.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.service.UserService
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

@HiltViewModel
class ResidenceViewModel @Inject constructor(
    private val userService: UserService
) : ViewModel() {
    fun fetchUsers(userId: ObjectId): List<UserDto> {
        return userService.fetch(isResidence = true, userId = userId)
    }
}