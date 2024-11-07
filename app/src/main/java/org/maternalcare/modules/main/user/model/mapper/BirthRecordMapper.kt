package org.maternalcare.modules.main.user.model.mapper

import org.maternalcare.modules.main.user.model.dto.UserBirthRecordDto
import org.maternalcare.modules.main.user.model.entity.UserBirthRecord
import org.maternalcare.shared.ext.toInstantString
import org.maternalcare.shared.ext.toObjectId
import org.maternalcare.shared.ext.toRealmInstant

fun UserBirthRecord.toDTO(): UserBirthRecordDto {
    return UserBirthRecordDto(
        id = _id.toHexString(),
        pregnancyUserId = pregnancyUserId,
        childOrder = childOrder,
        pregnancyOrder = pregnancyOrder,
        fillDate = fillDate.toInstantString()
    )
}

fun UserBirthRecordDto.toEntity(): UserBirthRecord {
    val birthRecordDto = this
    return UserBirthRecord().apply {
        _id = birthRecordDto.id.toObjectId()
        pregnancyUserId = birthRecordDto.pregnancyUserId
        childOrder = birthRecordDto.childOrder
        pregnancyOrder = birthRecordDto.pregnancyOrder
        fillDate = birthRecordDto.fillDate.toRealmInstant()
    }
}