package org.maternalcare.modules.main.user.model.mapper

import io.realm.kotlin.types.RealmInstant
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.model.entity.User
import org.maternalcare.shared.ext.toObjectId
import org.maternalcare.shared.ext.toRealmInstant
import org.maternalcare.shared.util.DateUtil

fun User.toDTO(): UserDto {
    return UserDto(
        id = _id.toHexString(),
        firstName = firstName,
        middleName = middleName,
        lastName = lastName,
        email = email,
        mobileNumber = mobileNumber,
        dateOfBirth = DateUtil.from(dateOfBirth),
        password = password,
        createdBy = createdBy?.toString(),
        createdAt = DateUtil.from(createdAt),
        lastUpdatedBy = lastUpdatedBy?.toString(),
        lastUpdatedAt = DateUtil.from(lastUpdatedAt),
        deletedBy = deletedBy?.toString(),
        deletedAt = deletedAt?.run { DateUtil.from(this) },
        isSuperAdmin = isSuperAdmin,
        isAdmin = isAdmin,
        isResidence = isResidence,
        isActive = isActive,
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
        dateOfBirth = DateUtil.parse(userDto.dateOfBirth)
        password = userDto.password
        createdBy = userDto.createdBy?.toObjectId()
        createdAt = userDto.createdAt.toRealmInstant() ?: RealmInstant.now()
        lastUpdatedBy = userDto.lastUpdatedBy?.toObjectId()
        lastUpdatedAt = userDto.createdAt.toRealmInstant() ?: RealmInstant.now()
        deletedBy = userDto.deletedBy?.toObjectId()
        deletedAt = userDto.deletedAt?.run { DateUtil.parse(this) }
        isSuperAdmin = userDto.isSuperAdmin
        isAdmin = userDto.isAdmin
        isResidence = userDto.isResidence
        isActive = userDto.isActive
        imageBase64 = userDto.imageBase64
    }
}
