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
        firstDoseGiven = firstDoseGiven?.run { this.toInstantStringNullable() },
        firstDoseReturn = firstDoseReturn.run { this.toInstantStringNullable() },
        secondDoseGiven = secondDoseGiven.run { this.toInstantStringNullable() },
        secondDoseReturn = secondDoseReturn.run { this.toInstantStringNullable() },
        thirdDoseGiven = thirdDoseGiven.run { this.toInstantStringNullable() },
        thirdDoseReturn = thirdDoseReturn.run { this.toInstantStringNullable() },
        fourthDoseGiven = fourthDoseGiven.run { this.toInstantStringNullable() },
        fourthDoseReturn = fourthDoseReturn.run { this.toInstantStringNullable() },
        fifthDoseGiven = fifthDoseGiven.run { this.toInstantStringNullable() },
        fifthDoseReturn = fifthDoseReturn.run { this.toInstantStringNullable() },
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
        firstDoseGiven = immunizationDto.firstDoseGiven.toRealmInstantNullable()
        firstDoseReturn = immunizationDto.firstDoseReturn.toRealmInstantNullable()
        secondDoseGiven = immunizationDto.secondDoseGiven.toRealmInstantNullable()
        secondDoseReturn = immunizationDto.secondDoseReturn.toRealmInstantNullable()
        thirdDoseGiven = immunizationDto.thirdDoseGiven.toRealmInstantNullable()
        thirdDoseReturn = immunizationDto.thirdDoseReturn.toRealmInstantNullable()
        fourthDoseGiven = immunizationDto.fourthDoseGiven.toRealmInstantNullable()
        fourthDoseReturn = immunizationDto.fourthDoseReturn.toRealmInstantNullable()
        fifthDoseGiven = immunizationDto.fifthDoseGiven.toRealmInstantNullable()
        fifthDoseReturn = immunizationDto.fifthDoseReturn.toRealmInstantNullable()
        createdById = immunizationDto.createdById?.toObjectId()
        createdAt = immunizationDto.createdAt.toRealmInstantNullable() ?: RealmInstant.now()
        lastUpdatedById = immunizationDto.lastUpdatedById?.toObjectId()
        lastUpdatedAt = immunizationDto.createdAt.toRealmInstantNullable() ?: RealmInstant.now()
        deletedById = immunizationDto.deletedById?.toObjectId()
        deletedAt = immunizationDto.deletedAt.toRealmInstantNullable()
    }
}