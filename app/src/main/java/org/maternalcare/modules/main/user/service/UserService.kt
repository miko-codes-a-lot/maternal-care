package org.maternalcare.modules.main.user.service

import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import io.realm.kotlin.types.RealmInstant
import org.maternalcare.modules.main.user.model.dto.UserCheckupDto
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.model.entity.User
import org.maternalcare.modules.main.user.model.entity.UserCheckup
import org.maternalcare.modules.main.user.model.mapper.toDTO
import org.maternalcare.modules.main.user.model.mapper.toEntity
import org.mongodb.kbson.ObjectId
import java.time.LocalDate
import java.time.ZoneOffset
import javax.inject.Inject

class UserService @Inject constructor(private val realm: Realm) {
    fun fetch(
        isResidence: Boolean = false,
        isArchive: Boolean = false,
        userId: ObjectId? = null,
        addressName: String? = null,
    ): List<UserDto> {
        val query = StringBuilder()
            .append("isResidence == $0")
            .append(" AND isArchive == $1")

        if (userId != null) query.append(" AND createdById == $2")
        if (addressName != null) query.append(" AND address = $3")

        return realm.query<User>(query.toString(), isResidence, isArchive, userId, addressName)
            .find()
            .map { user -> user.toDTO() }
    }

    fun fetchByCheckup(userId: ObjectId?, dateOfCheckup: String): List<UserDto> {
        val date = LocalDate.parse(dateOfCheckup.substring(0, 10))
        val startOfDay = date.atStartOfDay().toInstant(ZoneOffset.UTC)
        val endOfDay = date.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC).minusMillis(1)

        val startRealmInstant = RealmInstant.from(startOfDay.epochSecond, startOfDay.nano)
        val endRealmInstant = RealmInstant.from(endOfDay.epochSecond, endOfDay.nano)

        val query = StringBuilder()
            .append("isArchive == false")
            .append(" AND dateOfCheckUp >= $1 AND dateOfCheckUp <= $2")

        if (userId != null) query.append(" AND createdById == $0")

        val checkups = realm.query<UserCheckup>(query.toString(), userId, startRealmInstant, endRealmInstant)
            .find()

        val users = mutableListOf<UserDto>()

        checkups.forEach { checkup ->
            val user = realm.query<User>("_id == $0", ObjectId(checkup.userId)).find().firstOrNull()
            if (user != null) {
                users.add(user.toDTO())
            }
        }

        return users
    }

    suspend fun upsert(data: UserDto, actionOf: UserDto): Result<UserDto> {
        return try {
            realm.write {
                data.createdById = actionOf.id
                data.lastUpdatedById = data.lastUpdatedById ?: actionOf.id
                val user = copyToRealm(data.toEntity(), updatePolicy = UpdatePolicy.ALL)
                Result.success(user.toDTO())
            }
        } catch (error: Exception) {
            Result.failure(error)
        }
    }

    suspend fun archiveOldResidences() {
        val fiveYearsAgoInstant = LocalDate.now().minusYears(5).atStartOfDay().toInstant(ZoneOffset.UTC)
        val fiveYearsAgoRealmInstant = RealmInstant.from(fiveYearsAgoInstant.epochSecond, 0)

        val usersToArchive = realm.query<User>(
            "isResidence == true AND isArchive == false AND createdAt < $0",
            fiveYearsAgoRealmInstant
        ).find()

        for (user in usersToArchive) {
            this.archiveUserCheckup(user._id.toHexString())
            val userDto = user.toDTO()
            userDto.isArchive = true
            realm.write {
                copyToRealm(userDto.toEntity(), updatePolicy = UpdatePolicy.ALL)
                Result.success("Archived ${user._id.toHexString()}")
            }
        }
    }

    private suspend fun archiveUserCheckup(residenceId: String) {
        val checkupsToArchive = realm.query<UserCheckup>("userId == $0", residenceId)
            .find()

        for (checkup in checkupsToArchive) {
            val checkupDto = checkup.toDTO()
            checkupDto.isArchive = true
            realm.write {
                copyToRealm(checkupDto.toEntity(), updatePolicy = UpdatePolicy.ALL)
                Result.success("Archived Checkup: ${checkup._id.toHexString()}")
            }
        }
    }

    suspend fun upsertCheckUp(data: UserCheckupDto): Result<UserCheckupDto> {
        return try {
            realm.write {
                val userCheckUp = copyToRealm(data.toEntity(), updatePolicy = UpdatePolicy.ALL)
                Result.success(userCheckUp.toDTO())
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
        ).sort("dateOfCheckUp", Sort.DESCENDING)
            .find()
            .firstOrNull()
            ?.run { toDTO() }
    }

    fun getGroupOfCheckupDates(adminId: ObjectId): List<UserCheckupDto> {
        return realm.query<UserCheckup>("createdById == $0 AND isArchive == false", adminId)
            .distinct("dateOfCheckUp")
            .sort("dateOfCheckUp", Sort.ASCENDING)
            .find()
            .map { it.toDTO() }
    }

    fun getMyUpcomingCheckup(residenceId: String): List<UserCheckupDto> {
        return realm.query<UserCheckup>("userId == $0", residenceId)
            .distinct("dateOfCheckUp")
            .sort("dateOfCheckUp", Sort.ASCENDING)
            .find()
            .map { it.toDTO() }
    }
}