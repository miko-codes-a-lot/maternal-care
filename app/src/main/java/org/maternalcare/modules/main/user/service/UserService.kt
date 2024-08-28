package org.maternalcare.modules.main.user.service

import org.maternalcare.modules.main.user.model.dto.UserDto
import java.util.UUID
import javax.inject.Inject

class UserService @Inject constructor() {
    suspend fun fetch(): List<UserDto> {
        return emptyList()
    }

    suspend fun create(user: UserDto): Result<UserDto> {
        return try {
            user.id = UUID.randomUUID().toString()

            if (user.id.isNotEmpty()) {
                throw Exception("Testing")
            }

            Result.success(user)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}