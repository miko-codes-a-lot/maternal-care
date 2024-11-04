package org.maternalcare.modules.main.user.service

import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import io.realm.kotlin.types.RealmInstant
import org.maternalcare.modules.main.user.model.dto.UserBirthRecordDto
import org.maternalcare.modules.main.user.model.dto.UserCheckupDto
import org.maternalcare.modules.main.user.model.dto.UserConditionDto
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.model.dto.UserImmunizationDto
import org.maternalcare.modules.main.user.model.dto.UserTrimesterRecordDto
import org.maternalcare.modules.main.user.model.entity.Address
import org.maternalcare.modules.main.user.model.entity.User
import org.maternalcare.modules.main.user.model.entity.UserBirthRecord
import org.maternalcare.modules.main.user.model.entity.UserCheckup
import org.maternalcare.modules.main.user.model.entity.UserCondition
import org.maternalcare.modules.main.user.model.entity.UserImmunization
import org.maternalcare.modules.main.user.model.entity.UserTrimesterRecord
import org.maternalcare.modules.main.user.model.mapper.toDTO
import org.maternalcare.modules.main.user.model.mapper.toEntity
import org.maternalcare.shared.ext.hashPassword
import org.mongodb.kbson.BsonObjectId.Companion.invoke
import org.mongodb.kbson.ObjectId
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import javax.inject.Inject

class UserService @Inject constructor(private val realm: Realm) {
    fun fetchByEmail(email: String): UserDto? {
        val user = realm.query<User>("email == $0", email).find().firstOrNull()
        return user?.toDTO()
    }

    fun fetchEmailAndToken(email: String, token: String): UserDto? {
        val user = realm.query<User>("email == $0 AND resetPasswordToken == $1", email, token).find().firstOrNull()
        if (user != null) {
            return user.toDTO()
        }
        return null
    }

    suspend fun saveNewPassword(email: String, token: String, newPassword: String): Boolean {
        val hashedPassword = newPassword.hashPassword()
        return realm.write {
            val user = fetchEmailAndToken(email, token)
            if (user != null) {
                user.password = hashedPassword
                copyToRealm(user.toEntity(), updatePolicy = UpdatePolicy.ALL)
                true
            } else {
                false
            }
        }
    }

    fun fetch(
        isResidence: Boolean = false,
        isArchive: Boolean = false,
        isCompleted: Boolean? = null,
        userId: ObjectId? = null,
        addressName: String? = null,
    ): List<UserDto> {
        val query = StringBuilder()
            .append("isResidence == $0")
            .append(" AND isArchive == $1")

        if (isCompleted != null) query.append(" AND isCompleted == $2")
        if (userId != null) query.append(" AND createdById == $3")
        if (addressName != null) query.append(" AND address = $4")

        return realm.query<User>(
            query.toString(), isResidence, isArchive, isCompleted, userId, addressName
        )
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

    fun fetchAllCheckup(userId: ObjectId?, checkup: Int, isArchive: Boolean = false): List<UserDto> {
        val query = StringBuilder()
            .append("isArchive == false")
            .append(" AND checkup >= $0")
        if (userId != null) query.append(" AND createdById == $0")
        val checkups = realm.query<UserCheckup>(query.toString(), checkup, userId
        ).find()
        val users = mutableListOf<UserDto>()
        val seenUserIds = mutableSetOf<String>()
        checkups.forEach { userCheckup ->
            if (!seenUserIds.contains(userCheckup.userId)) {
                val user = realm.query<User>(
                    "_id == $0 AND isResidence == true AND isArchive == false",
                    ObjectId(userCheckup.userId)
                ).find().firstOrNull()
                if (user != null) {
                    users.add(user.toDTO())
                    seenUserIds.add(userCheckup.userId)
                }
            }
        }
        return users
    }

    fun fetchUsersWithNormalCondition(userId: ObjectId?, isNormal: Boolean, isArchive: Boolean = false): List<UserDto> {
        val query = StringBuilder()
            .append("isArchive == false")
            .append(" AND isNormal == $0")
        if (userId != null) query.append(" AND createdById == $0")
        val conditions = realm.query<UserCondition>(query.toString(), isNormal, userId
        ).find()
        val users = mutableListOf<UserDto>()
        val seenUserIds = mutableSetOf<String>()
        conditions.forEach { userConditions ->
            if (!seenUserIds.contains(userConditions.userId)) {
                val user = realm.query<User>(
                    "_id == $0 AND isResidence == true AND isArchive == false",
                    ObjectId(userConditions.userId)
                ).find().firstOrNull()
                if (user != null) {
                    users.add(user.toDTO())
                    seenUserIds.add(userConditions.userId)
                }
            }
        }
        return users
    }

    fun fetchUsersWithCriticalCondition(userId: ObjectId?, isCritical: Boolean, isArchive: Boolean = false): List<UserDto> {
        val query = StringBuilder()
            .append("isArchive == false")
            .append(" AND isCritical == $0")
        if (userId != null) query.append(" AND createdById == $0")
        val conditions = realm.query<UserCondition>(query.toString(), isCritical, userId
        ).find()
        val users = mutableListOf<UserDto>()
        val seenUserIds = mutableSetOf<String>()
        conditions.forEach { userConditions ->
            if (!seenUserIds.contains(userConditions.userId)) {
                val user = realm.query<User>(
                    "_id == $0 AND isResidence == true AND isArchive == false",
                    ObjectId(userConditions.userId)
                ).find().firstOrNull()
                if (user != null) {
                    users.add(user.toDTO())
                    seenUserIds.add(userConditions.userId)
                }
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

    suspend fun updateCompletionStatus() {
        val checkups = realm.query<UserCheckup>()
            .sort("userId", Sort.ASCENDING)
            .find()

        val userCheckupCount = checkups
            .groupBy { it.userId }
            .mapValues { it.value.size }

        userCheckupCount.forEach { (userId, count) ->
            val userDto = realm.query<User>("_id == $0", ObjectId(userId))
                .find()
                .firstOrNull()
                ?.toDTO()

            if (userDto == null) return

            realm.write {
                userDto.isCompleted = count == 4
                copyToRealm(userDto.toEntity(), updatePolicy = UpdatePolicy.ALL)
            }
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

    fun fetchCheckupDetailByNumber(userId: String, checkupNumber: Int, pregnantRecordId: String, pregnantTrimesterId: String): UserCheckupDto? {
        val userCheckup = realm.query<UserCheckup>("userId == $0 AND checkup == $1 AND pregnantRecordId == $2 AND trimesterRecordId == $3", userId, checkupNumber, pregnantRecordId, pregnantTrimesterId)
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

    fun getGroupOfCheckupDates(adminId: ObjectId): List<UserCheckupDto> {
        val currentDate: RealmInstant = RealmInstant.from(Instant.now().epochSecond, 0)
        val query = StringBuilder()
            .append("createdById == $0")
            .append(" AND scheduleOfNextCheckUp > $1")
        return realm.query<UserCheckup>(query.toString(), adminId, currentDate)
            .distinct("scheduleOfNextCheckUp")
            .sort("scheduleOfNextCheckUp", Sort.ASCENDING)
            .find()
            .map { it.toDTO() }
    }

    fun getAllUserWithCheckups(userId: String): List<UserCheckupDto> {
        val query = "userId == $0 AND checkup > 0"
        return realm.query<UserCheckup>(query, userId)
            .find()
            .map { it.toDTO() }
    }

    fun getMyUpcomingCheckup(residenceId: String): List<UserCheckupDto> {
        val currentDate: RealmInstant = RealmInstant.from(Instant.now().epochSecond, 0)
        val query = StringBuilder()
            .append("userId == $0")
            .append(" AND scheduleOfNextCheckUp > $1")
        return realm.query<UserCheckup>(query.toString(), residenceId, currentDate)
            .distinct("scheduleOfNextCheckUp")
            .sort("scheduleOfNextCheckUp", Sort.ASCENDING)
            .find()
            .map { it.toDTO() }
    }

    fun fetchAddressCheckupPercentage(isArchive: Boolean = false): Map<String, Double> {
        val addresses = realm.query<Address>().find()
        val addressPercentageMap = mutableMapOf<String, Double>()
        var completedAddressCount = 0
        addresses.forEach { address ->
            val usersAtAddress = realm.query<User>( "address == $0 AND isResidence == true AND isArchive == $1", address.name, isArchive ).find()
            val usersWithFourCheckups = usersAtAddress.filter { user ->
                val checkups = realm.query<UserCheckup>(
                    "userId == $0",
                    user._id.toHexString()
                ).find()
                checkups.size >= 3
            }
            val percentage = if (usersAtAddress.isNotEmpty()) {
                (usersWithFourCheckups.size.toDouble() / usersAtAddress.size.toDouble()) * 100
            } else {
                0.0
            }
            addressPercentageMap[address.name] = percentage
            if (usersWithFourCheckups.isNotEmpty()) { completedAddressCount++ }
        }
        val totalAddresses = addresses.size
        val overallPercentage = if (totalAddresses > 0) {
            (completedAddressCount.toDouble() / totalAddresses.toDouble()) * 100
        } else {
            0.0
        }
        addressPercentageMap["Overall Completed Address Percentage"] = overallPercentage
        return addressPercentageMap
    }

    fun fetchAddressWithCompleteCheckup(isArchive: Boolean = false): Map<String, Map<String,Int>>{
        val addresses = realm.query<Address>().find()
        return addresses.associate { address ->
            val usersAtAddress = realm.query<User>(
                "address == $0 And isResidence == true And isArchive == $1", address.name, isArchive
            ).find()
            val (usersWithFourCheckups, usersWithoutFourCheckups) = usersAtAddress.partition { user ->
                val checkups = realm.query<UserCheckup>("userId == $0", user._id.toHexString()).find()
                checkups.size >= 3
            }
            val totalUsers = usersAtAddress.size
            val usersCompleteCheckup = if(totalUsers > 0) (usersWithFourCheckups.size) else 0
            val usersNotCompleteCheckup = if(totalUsers > 0) (usersWithoutFourCheckups.size) else 0
            address.name to mapOf(
                "Complete" to usersCompleteCheckup,
                "Incomplete" to usersNotCompleteCheckup
            )
        }
    }

    fun fetchUserConditionByUserId(userId: String): UserConditionDto? {
        val result = realm.query<UserCondition>("userId == $0", userId)
            .find()
            .firstOrNull()
        return result?.toDTO()
    }

    fun fetchUserConditionByRecord(userId: String): UserConditionDto? {
        val result = realm.query<UserCondition>("userId == $0", userId)
            .find()
            .firstOrNull()
        return result?.toDTO()
    }

    suspend fun upsertCondition(data: UserConditionDto): Result<UserConditionDto> {
        return try {
            realm.write {
                val userCondition = copyToRealm(data.toEntity(), updatePolicy = UpdatePolicy.ALL)
                Result.success(userCondition.toDTO())
            }
        } catch (error: Exception) {
            Result.failure(error)
        }
    }

    fun fetchUserImmunizationByUserId(userId: String, pregnantRecordId: String): UserImmunizationDto? {
        val result = realm.query<UserImmunization>("userId == $0 And pregnantRecordId == $1", userId ,pregnantRecordId)
            .find()
            .firstOrNull()
        return result?.toDTO()
    }

    suspend fun upsertImmunization(dates: UserImmunizationDto): Result<UserImmunizationDto> {
        return try {
            realm.write {
                val userImmunization = copyToRealm(dates.toEntity(), updatePolicy = UpdatePolicy.ALL)
                Result.success(userImmunization.toDTO())
            }
        } catch (error: Exception) {
            Result.failure(error)
        }
    }

    suspend fun upsertHealthRecord(records: UserBirthRecordDto): Result<UserBirthRecordDto> {
        return try {
            realm.write {
                val userHealthRecord = copyToRealm(records.toEntity(), updatePolicy = UpdatePolicy.ALL)
                Result.success(userHealthRecord.toDTO())
            }
        } catch (error: Exception) {
            Result.failure(error)
        }
    }

    fun fetchOnePregnancy(pregnancyRecordId: String): UserBirthRecordDto {
        return realm.query<UserBirthRecord>("_id == $0", ObjectId(pregnancyRecordId))
            .find()
            .first()
            .run {
                toDTO()
            }
    }

    fun fetchListHealthRecordUser(userId: String): List<UserBirthRecordDto> {
        return realm.query<UserBirthRecord>("pregnancyUserId == $0", userId)
            .sort("childOrder", Sort.DESCENDING)
            .find()
            .map { it.toDTO() }
    }

    fun fetchOneTrimester(trimesterUserId: String): UserTrimesterRecordDto {
        return realm.query<UserTrimesterRecord>("_id == $0", ObjectId(trimesterUserId))
            .find()
            .first()
            .run {
                toDTO()
            }
    }

    fun fetchListTrimesterRecordUser(pregnantTrimesterId: String, pregnantRecordId: String): List<UserTrimesterRecordDto> {
        return realm.query<UserTrimesterRecord>(
            "trimesterUserId == $0 AND pregnancyUserId == $1",
            pregnantTrimesterId, pregnantRecordId
        )
            .sort("trimesterOrder", Sort.ASCENDING)
            .find()
            .map { it.toDTO() }
    }

    suspend fun upsertTrimesterRecord(trimesterRecord: UserTrimesterRecordDto): Result<UserTrimesterRecordDto> {
        return try {
            realm.write {
                val userTrimesterRecord = copyToRealm(trimesterRecord.toEntity(), updatePolicy = UpdatePolicy.ALL)
                Result.success(userTrimesterRecord.toDTO())
            }
        } catch (error: Exception) {
            Result.failure(error)
        }
    }
}