package org.maternalcare.modules.main.user.model.mapper

import io.realm.kotlin.types.RealmInstant
import org.maternalcare.modules.main.user.model.dto.UserImmunizationDto
import org.maternalcare.modules.main.user.model.entity.UserImmunization
import org.maternalcare.shared.ext.toInstantString
import org.maternalcare.shared.ext.toInstantStringNullable
import org.maternalcare.shared.ext.toObjectId
import org.maternalcare.shared.ext.toRealmInstant
import org.maternalcare.shared.ext.toRealmInstantNullable

fun UserImmunization.toDTO(): UserImmunizationDto{
    return UserImmunizationDto(
        id = _id.toHexString(),
        userId = userId,
        firstDoseGiven = firstDoseGiven.toInstantString(),
        firstDoseReturn = firstDoseReturn.toInstantString(),
        secondDoseGiven = secondDoseGiven.toInstantString(),
        secondDoseReturn = secondDoseReturn.toInstantString(),
        thirdDoseGiven = thirdDoseGiven.toInstantString(),
        thirdDoseReturn = thirdDoseReturn.toInstantString(),
        fourthDoseGiven = fourthDoseGiven.toInstantString(),
        fourthDoseReturn = fourthDoseReturn.toInstantString(),
        fifthDoseGiven = fifthDoseGiven.toInstantString(),
        fifthDoseReturn = fifthDoseReturn.toInstantString(),
        createdById = createdById?.toHexString(),
        createdAt = createdAt.toInstantStringNullable(),
        lastUpdatedById = lastUpdatedById?.toHexString(),
        lastUpdatedAt = lastUpdatedAt.toInstantStringNullable(),
        deletedById = deletedById?.toHexString(),
        deletedAt = deletedAt?.run { this.toInstantStringNullable() },
    )
}

fun UserImmunizationDto.toEntity(): UserImmunization {
    val immunizationDto = this
    return UserImmunization().apply{
        _id = immunizationDto.id.toObjectId()
        userId = immunizationDto.userId
        firstDoseGiven = immunizationDto.firstDoseGiven.toRealmInstant()
        firstDoseReturn = immunizationDto.firstDoseReturn.toRealmInstant()
        secondDoseGiven = immunizationDto.secondDoseGiven.toRealmInstant()
        secondDoseReturn = immunizationDto.secondDoseReturn.toRealmInstant()
        thirdDoseGiven = immunizationDto.thirdDoseGiven.toRealmInstant()
        thirdDoseReturn = immunizationDto.thirdDoseReturn.toRealmInstant()
        fourthDoseGiven = immunizationDto.fourthDoseGiven.toRealmInstant()
        fourthDoseReturn = immunizationDto.fourthDoseReturn.toRealmInstant()
        fifthDoseGiven = immunizationDto.fifthDoseGiven.toRealmInstant()
        fifthDoseReturn = immunizationDto.fifthDoseReturn.toRealmInstant()
        createdById = immunizationDto.createdById?.toObjectId()
        createdAt = immunizationDto.createdAt.toRealmInstantNullable() ?: RealmInstant.now()
        lastUpdatedById = immunizationDto.lastUpdatedById?.toObjectId()
        lastUpdatedAt = immunizationDto.createdAt.toRealmInstantNullable() ?: RealmInstant.now()
        deletedById = immunizationDto.deletedById?.toObjectId()
        deletedAt = immunizationDto.deletedAt.toRealmInstantNullable()
    }
}