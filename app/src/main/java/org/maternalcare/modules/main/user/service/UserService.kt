package org.maternalcare.modules.main.user.service

import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.model.entity.User
import org.maternalcare.modules.main.user.model.mapper.UserMapper
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

class UserService @Inject constructor(private val realm: Realm) {
    fun fetch(isResidence: Boolean = false): List<UserDto> {
        return realm.query<User>("isResidence == $0", isResidence)
            .find()
            .map { user -> UserMapper.toDTO(user) }
    }

    suspend fun create(data: UserDto): Result<UserDto> {
        return try {
            realm.write {
                val user = copyToRealm(UserMapper.toEntity(data))
                Result.success(UserMapper.toDTO(user));
            }
        } catch (error: Exception) {
            Result.failure(error)
        }
    }

    fun fetchOne(userId: String): UserDto {
        return realm.query<User>("_id == $0", ObjectId(userId))
            .find()
            .first()
            .run {
                UserMapper.toDTO(this)
            }
    }
}