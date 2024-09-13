package org.maternalcare.modules.main.user.service

import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.model.entity.User
import org.maternalcare.modules.main.user.model.mapper.toDTO
import org.maternalcare.modules.main.user.model.mapper.toEntity
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

class UserService @Inject constructor(private val realm: Realm) {
    fun fetch(isResidence: Boolean = false): List<UserDto> {
        return realm.query<User>("isResidence == $0", isResidence)
            .find()
            .map { user -> user.toDTO() }
    }

    suspend fun upsert(data: UserDto): Result<UserDto> {
        return try {
            realm.write {
                val user = copyToRealm(data.toEntity(), updatePolicy = UpdatePolicy.ALL)
                Result.success(user.toDTO());
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
                toDTO()
            }
    }

    suspend fun saveProfilePicture(userId: String, imageUri: ByteArray?): Result<UserDto> {
        return try {
            realm.write {
                val user = query<User>("_id == $0", ObjectId(userId)).find().firstOrNull()
                if (user != null && imageUri != null) {
                    val base64Image = android.util.Base64.encodeToString(imageUri, android.util.Base64.DEFAULT)
                    user.imageBase64 = base64Image
                    copyToRealm(user, UpdatePolicy.ALL)
                    Result.success(user.toDTO())
                } else {
                    Result.failure(Exception())
                }
            }
        } catch (error: Exception) {
            Result.failure(error)
        }
    }
}