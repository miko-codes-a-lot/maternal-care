package org.maternalcare.modules.main.user.model.mapper

import org.maternalcare.modules.main.user.model.dto.UserTrimesterRecordDto
import org.maternalcare.modules.main.user.model.entity.UserTrimesterRecord
import org.maternalcare.shared.ext.toObjectId

fun UserTrimesterRecord.toDTO(): UserTrimesterRecordDto {
    return UserTrimesterRecordDto(
        id = _id.toHexString(),
        trimesterUserId = trimesterUserId,
        pregnancyUserId = pregnancyUserId,
        trimesterOrder = trimesterOrder,
    )
}

fun UserTrimesterRecordDto.toEntity(): UserTrimesterRecord {
    val recordDto = this
    return UserTrimesterRecord().apply {
        _id = recordDto.id.toObjectId()
        trimesterUserId = recordDto.trimesterUserId
        pregnancyUserId = recordDto.pregnancyUserId
        trimesterOrder = recordDto.trimesterOrder
    }
}