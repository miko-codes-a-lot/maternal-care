package org.maternalcare.modules.main.user.model.mapper

import io.realm.kotlin.types.RealmInstant
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.model.entity.UserEntity
import org.maternalcare.shared.ext.toObjectId
import org.maternalcare.shared.ext.toRealmInstant
import org.maternalcare.shared.util.DateUtil
import org.mongodb.kbson.ObjectId

object UserMapper {
    fun toDTO(user: UserEntity): UserDto {
        return UserDto(
            id = user._id.toHexString(),
            firstName = user.firstName,
            middleName = user.middleName,
            lastName = user.lastName,
            email = user.email,
            mobileNumber = user.mobileNumber,
            dateOfBirth = DateUtil.from(user.dateOfBirth),
            password = user.password,
            createdBy = user.createdBy?._id?.toString(),
            createdAt = DateUtil.from(user.createdAt),
            lastUpdatedBy = user.lastUpdatedBy?._id?.toString(),
            lastUpdatedAt = DateUtil.from(user.lastUpdatedAt),
            deletedBy = user.deletedBy?._id?.toString(),
            deletedAt = user.deletedAt?.run { DateUtil.from(this) },
            isSuperAdmin = user.isSuperAdmin,
            isAdmin = user.isAdmin,
            isResidence = user.isResidence,
            isActive = user.isActive
        )
    }

    fun toEntity(user: UserDto): UserEntity {
        return UserEntity().apply {
            _id = user.id.toObjectId()
            firstName = user.firstName
            middleName = user.middleName
            lastName = user.lastName
            email = user.email
            mobileNumber = user.mobileNumber
            dateOfBirth = DateUtil.parse(user.dateOfBirth)
            password = user.password
            createdBy = user.createdBy?.run { UserEntity().apply { _id = ObjectId(this@run) } }
            createdAt = user.createdAt.toRealmInstant() ?: RealmInstant.now()
            lastUpdatedBy = user.lastUpdatedBy?.run { UserEntity().apply { _id = ObjectId(this@run) }}
            lastUpdatedAt = user.createdAt.toRealmInstant() ?: RealmInstant.now()
            deletedBy = user.deletedBy?.run { UserEntity().apply { _id = ObjectId(this@run) } }
            deletedAt = user.deletedAt?.run { DateUtil.parse(this) }
            isSuperAdmin = user.isSuperAdmin
            isAdmin = user.isAdmin
            isResidence = user.isResidence
            isActive = user.isActive
        }
    }
}