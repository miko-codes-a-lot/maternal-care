package org.maternalcare.modules.main.chat.model.dto

import kotlinx.datetime.Instant
import org.maternalcare.modules.main.user.model.dto.UserDto

data class UserChatDto(
    val userDto: UserDto,
    val chatDto: ChatDto,
    val updatedAt: Instant?
)