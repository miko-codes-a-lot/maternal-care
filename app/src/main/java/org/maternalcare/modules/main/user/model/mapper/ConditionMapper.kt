package org.maternalcare.modules.main.user.model.mapper

import io.realm.kotlin.types.RealmInstant
import org.maternalcare.shared.ext.toInstantStringNullable
import org.maternalcare.shared.ext.toObjectId
import org.maternalcare.modules.main.user.model.dto.UserConditionDto
import org.maternalcare.modules.main.user.model.entity.UserCondition
import org.maternalcare.shared.ext.toRealmInstantNullable

fun UserCondition.toDTO(): UserConditionDto{
    return UserConditionDto(
        id = _id.toHexString(),
        userId = userId,
        tuberculosisPersonal = tuberculosisPersonal,
        tuberculosisFamily = tuberculosisFamily,
        heartDiseasesPersonal = heartDiseasesPersonal,
        heartDiseasesFamily = heartDiseasesFamily,
        diabetesPersonal = diabetesPersonal,
        diabetesFamily = diabetesFamily,
        hypertensionPersonal = hypertensionPersonal,
        hypertensionFamily = hypertensionFamily,
        branchialAsthmaPersonal = branchialAsthmaPersonal,
        branchialAsthmaFamily = branchialAsthmaFamily,
        urinaryTractInfectionPersonal = urinaryTractInfectionPersonal,
        urinaryTractInfectionFamily = urinaryTractInfectionFamily,
        parasitismPersonal = parasitismPersonal,
        parasitismFamily = parasitismFamily,
        goitersPersonal = goitersPersonal,
        goitersFamily = goitersFamily,
        anemiaPersonal = anemiaPersonal,
        anemiaFamily = anemiaFamily,
        isNormal = isNormal,
        isCritical = isCritical,
        genitalTractInfection = genitalTractInfection,
        otherInfectionsDiseases = otherInfectionsDiseases,
        notes = notes,
        createdById = createdById?.toHexString(),
        createdAt = createdAt.toInstantStringNullable(),
        lastUpdatedById = lastUpdatedById?.toHexString(),
        lastUpdatedAt = lastUpdatedAt.toInstantStringNullable(),
        deletedById = deletedById?.toHexString(),
        deletedAt = deletedAt?.run { this.toInstantStringNullable() },
    )
}

fun UserConditionDto.toEntity(): UserCondition {
    val conditionDto = this
    return UserCondition().apply {
        _id = conditionDto.id.toObjectId()
        userId = conditionDto.userId
        tuberculosisPersonal = conditionDto.tuberculosisPersonal
        tuberculosisFamily = conditionDto.tuberculosisFamily
        heartDiseasesPersonal = conditionDto.heartDiseasesPersonal
        heartDiseasesFamily = conditionDto.heartDiseasesFamily
        diabetesPersonal = conditionDto.diabetesPersonal
        diabetesFamily = conditionDto.diabetesFamily
        hypertensionPersonal = conditionDto.hypertensionPersonal
        hypertensionFamily = conditionDto.hypertensionFamily
        branchialAsthmaPersonal = conditionDto.branchialAsthmaPersonal
        branchialAsthmaFamily = conditionDto.branchialAsthmaFamily
        urinaryTractInfectionPersonal = conditionDto.urinaryTractInfectionPersonal
        urinaryTractInfectionFamily = conditionDto.urinaryTractInfectionFamily
        parasitismPersonal = conditionDto.parasitismPersonal
        parasitismFamily = conditionDto.parasitismFamily
        goitersPersonal = conditionDto.goitersPersonal
        goitersFamily = conditionDto.goitersFamily
        anemiaPersonal = conditionDto.anemiaPersonal
        anemiaFamily = conditionDto.anemiaFamily
        isNormal = conditionDto.isNormal
        isCritical = conditionDto.isCritical
        genitalTractInfection = conditionDto.genitalTractInfection
        otherInfectionsDiseases = conditionDto.otherInfectionsDiseases
        notes = conditionDto.notes
        createdById = conditionDto.createdById?.toObjectId()
        createdAt = conditionDto.createdAt.toRealmInstantNullable() ?: RealmInstant.now()
        lastUpdatedById = conditionDto.lastUpdatedById?.toObjectId()
        lastUpdatedAt = conditionDto.createdAt.toRealmInstantNullable() ?: RealmInstant.now()
        deletedById = conditionDto.deletedById?.toObjectId()
        deletedAt = conditionDto.deletedAt.toRealmInstantNullable()
    }
}