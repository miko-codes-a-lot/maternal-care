package org.maternalcare.modules.main.user.model.mapper

import io.realm.kotlin.types.RealmInstant
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.model.entity.User
import org.maternalcare.shared.ext.toInstantString
import org.maternalcare.shared.ext.toInstantStringNullable
import org.maternalcare.shared.ext.toObjectId
import org.maternalcare.shared.ext.toRealmInstant
import org.maternalcare.shared.ext.toRealmInstantNullable

fun User.toDTO(): UserDto {
    return UserDto(
        id = _id.toHexString(),
        firstName = firstName,
        middleName = middleName,
        lastName = lastName,
        email = email,
        mobileNumber = mobileNumber,
        address = address,
        dateOfBirth = dateOfBirth.toInstantString(),
        password = password,
        createdById = createdById?.toHexString(),
        createdAt = createdAt.toInstantStringNullable(),
        lastUpdatedById = lastUpdatedById?.toHexString(),
        lastUpdatedAt = lastUpdatedAt.toInstantStringNullable(),
        deletedById = deletedById?.toHexString(),
        deletedAt = deletedAt?.run { this.toInstantStringNullable() },
        isSuperAdmin = isSuperAdmin,
        isAdmin = isAdmin,
        isResidence = isResidence,
        isActive = isActive,
        isArchive = isArchive,
        imageBase64 = imageBase64
    )
}



fun UserDto.toEntity(): User {
    val userDto = this
    return User().apply {
        _id = userDto.id.toObjectId()
        firstName = userDto.firstName
        middleName = userDto.middleName
        lastName = userDto.lastName
        email = userDto.email
        mobileNumber = userDto.mobileNumber
        address = userDto.address
        dateOfBirth = userDto.dateOfBirth.toRealmInstant()
        password = userDto.password
        createdById = userDto.createdById?.toObjectId()
        createdAt = userDto.createdAt.toRealmInstantNullable() ?: RealmInstant.now()
        lastUpdatedById = userDto.lastUpdatedById?.toObjectId()
        lastUpdatedAt = userDto.createdAt.toRealmInstantNullable() ?: RealmInstant.now()
        deletedById = userDto.deletedById?.toObjectId()
        deletedAt = userDto.deletedAt.toRealmInstantNullable()
        isSuperAdmin = userDto.isSuperAdmin
        isAdmin = userDto.isAdmin
        isResidence = userDto.isResidence
        isActive = userDto.isActive
        isArchive = userDto.isArchive
        imageBase64 = userDto.imageBase64
    }
}
