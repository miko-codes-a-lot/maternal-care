package org.maternalcare.modules.main.chat.mapper

import org.maternalcare.modules.main.chat.model.dto.ChatDto
import org.maternalcare.modules.main.chat.model.entity.Chat
import org.maternalcare.shared.ext.toInstantK
import org.maternalcare.shared.ext.toObjectId
import org.maternalcare.shared.ext.toRealmInstant

fun Chat.toDTO(): ChatDto {
    return ChatDto(
        id = _id.toHexString(),
        user1Id = user1Id!!.toHexString(),
        user2Id = user2Id!!.toHexString(),
        lastMessage = lastMessage,
        isRead = isRead,
        updatedAt = updatedAt.toInstantK(),
    )
}

fun ChatDto.toEntity(): Chat {
    val chatDto = this
    return Chat().apply {
        _id = chatDto.id.toObjectId()
        user1Id = chatDto.user1Id.toObjectId()
        user2Id = chatDto.user2Id.toObjectId()
        lastMessage = chatDto.lastMessage
        isRead = chatDto.isRead
        updatedAt = chatDto.updatedAt.toRealmInstant()
    }
}