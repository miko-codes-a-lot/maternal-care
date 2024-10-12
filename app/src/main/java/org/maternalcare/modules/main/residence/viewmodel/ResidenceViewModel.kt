package org.maternalcare.modules.main.residence.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.maternalcare.modules.main.user.model.dto.AddressDto
import org.maternalcare.modules.main.user.model.dto.UserCheckupDto
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.service.AddressService
import org.maternalcare.modules.main.user.service.UserService
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

@HiltViewModel
class ResidenceViewModel @Inject constructor(
    private val userService: UserService,
    private val addressService: AddressService
) : ViewModel() {
    fun fetchAddresses(): List<AddressDto> {
        return addressService.fetch()
    }

    fun fetchOneAddress(addressId: ObjectId): AddressDto {
        return addressService.fetchOne(addressId)
    }

    suspend fun upsertAddress(addressDto: AddressDto): Result<AddressDto> {
        return addressService.upsert(addressDto)
    }

    fun fetchUsers(
        userId: ObjectId,
        isSuperAdmin: Boolean = false,
        addressName: String?,
        isArchive: Boolean = false,
        isCompleted: Boolean? = null,
    ): List<UserDto> {
        val id = if (!isSuperAdmin) userId else null
        return userService.fetch(
            isResidence = true,
            isArchive = isArchive,
            isCompleted = isCompleted,
            userId = id,
            addressName = addressName
        )
    }

    fun getCheckupForUser(userId: String): UserCheckupDto? {
        return userService.fetchCheckUpDetails(userId)
    }

    fun fetchUsersByCheckup(
        userId: ObjectId,
        isSuperAdmin: Boolean = false,
        dateOfCheckup: String
    ): List<UserDto> {
        val id = if (!isSuperAdmin) userId else null
        return userService.fetchByCheckup(userId = id, dateOfCheckup = dateOfCheckup)
    }

    fun fetchAllUsersByCheckup(
        userId: ObjectId,
        isSuperAdmin: Boolean = false,
        checkup: Int,
        isArchive: Boolean = false,
    ): List<UserDto> {
        val id = if(!isSuperAdmin) userId else null
        return userService.fetchAllCheckup(userId = id, checkup = checkup, isArchive = isArchive )
    }

    fun fetchAllUsersWithNormalCondition(
        userId: ObjectId,
        isSuperAdmin: Boolean = false,
        isNormal: Boolean,
        isArchive: Boolean = false
    ): List<UserDto> {
        val id = if(!isSuperAdmin) userId else null
        return userService.fetchUsersWithNormalCondition(userId = id, isNormal = isNormal, isArchive = isArchive)
    }

    fun fetchAllUsersWithCriticalCondition(
        userId: ObjectId,
        isSuperAdmin: Boolean = false,
        isCritical: Boolean,
        isArchive: Boolean = false
    ): List<UserDto> {
        val id = if(!isSuperAdmin) userId else null
        return userService.fetchUsersWithCriticalCondition(userId = id, isCritical = isCritical, isArchive = isArchive)
    }

}