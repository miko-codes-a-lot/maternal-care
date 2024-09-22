package org.maternalcare.modules.main.user.service

import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import org.maternalcare.modules.main.user.model.dto.UserCheckupDto
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.model.entity.User
import org.maternalcare.modules.main.user.model.entity.UserCheckup
import org.maternalcare.modules.main.user.model.mapper.toDTO
import org.maternalcare.modules.main.user.model.mapper.toEntity
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

class UserService @Inject constructor(private val realm: Realm) {
    fun fetch(
        isResidence: Boolean = false,
        userId: ObjectId? = null,
        addressName: String? = null,
    ): List<UserDto> {
        val query = StringBuilder()
            .append("isResidence == $0")

        if (userId != null) query.append(" AND createdById == $1")
        if (addressName != null) query.append(" AND address = $2")

        return realm.query<User>(query.toString(), isResidence, userId, addressName)
            .find()
            .map { user -> user.toDTO() }
    }

    suspend fun upsert(data: UserDto, actionOf: UserDto): Result<UserDto> {
        return try {
            realm.write {
                data.createdById = actionOf.id
                data.lastUpdatedById = data.lastUpdatedById ?: actionOf.id
                val user = copyToRealm(data.toEntity(), updatePolicy = UpdatePolicy.ALL)
                Result.success(user.toDTO());
            }
        } catch (error: Exception) {
            Result.failure(error)
        }
    }

    suspend fun upsertCheckUp(data: UserCheckupDto): Result<UserCheckupDto> {
        return try {
            realm.write {
                val userCheckUp = copyToRealm(data.toEntity(), updatePolicy = UpdatePolicy.ALL)
                Result.success(userCheckUp.toDTO());
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

    fun fetchOneCheckup(checkupId: ObjectId): UserCheckupDto {
        return realm.query<UserCheckup>("_id == $0", checkupId)
            .find()
            .first()
            .run {
                toDTO()
            }
    }

    fun fetchCheckupDetailByNumber(userId: String, checkupNumber: Int): UserCheckupDto? {
        val userCheckup = realm.query<UserCheckup>("userId == $0 AND checkup == $1", userId, checkupNumber)
            .find()
            .firstOrNull()

        return userCheckup?.toDTO()
    }

    suspend fun saveProfilePicture(userId: String, imageUri: ByteArray?): Result<UserDto> {
        return try {
            realm.write {
                val user = query<User>("_id == $0", ObjectId(userId)).find().firstOrNull()
                if (user != null && imageUri != null) {
                    val base64Image =
                        android.util.Base64.encodeToString(imageUri, android.util.Base64.DEFAULT)
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

    fun fetchCheckUpDetails(userId: String): UserCheckupDto? {
        return realm.query<UserCheckup>(
            "userId == $0",
            userId
        ).sort("scheduleOfNextCheckUp", Sort.DESCENDING)
            .find()
            .firstOrNull()
            ?.run { toDTO() }
    }

    fun getGroupOfCheckupDates(): List<UserCheckupDto> {
        return realm.query<UserCheckup>()
            .distinct("userId")
            .sort("scheduleOfNextCheckUp", Sort.DESCENDING)
            .find()
            .map { it.toDTO() }
    }
}