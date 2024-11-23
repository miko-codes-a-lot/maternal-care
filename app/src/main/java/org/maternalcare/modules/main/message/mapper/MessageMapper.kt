package org.maternalcare.modules.main.message.mapper

import io.realm.kotlin.types.RealmInstant
import org.maternalcare.modules.main.message.model.dto.MessageDto
import org.maternalcare.modules.main.message.model.entity.MessageDeprecate
import org.maternalcare.shared.ext.toInstantStringNullable
import org.maternalcare.shared.ext.toObjectId
import org.maternalcare.shared.ext.toRealmInstant

fun MessageDeprecate.toDTO(): MessageDto {
    return MessageDto(
        id = _id.toHexString(),
        senderId = senderId?.toHexString(),
        receiverId = receiverId?.toHexString(),
        content = content,
        date = date?.toInstantStringNullable()
    )
}

fun MessageDto.toEntity(): MessageDeprecate {
    val messageDto = this
    return MessageDeprecate().apply {
        _id = messageDto.id.toObjectId()
        senderId = messageDto.senderId.toObjectId()
        receiverId = messageDto.receiverId.toObjectId()
        content = messageDto.content
        date = messageDto.date?.toRealmInstant() ?: RealmInstant.now()
    }
}


