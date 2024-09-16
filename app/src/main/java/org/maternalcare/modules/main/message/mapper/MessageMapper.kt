package org.maternalcare.modules.main.message.mapper

import org.maternalcare.modules.main.message.model.dto.MessageDto
import org.maternalcare.modules.main.message.model.entity.Message
import org.maternalcare.shared.ext.toInstantString
import org.maternalcare.shared.ext.toObjectId
import org.maternalcare.shared.ext.toRealmInstant

fun Message.toDTO(): MessageDto {
    return MessageDto(
        id = _id.toHexString(),
        senderId = senderId?.toHexString(),
        receiverId = receiverId?.toHexString(),
        date = date.toInstantString()
    )
}

fun MessageDto.toEntity(): Message {
    val messageDto = this
    return Message().apply {
        _id = messageDto.id.toObjectId()
        senderId = messageDto.senderId.toObjectId()
        receiverId = messageDto.receiverId.toObjectId()
        date = messageDto.date.toRealmInstant()
    }
}


