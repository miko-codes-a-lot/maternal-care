package org.maternalcare.modules.main.user.model.mapper

import org.maternalcare.modules.main.user.model.dto.UserCheckupDto
import org.maternalcare.modules.main.user.model.entity.UserCheckup
import org.maternalcare.shared.ext.toInstantString
import org.maternalcare.shared.ext.toObjectId
import org.maternalcare.shared.ext.toRealmInstant

fun UserCheckup.toDTO(): UserCheckupDto {
    return UserCheckupDto(
        id = _id.toHexString(),
        userId = userId,
        bloodPressure = bloodPressure,
        height = height,
        weight = weight,
        lastMenstrualPeriod = lastMenstrualPeriod.toInstantString(),
        dateOfCheckUp = dateOfCheckUp.toInstantString(),
        scheduleOfNextCheckUp = scheduleOfNextCheckUp.toInstantString(),
        checkup = checkup
    )
}

fun UserCheckupDto.toEntity(): UserCheckup {
    val checkUpDto = this
    return UserCheckup().apply {
        _id = checkUpDto.id.toObjectId()
        userId = checkUpDto.userId
        bloodPressure = checkUpDto.bloodPressure
        height = checkUpDto.height
        weight = checkUpDto.weight
        lastMenstrualPeriod = checkUpDto.lastMenstrualPeriod.toRealmInstant()
        dateOfCheckUp = checkUpDto.dateOfCheckUp.toRealmInstant()
        scheduleOfNextCheckUp = checkUpDto.scheduleOfNextCheckUp.toRealmInstant()
        checkup = checkUpDto.checkup
    }
}