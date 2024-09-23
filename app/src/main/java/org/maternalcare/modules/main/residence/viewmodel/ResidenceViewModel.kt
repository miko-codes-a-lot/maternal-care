package org.maternalcare.modules.main.residence.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.maternalcare.modules.main.user.model.dto.AddressDto
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
    ): List<UserDto> {
        val id = if (!isSuperAdmin) userId else null
        return userService.fetch(isResidence = true, isArchive = isArchive, userId = id, addressName = addressName)
    }

    fun fetchUsersByCheckup(
        userId: ObjectId,
        isSuperAdmin: Boolean = false,
        dateOfCheckup: String
    ): List<UserDto> {
        val id = if (!isSuperAdmin) userId else null
        return userService.fetchByCheckup(userId = id, dateOfCheckup = dateOfCheckup)
    }
}