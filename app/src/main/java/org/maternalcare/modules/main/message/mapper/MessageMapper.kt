package org.maternalcare.modules.main.message.mapper

import io.realm.kotlin.types.RealmInstant
import org.maternalcare.modules.main.message.model.dto.MessageDto
import org.maternalcare.modules.main.message.model.entity.Message
import org.maternalcare.shared.ext.toInstantStringNullable
import org.maternalcare.shared.ext.toObjectId
import org.maternalcare.shared.ext.toRealmInstant

fun Message.toDTO(): MessageDto {
    return MessageDto(
        id = _id.toHexString(),
        senderId = senderId?.toHexString(),
        receiverId = receiverId?.toHexString(),
        content = content,
        date = date?.toInstantStringNullable(),
        isRead = isRead
    )
}

fun MessageDto.toEntity(): Message {
    val messageDto = this
    return Message().apply {
        _id = messageDto.id.toObjectId()
        senderId = messageDto.senderId.toObjectId()
        receiverId = messageDto.receiverId.toObjectId()
        content = messageDto.content
        date = messageDto.date?.toRealmInstant() ?: RealmInstant.now()
        isRead = messageDto.isRead
    }
}


