package org.maternalcare.modules.main.user.model.mapper

import io.realm.kotlin.types.RealmInstant
import org.maternalcare.modules.main.user.model.dto.UserCheckupDto
import org.maternalcare.modules.main.user.model.entity.UserCheckup
import org.maternalcare.shared.ext.toInstantString
import org.maternalcare.shared.ext.toInstantStringNullable
import org.maternalcare.shared.ext.toObjectId
import org.maternalcare.shared.ext.toRealmInstant
import org.maternalcare.shared.ext.toRealmInstantNullable

fun UserCheckup.toDTO(): UserCheckupDto {
    return UserCheckupDto(
        id = _id.toHexString(),
        userId = userId,
        bloodPressure = bloodPressure,
        height = height,
        weight = weight,
        typeOfVaccine = typeOfVaccine,
        lastMenstrualPeriod = lastMenstrualPeriod.toInstantString(),
        dateOfCheckUp = dateOfCheckUp.toInstantString(),
        scheduleOfNextCheckUp = scheduleOfNextCheckUp.toInstantString(),
        checkup = checkup,
        createdById = createdById?.toHexString(),
        createdAt = createdAt.toInstantStringNullable(),
        lastUpdatedById = lastUpdatedById?.toHexString(),
        lastUpdatedAt = lastUpdatedAt.toInstantStringNullable(),
        deletedById = deletedById?.toHexString(),
        deletedAt = deletedAt?.run { this.toInstantStringNullable() },
        isArchive = isArchive
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
        typeOfVaccine = checkUpDto.typeOfVaccine
        lastMenstrualPeriod = checkUpDto.lastMenstrualPeriod.toRealmInstant()
        dateOfCheckUp = checkUpDto.dateOfCheckUp.toRealmInstant()
        scheduleOfNextCheckUp = checkUpDto.scheduleOfNextCheckUp.toRealmInstant()
        checkup = checkUpDto.checkup
        createdById = checkUpDto.createdById?.toObjectId()
        createdAt = checkUpDto.createdAt.toRealmInstantNullable() ?: RealmInstant.now()
        lastUpdatedById = checkUpDto.lastUpdatedById?.toObjectId()
        lastUpdatedAt = checkUpDto.createdAt.toRealmInstantNullable() ?: RealmInstant.now()
        deletedById = checkUpDto.deletedById?.toObjectId()
        deletedAt = checkUpDto.deletedAt.toRealmInstantNullable()
        isArchive = checkUpDto.isArchive
    }
}