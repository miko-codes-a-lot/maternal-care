package org.maternalcare.modules.main.user.service

import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.model.entity.UserEntity
import org.maternalcare.modules.main.user.model.mapper.UserMapper
import javax.inject.Inject

class UserService @Inject constructor(private val realm: Realm) {
    fun fetch(): List<UserDto> {
        return realm.query<UserEntity>()
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
}